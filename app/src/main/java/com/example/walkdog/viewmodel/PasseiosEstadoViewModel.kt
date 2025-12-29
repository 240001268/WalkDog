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

data class PasseiosEstadoState(
    val loading: Boolean = false,
    val passeios: List<Document<Map<String, Any>>> = emptyList(),
    val estadoSelecionado: String = "todos",
    val userId: String? = null,
    val error: String? = null
)

class PasseiosEstadoViewModel : ViewModel() {

    private val _state = MutableStateFlow(PasseiosEstadoState())
    val state: StateFlow<PasseiosEstadoState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_PASSEIOS_MARCADOS = "694acd59001d3cf05135"

    // --------------------------------------------------
    // CARREGAR PASSEIOS DO USER (CLIENTE OU FORNECEDOR)
    // --------------------------------------------------
    fun loadPasseiosUser() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val userId = AppwriteService.account.get().id
                val estado = _state.value.estadoSelecionado

                // ✅ Lista de queries
                val queries = mutableListOf<String>()

                // 🔑 Passeios do user (cliente OU fornecedor)
                queries.add(
                    Query.or(
                        listOf(
                            Query.equal("fornecedorId", userId),
                            Query.equal("clienteId", userId)
                        )
                    )
                )

                // 🎯 Filtro por estado (exceto "todos")
                if (estado != "todos") {
                    queries.add(Query.equal("estado", estado))
                }

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIOS_MARCADOS,
                    queries = queries
                )

                _state.value = _state.value.copy(
                    passeios = result.documents,
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

    // --------------------------------------------------
// ALTERAR ESTADO DO FILTRO
// --------------------------------------------------
fun alterarEstadoFiltro(novoEstado: String) {
    _state.value = _state.value.copy(estadoSelecionado = novoEstado)
    loadPasseiosUser()
}

// --------------------------------------------------
// ATUALIZAR ESTADO DO PASSEIO
// --------------------------------------------------
fun atualizarEstadoPasseio(passeioId: String, novoEstado: String) {
    viewModelScope.launch {
        try {
            AppwriteService.databases.updateDocument(
                databaseId = DB_ID,
                collectionId = COLLECTION_PASSEIOS_MARCADOS,
                documentId = passeioId,
                data = mapOf("estado" to novoEstado)
            )

            loadPasseiosUser()

        } catch (e: Exception) {
            Log.e("PASSEIOS_ESTADO", "Erro ao atualizar estado", e)
        }
    }
}
}
