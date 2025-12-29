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

data class PasseiosMarcadosState(
    val loading: Boolean = false,
    val error: String? = null,
    val passeios: List<Document<Map<String, Any>>> = emptyList()
)

class PasseiosMarcadosFornecedorViewModel : ViewModel() {

    private val _state = MutableStateFlow(PasseiosMarcadosState())
    val state: StateFlow<PasseiosMarcadosState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_PASSEIOS_MARCADOS = "694acd59001d3cf05135"

    // ---------------------------------------------------------
    // Buscar pedidos de um tipo de passeio (pendentes)
    // ---------------------------------------------------------
    fun getPasseiosMarcadosPorTipo(tipoPasseio: String) {
        viewModelScope.launch {
            try {
                _state.value = PasseiosMarcadosState(loading = true)

                val fornecedor = AppwriteService.account.get()

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIOS_MARCADOS,
                    queries = listOf(
                        Query.equal("fornecedorId", fornecedor.id),
                        Query.equal("tipoPasseio", tipoPasseio),
                        Query.equal("estado", "pendente")
                    )
                )

                _state.value = PasseiosMarcadosState(
                    passeios = result.documents
                )

            } catch (e: Exception) {
                Log.e("PASSEIOS_MARCADOS", "Erro ao buscar passeios", e)
                _state.value = PasseiosMarcadosState(
                    error = e.message
                )
            }
        }
    }

    // ---------------------------------------------------------
    // Aceitar passeio
    // ---------------------------------------------------------
    fun aceitarPasseio(passeioMarcadoId: String) {
        viewModelScope.launch {
            try {
                AppwriteService.databases.updateDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIOS_MARCADOS,
                    documentId = passeioMarcadoId,
                    data = mapOf(
                        "estado" to "aceite"
                    )
                )

                // Atualiza lista removendo o aceite
                _state.value = _state.value.copy(
                    passeios = _state.value.passeios.filterNot {
                        it.id == passeioMarcadoId
                    }
                )

            } catch (e: Exception) {
                Log.e("PASSEIO_ACEITAR", "Erro ao aceitar passeio", e)
            }
        }
    }
}
