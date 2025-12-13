package com.example.walkdog.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.ID
import io.appwrite.models.InputFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EditarFornecedorState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val data: Map<String, Any>? = null
)

class EditarFornecedorViewModel : ViewModel() {

    private val _state = MutableStateFlow(EditarFornecedorState())
    val state: StateFlow<EditarFornecedorState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_FORNECEDOR = "69236f93001828d82b6f"
    private val BUCKET_FOTOS = "fornecedorFotos"

    // ----------------------------------------------------------
    // 1) BUSCAR DADOS DO FORNECEDOR
    // ----------------------------------------------------------
    fun loadFornecedor(userId: String) {
        viewModelScope.launch {
            try {
                _state.value = EditarFornecedorState(loading = true)

                val doc = AppwriteService.databases.getDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    documentId = userId
                )

                _state.value = EditarFornecedorState(
                    data = doc.data,
                    loading = false
                )

            } catch (e: Exception) {
                Log.e("EDITAR_FORNECEDOR", "Erro ao carregar fornecedor", e)
                _state.value = EditarFornecedorState(error = e.message)
            }
        }
    }

    // ----------------------------------------------------------
    // 2) SALVAR ALTERAÇÕES
    // ----------------------------------------------------------
    fun salvarAlteracoes(
        context: Context,
        userId: String,
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
                _state.value = EditarFornecedorState(loading = true)

                var fotoUrl: String? = null

                // Se o utilizador mudou a foto → atualizar
                if (fotoUri != null) {
                    val bytes = context.contentResolver.openInputStream(fotoUri)!!.readBytes()

                    val upload = AppwriteService.storage.createFile(
                        bucketId = BUCKET_FOTOS,
                        fileId = ID.unique(),
                        file = InputFile.fromBytes(
                            filename = "foto_${userId}.jpg",
                            bytes = bytes
                        )
                    )

                    val projectId = AppwriteService.client.config["project"]
                    fotoUrl =
                        "${AppwriteService.client.endpoint}/storage/buckets/$BUCKET_FOTOS/files/${upload.id}/view?project=$projectId"
                }

                // Atualizar documento
                AppwriteService.databases.updateDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    documentId = userId,
                    data = mapOf(
                        "nome" to nome,
                        "morada" to listOf(morada),
                        "codpostal" to codPostal.toInt(),
                        "localidade" to localidade,
                        "nif" to nif.toInt(),
                        "email" to email,
                        "IBAN" to iban,
                        "FotoID" to (fotoUrl ?: _state.value.data?.get("FotoID") ?: ""),
                        "fornecedorId" to userId
                    )
                )

                _state.value = EditarFornecedorState(success = true)

            } catch (e: Exception) {
                Log.e("EDITAR_FORNECEDOR", "Erro ao salvar alterações", e)
                _state.value = EditarFornecedorState(error = e.message)
            }
        }
    }
}
