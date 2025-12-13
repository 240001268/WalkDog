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

data class EscolherPasseiosState(
    val loading: Boolean = false,
    val error: String? = null,
    val todosPasseios: List<Document<Map<String, Any>>> = emptyList(),
    val selecionados: List<String> = emptyList(),
    val success: Boolean = false
)

class EscolherPasseiosViewModel : ViewModel() {

    private val _state = MutableStateFlow(EscolherPasseiosState())
    val state: StateFlow<EscolherPasseiosState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_PASSEIO = "693aeccc002dfd874f6d"
    private val COLLECTION_FORNECEDOR = "69236f93001828d82b6f"

    // ----------------------------------------------------------
    // 1) CARREGAR TODOS OS PASSEIOS
    // ----------------------------------------------------------
    fun loadPasseios(fornecedorId: String) {
        viewModelScope.launch {
            try {
                _state.value = EscolherPasseiosState(loading = true)

                val todos = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIO
                )

                // carregar seleção anterior do fornecedor
                val fornecedor = AppwriteService.databases.getDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    documentId = fornecedorId
                )

                val selecionados = (fornecedor.data["passeiosSelecionados"] as? List<*>)?.map { it.toString() }
                    ?: emptyList()

                _state.value = EscolherPasseiosState(
                    todosPasseios = todos.documents,
                    selecionados = selecionados
                )

            } catch (e: Exception) {
                _state.value = EscolherPasseiosState(error = e.message)
                Log.e("ESCOLHER_PASSEIOS", "Erro carregando passeios", e)
            }
        }
    }

    // ----------------------------------------------------------
    // 2) ALTERAR SELEÇÃO LOCAL
    // ----------------------------------------------------------
    fun togglePasseio(passeioId: String) {
        val current = _state.value.selecionados.toMutableList()

        if (current.contains(passeioId))
            current.remove(passeioId)
        else
            current.add(passeioId)

        _state.value = _state.value.copy(selecionados = current)
    }

    // ----------------------------------------------------------
    // 3) GUARDAR SELEÇÃO NO APPWRITE
    // ----------------------------------------------------------
    fun salvar(fornecedorId: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                AppwriteService.databases.updateDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    documentId = fornecedorId,
                    data = mapOf(
                        "passeiosSelecionados" to _state.value.selecionados
                    )
                )

                _state.value = _state.value.copy(success = true, loading = false)

            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message, loading = false)
                Log.e("ESCOLHER_PASSEIOS", "Erro salvando seleção", e)
            }
        }
    }
}
