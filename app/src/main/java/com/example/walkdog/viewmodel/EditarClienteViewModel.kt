package com.example.walkdog.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.models.InputFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EditarClienteState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val data: Map<String, Any>? = null
)

class EditarClienteViewModel : ViewModel() {

    private val _state = MutableStateFlow(EditarClienteState())
    val state: StateFlow<EditarClienteState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_CLIENTE = "69236f5200282814eb3c"
    private val BUCKET_FOTOS = "693dc47f0030dd8f0250"

    // -------------------------------
    // CARREGAR DADOS DO CLIENTE
    // -------------------------------
    fun loadCliente() {
        viewModelScope.launch {
            try {
                _state.value = EditarClienteState(loading = true)

                val authUserId = AppwriteService.account.get().id

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CLIENTE,
                    queries = listOf(
                        Query.equal("userId", authUserId)
                    )
                )

                if (result.documents.isEmpty()) {
                    _state.value = EditarClienteState(
                        error = "Cliente não encontrado",
                        loading = false
                    )
                    return@launch
                }

                val cliente = result.documents.first()

                _state.value = EditarClienteState(
                    data = cliente.data,
                    loading = false
                )

            } catch (e: Exception) {
                Log.e("EDITAR_CLIENTE", "Erro ao carregar cliente", e)
                _state.value = EditarClienteState(error = e.message)
            }
        }
    }

    // -------------------------------
    // SALVAR ALTERAÇÕES
    // -------------------------------
    fun salvarAlteracoes(
        context: Context,
        nome: String,
        morada: String,
        codPostal: String,
        localidade: String,
        nif: String,
        email: String,
        iban: String,
        fotoUri: Uri?
    ) {
        viewModelScope.launch {
            try {
                _state.value = EditarClienteState(loading = true)

                val authUserId = AppwriteService.account.get().id

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CLIENTE,
                    queries = listOf(
                        Query.equal("userId", authUserId)
                    )
                )

                if (result.documents.isEmpty()) {
                    _state.value = EditarClienteState(error = "Cliente não encontrado")
                    return@launch
                }

                val clienteDoc = result.documents.first()

                // -------------------------------
                // UPLOAD DA FOTO (guarda só o ID)
                // -------------------------------
                var fotoId: String? = null

                if (fotoUri != null) {
                    val bytes = context.contentResolver
                        .openInputStream(fotoUri)
                        ?.readBytes()

                    if (bytes != null) {
                        val upload = AppwriteService.storage.createFile(
                            bucketId = BUCKET_FOTOS,
                            fileId = ID.unique(),
                            file = InputFile.fromBytes(
                                filename = "foto_cliente_$authUserId.jpg",
                                bytes = bytes
                            )
                        )
                        fotoId = upload.id
                    }
                }

                // -------------------------------
                // ATUALIZAR DOCUMENTO
                // -------------------------------
                AppwriteService.databases.updateDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CLIENTE,
                    documentId = clienteDoc.id,
                    data = mapOf(
                        "nome" to nome,
                        "morada" to morada,
                        "codpostal" to codPostal,
                        "localidade" to localidade,
                        "NIF" to nif,
                        "email" to email,
                        "iban" to iban,
                        "fotoId" to (fotoId ?: clienteDoc.data["fotoId"] ?: ""),
                        "userId" to authUserId
                    )
                )

                _state.value = EditarClienteState(success = true)

            } catch (e: Exception) {
                Log.e("EDITAR_CLIENTE", "Erro ao salvar alterações", e)
                _state.value = EditarClienteState(error = e.message)
            }
        }
    }
}

