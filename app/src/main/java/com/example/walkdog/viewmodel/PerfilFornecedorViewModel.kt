package com.example.walkdog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import com.example.walkdog.service.AppwriteService
import io.appwrite.models.Document

data class PerfilFornecedorUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val fornecedor: Document<Map<String, Any>>? = null,
    val clientes: List<Document<Map<String, Any>>> = emptyList()
)

class PerfilFornecedorViewModel : ViewModel() {

    private val _state = MutableStateFlow(PerfilFornecedorUiState())
    val state: StateFlow<PerfilFornecedorUiState> = _state

    private val DB_ID = "69236f45003447bc5844" // ID da base de dados walkdogDB
    private val COLLECTION_FORNECEDORES = "69236f93001828d82b6f" // ID da coleção fornecedor
    private val COLLECTION_CLIENTES = "69236f5200282814eb3c" // ID da coleção cliente

    fun loadFornecedor(id: String) {
        viewModelScope.launch {
            try {
                _state.value = PerfilFornecedorUiState(loading = true)

                val doc = AppwriteService.databases.getDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDORES,
                    documentId = id
                )

                _state.value = PerfilFornecedorUiState(fornecedor = doc)

            } catch (e: Exception) {
                _state.value = PerfilFornecedorUiState(error = e.message)
            }
        }
    }

    fun loadClientes() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CLIENTES
                )

                _state.value = _state.value.copy(
                    clientes = result.documents,
                    loading = false
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }
}
