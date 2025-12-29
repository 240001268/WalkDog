package com.example.walkdog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.Query
import io.appwrite.models.Document
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PerfilFornecedorState(
    val loading: Boolean = false,
    val error: String? = null,
    val fornecedor: Document<Map<String, Any>>? = null,
    val passeios: List<Document<Map<String, Any>>> = emptyList()
)

class PerfilFornecedorViewModel : ViewModel() {

    private val _state = MutableStateFlow(PerfilFornecedorState())
    val state: StateFlow<PerfilFornecedorState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_FORNECEDOR = "69236f93001828d82b6f"
    private val COLLECTION_PASSEIO = "693aeccc002dfd874f6d"

    // ---------------------------------------------------------
    // 1️⃣ Buscar dados do fornecedor (POR userId)
    // ---------------------------------------------------------
    fun getFornecedorData(fornecedorId: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val fornecedor = AppwriteService.databases.getDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    documentId = fornecedorId
                )

                _state.value = _state.value.copy(
                    fornecedor = fornecedor,
                    loading = false,
                    error = null
                )

            } catch (e: Exception) {
                Log.e("PERFIL_FORNECEDOR", "Erro ao obter fornecedor", e)
                _state.value = _state.value.copy(
                    error = "Fornecedor não encontrado",
                    loading = false
                )
            }
        }
    }

    // ---------------------------------------------------------
    // 2️⃣ Buscar os passeios do fornecedor (USANDO STATE)
    // ---------------------------------------------------------
    fun getPasseiosFornecedor() {
        viewModelScope.launch {
            try {
                val fornecedor = _state.value.fornecedor
                if (fornecedor == null) return@launch

                val selecionados = (fornecedor.data["passeiosSelecionados"] as? List<*>)
                    ?.map { it.toString() }
                    ?: emptyList()

                if (selecionados.isEmpty()) {
                    _state.value = _state.value.copy(passeios = emptyList())
                    return@launch
                }

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIO,
                    queries = listOf(
                        Query.equal("\$id", selecionados)
                    )
                )

                _state.value = _state.value.copy(
                    passeios = result.documents
                )

            } catch (e: Exception) {
                Log.e("PERFIL_FORNECEDOR", "Erro ao carregar passeios", e)
                _state.value = _state.value.copy(
                    error = e.message
                )
            }
        }
    }

    fun getFornecedorDataDoUserLogado() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val user = AppwriteService.account.get()

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    queries = listOf(
                        Query.equal("userId", user.id)
                    )
                )

                if (result.documents.isNotEmpty()) {
                    _state.value = _state.value.copy(
                        fornecedor = result.documents.first(),
                        loading = false,
                        error = null
                    )
                } else {
                    _state.value = _state.value.copy(
                        error = "Fornecedor não encontrado",
                        loading = false
                    )
                }

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message,
                    loading = false
                )
            }
        }
    }
}
