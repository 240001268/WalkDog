package com.example.walkdog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.Query
import io.appwrite.models.Document
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ----------------------------------------------------
// UI MODELS
// ----------------------------------------------------
data class PasseioFornecedorUi(
    val id: String,
    val descricao: String,
    val duracao: String,
    val preco: String
)

data class PerfilFornecedorState(
    val loading: Boolean = false,
    val error: String? = null,
    val fornecedor: Document<Map<String, Any>>? = null,
    val passeios: List<PasseioFornecedorUi> = emptyList()
)

// ----------------------------------------------------
// VIEWMODEL
// ----------------------------------------------------
class PerfilFornecedorViewModel : ViewModel() {

    private val _state = MutableStateFlow(PerfilFornecedorState())
    val state: StateFlow<PerfilFornecedorState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_FORNECEDOR = "69236f93001828d82b6f"
    private val COLLECTION_PASSEIO = "693aeccc002dfd874f6d"

    // -------------------------------------------------
    // PERFIL DO FORNECEDOR (USER LOGADO)
    // -------------------------------------------------
    fun getFornecedorDataDoUserLogado() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val userId = AppwriteService.account.get().id

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    queries = listOf(Query.equal("userId", userId))
                )

                _state.value = _state.value.copy(
                    fornecedor = result.documents.firstOrNull(),
                    loading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }

    // ---------------------------------------------------------
// FORNECEDOR POR ID (PERFIL PÚBLICO)
// ---------------------------------------------------------
    fun getFornecedorData(fornecedorId: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val doc = AppwriteService.databases.getDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    documentId = fornecedorId
                )

                _state.value = _state.value.copy(
                    fornecedor = doc,
                    loading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }

    // ---------------------------------------------------------
    // PASSEIOS SELECIONADOS PELO FORNECEDOR
    // ---------------------------------------------------------
    fun getPasseiosFornecedor() {
        viewModelScope.launch {
            try {
                val fornecedor = _state.value.fornecedor ?: return@launch

                val ids = (fornecedor.data["passeiosSelecionados"] as? List<*>)
                    ?.map { it.toString() }
                    ?: emptyList()

                if (ids.isEmpty()) {
                    _state.value = _state.value.copy(passeios = emptyList())
                    return@launch
                }

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIO,
                    queries = listOf(Query.equal("\$id", ids))
                )

                val passeiosUi = result.documents.map { doc ->
                    PasseioFornecedorUi(
                        id = doc.id,
                        descricao = doc.data["descricao"]?.toString() ?: "—",
                        duracao = doc.data["duracao"]?.toString() ?: "0",
                        preco = doc.data["preco"]?.toString() ?: "0"
                    )
                }

                _state.value = _state.value.copy(passeios = passeiosUi)

            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}
