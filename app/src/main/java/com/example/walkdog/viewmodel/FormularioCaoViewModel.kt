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

data class FormularioCaoState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,

    // dados do dono (pré-preenchidos)
    val nomeDono: String = "",
    val emailDono: String = "",
    val telefoneDono: String = "",
    val localidadeDono: String = ""
)

class FormularioCaoViewModel : ViewModel() {

    private val _state = MutableStateFlow(FormularioCaoState())
    val state: StateFlow<FormularioCaoState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_CLIENTE = "69236f5200282814eb3c"
    private val COLLECTION_CAO = "692b22060025bfc8cade"
    private val BUCKET_FOTOS = "6930dbe0002154093a9f" // fotosCao

    // ------------------------------------------------
    // 1️⃣ CARREGAR DADOS DO DONO (CLIENTE)
    // ------------------------------------------------
    fun loadDono() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val authUserId = AppwriteService.account.get().id

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CLIENTE,
                    queries = listOf(
                        Query.equal("userId", authUserId)
                    )
                )

                if (result.documents.isEmpty()) {
                    _state.value = _state.value.copy(
                        loading = false,
                        error = "Cliente não encontrado"
                    )
                    return@launch
                }

                val cliente = result.documents.first().data

                _state.value = _state.value.copy(
                    loading = false,
                    nomeDono = cliente["nome"]?.toString() ?: "",
                    emailDono = cliente["email"]?.toString() ?: "",
                    telefoneDono = cliente["telefone"]?.toString() ?: "",
                    localidadeDono = cliente["localidade"]?.toString() ?: ""
                )

            } catch (e: Exception) {
                Log.e("FORMULARIO_CAO", "Erro ao carregar dono", e)
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }

    // ------------------------------------------------
    // 2️⃣ REGISTAR CÃO
    // ------------------------------------------------
    fun registarCao(
        context: Context,
        nome: String,
        raca: String,
        porte: String,
        peso: String,
        localidadeCao: String,
        fotoUri: Uri?
    ) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val authUserId = AppwriteService.account.get().id

                // -------------------------
                // UPLOAD DA FOTO (opcional)
                // -------------------------
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
                                filename = "foto_cao_$authUserId.jpg",
                                bytes = bytes
                            )
                        )
                        fotoId = upload.id
                    }
                }

                // -------------------------
                // CRIAR DOCUMENTO DO CÃO
                // -------------------------
                AppwriteService.databases.createDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CAO,
                    documentId = ID.unique(),
                    data = mapOf(
                        "nome" to nome,
                        "raca" to raca,
                        "porte" to porte,
                        "peso" to peso, // ✅ STRING
                        "localidade" to localidadeCao,
                        "fotoId" to (fotoId ?: ""),
                        "userId" to authUserId
                    )
                )

                _state.value = _state.value.copy(
                    loading = false,
                    success = true
                )

            } catch (e: Exception) {
                Log.e("FORMULARIO_CAO", "Erro ao registar cão", e)
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message ?: "Erro ao registar cão"
                )
            }
        }
    }
}
