package com.example.walkdog.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.Query
import io.appwrite.models.Document
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


data class PerfilClienteUiState(
    val loading: Boolean = false,
    val cliente: Document<Map<String, Any>>? = null,
    val error: String? = null
)


class PerfilClienteViewModel : ViewModel() {


    private val _state = MutableStateFlow(PerfilClienteUiState())
    val state: StateFlow<PerfilClienteUiState> = _state


    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_CLIENTES = "69236f5200282814eb3c"


    fun loadCliente(userId: String) {
        viewModelScope.launch {
            try {
                _state.value = PerfilClienteUiState(loading = true)


                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CLIENTES,
                    queries = listOf(Query.equal("userId", userId))
                )


                if (result.documents.isEmpty()) {
                    _state.value = PerfilClienteUiState(error = "Cliente não encontrado")
                    return@launch
                }


                _state.value = PerfilClienteUiState(
                    loading = false,
                    cliente = result.documents.first()
                )


            } catch (e: Exception) {
                _state.value = PerfilClienteUiState(error = e.message)
            }
        }
    }
}