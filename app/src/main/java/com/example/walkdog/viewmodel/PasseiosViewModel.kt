package com.example.walkdog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.Query
import io.appwrite.models.Document
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PasseiosUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val passeios: List<Document<Map<String, Any>>> = emptyList()
)

class PasseiosViewModel : ViewModel() {

    private val _state = MutableStateFlow(PasseiosUiState())
    val state: StateFlow<PasseiosUiState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_PASSEIO = "693aeccc002dfd874f6d"

    fun getPasseiosFornecedor(fornecedorId: String) {
        viewModelScope.launch {
            try {
                _state.value = PasseiosUiState(loading = true)

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIO,
                    queries = listOf(
                        Query.equal("fornecedorId", fornecedorId)
                    )
                )

                _state.value = PasseiosUiState(
                    passeios = result.documents,
                    loading = false
                )

            } catch (e: Exception) {
                _state.value = PasseiosUiState(error = e.message)
            }
        }
    }
}
