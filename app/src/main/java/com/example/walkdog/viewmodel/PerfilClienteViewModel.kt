package com.example.walkdog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.walkdog.service.AppwriteService
import io.appwrite.models.Document

data class PerfilClienteUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val cliente: Document<Map<String, Any>>? = null,
    val fornecedores: List<Document<Map<String, Any>>> = emptyList()
)

class PerfilClienteViewModel : ViewModel() {

    private val _state = MutableStateFlow(PerfilClienteUiState())
    val state: StateFlow<PerfilClienteUiState> = _state

    private val DB_ID = "69236f45003447bc5844" // ID da base de dados walkdogDB
    private val COLLECTION_CLIENTES = "69236f5200282814eb3c" // ID da coleção cliente
    private val COLLECTION_FORNECEDORES = "69236f93001828d82b6f" // ID da coleção fornecedor

    fun loadCliente(userId: String) {
        viewModelScope.launch {
            try {
                _state.value = PerfilClienteUiState(loading = true)

                val doc = AppwriteService.databases.getDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CLIENTES,
                    documentId = userId
                )

                _state.value = PerfilClienteUiState(cliente = doc)

            } catch (e: Exception) {
                _state.value = PerfilClienteUiState(error = e.message)
            }
        }
    }

    fun loadFornecedores() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDORES
                )

                _state.value = _state.value.copy(
                    fornecedores = result.documents,
                    loading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}
