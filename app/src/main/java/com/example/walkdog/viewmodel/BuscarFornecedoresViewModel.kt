package com.example.walkdog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class FornecedorItem(
    val id: String,
    val nome: String,
    val localidade: String,
    val ratingMedio: Double,
    val fotoId: String?
)

data class BuscarFornecedoresUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val fornecedores: List<FornecedorItem> = emptyList()
)

class BuscarFornecedoresViewModel : ViewModel() {

    private val _state = MutableStateFlow(BuscarFornecedoresUiState())
    val state: StateFlow<BuscarFornecedoresUiState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_FORNECEDORES = "69236f93001828d82b6f"

    fun loadFornecedores() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val docs = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDORES
                )

                val lista = docs.documents.map { doc ->
                    FornecedorItem(
                        id = doc.id,
                        nome = doc.data["nome"]?.toString() ?: "Sem nome",
                        localidade = doc.data["localidade"]?.toString() ?: "—",
                        ratingMedio = (doc.data["ratingMedio"] as? Number)?.toDouble() ?: 0.0,
                        fotoId = doc.data["fotoId"]?.toString()
                    )
                }

                _state.value = BuscarFornecedoresUiState(
                    fornecedores = lista,
                    loading = false
                )

            } catch (e: Exception) {
                _state.value = BuscarFornecedoresUiState(
                    error = e.message,
                    loading = false
                )
            }
        }
    }
}
