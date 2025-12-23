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
    // 1️⃣ CARREGAR PASSEIOS + FORNECEDOR (POR userId)
    // ----------------------------------------------------------
    fun loadPasseios() {
        viewModelScope.launch {
            try {
                _state.value = EscolherPasseiosState(loading = true)

                // 🔹 user autenticado
                val user = AppwriteService.account.get()

                // 🔹 buscar fornecedor pelo userId
                val fornecedorResult = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    queries = listOf(
                        Query.equal("userId", user.id)
                    )
                )

                if (fornecedorResult.documents.isEmpty()) {
                    _state.value = EscolherPasseiosState(
                        error = "Fornecedor não encontrado"
                    )
                    return@launch
                }

                val fornecedor = fornecedorResult.documents.first()

                // 🔹 carregar passeios
                val todos = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIO
                )

                val selecionados =
                    (fornecedor.data["passeiosSelecionados"] as? List<*>)
                        ?.map { it.toString() }
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
    // 2️⃣ TOGGLE LOCAL
    // ----------------------------------------------------------
    fun togglePasseio(passeioId: String) {
        val current = _state.value.selecionados.toMutableList()
        if (current.contains(passeioId)) current.remove(passeioId)
        else current.add(passeioId)

        _state.value = _state.value.copy(selecionados = current)
    }

    // ----------------------------------------------------------
    // 3️⃣ SALVAR NO DOCUMENTO CORRETO
    // ----------------------------------------------------------
    fun salvar() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true, error = null)

                val user = AppwriteService.account.get()

                val fornecedorResult = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    queries = listOf(
                        Query.equal("userId", user.id)
                    )
                )

                if (fornecedorResult.documents.isEmpty()) {
                    _state.value = _state.value.copy(
                        loading = false,
                        error = "Fornecedor não encontrado"
                    )
                    return@launch
                }

                val fornecedor = fornecedorResult.documents.first()

                AppwriteService.databases.updateDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    documentId = fornecedor.id,
                    data = mapOf(
                        "passeiosSelecionados" to _state.value.selecionados
                    )
                )

                _state.value = _state.value.copy(
                    success = true,
                    loading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Erro ao guardar passeios",
                    loading = false
                )
                Log.e("ESCOLHER_PASSEIOS", "Erro salvando seleção", e)
            }
        }


    }

}