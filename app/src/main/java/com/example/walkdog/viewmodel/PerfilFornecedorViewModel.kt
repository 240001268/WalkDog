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
    // 1) Buscar dados do fornecedor
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
                    loading = false
                )

            } catch (e: Exception) {
                Log.e("PERFIL_FORNECEDOR", "Erro ao obter fornecedor", e)
                _state.value = _state.value.copy(
                    error = e.message,
                    loading = false
                )
            }
        }
    }

    // ---------------------------------------------------------
    // 2) Buscar os passeios selecionados no perfil do fornecedor
    // ---------------------------------------------------------
    fun getPasseiosFornecedor(fornecedorId: String) {
        viewModelScope.launch {

            try {
                _state.value = _state.value.copy(loading = true)

                // 1 — buscar fornecedor
                val fornecedor = AppwriteService.databases.getDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    documentId = fornecedorId
                )

                // lista de IDs selecionados no schema
                val selecionados = (fornecedor.data["passeiosSelecionados"] as? List<*>)
                    ?.map { it.toString() }
                    ?: emptyList()

                // 2 — se não tiver passeios, retorna vazio
                if (selecionados.isEmpty()) {
                    _state.value = _state.value.copy(
                        passeios = emptyList(),
                        loading = false
                    )
                    return@launch
                }

                // 3 — buscar esses passeios na coleção passeio
                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIO,
                    queries = listOf(
                        Query.equal("\$id", selecionados)
                    )
                )

                _state.value = _state.value.copy(
                    passeios = result.documents,
                    loading = false
                )

            } catch (e: Exception) {
                Log.e("PERFIL_FORNECEDOR", "Erro ao carregar passeios", e)
                _state.value = _state.value.copy(
                    error = e.message,
                    loading = false
                )
            }
        }
    }
}
