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

data class PasseioFornecedorUi(
    val id: String,
    val descricao: String,
    val duracao: Int,
    val preco: Int
)

data class PerfilFornecedorState(
    val loading: Boolean = false,
    val error: String? = null,
    val fornecedor: Document<Map<String, Any>>? = null,
    val passeios: List<PasseioFornecedorUi> = emptyList()
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
    // 2️⃣ Buscar os passeios do fornecedor
    // ---------------------------------------------------------
    fun getPasseiosFornecedor() {
        viewModelScope.launch {
            try {
                val fornecedor = _state.value.fornecedor ?: return@launch

                val ids: List<String> =
                    (fornecedor.data["passeiosSelecionados"] as? List<*>)
                        ?.filterIsInstance<String>()
                        ?: emptyList()

                if (ids.isEmpty()) {
                    _state.value = _state.value.copy(passeios = emptyList())
                    return@launch
                }

                val passeios = ids.mapNotNull { passeioId ->
                    try {
                        val doc = AppwriteService.databases.getDocument(
                            databaseId = DB_ID,
                            collectionId = COLLECTION_PASSEIO,
                            documentId = passeioId
                        )

                        PasseioFornecedorUi(
                            id = doc.id,
                            descricao = doc.data["descricao"]?.toString() ?: "",
                            duracao = doc.data["duracao"]?.toString()?.toIntOrNull() ?: 0,
                            preco = doc.data["preco"]?.toString()?.toIntOrNull() ?: 0
                        )
                    } catch (_: Exception) {
                        null
                    }
                }

                _state.value = _state.value.copy(
                    passeios = passeios
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
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
