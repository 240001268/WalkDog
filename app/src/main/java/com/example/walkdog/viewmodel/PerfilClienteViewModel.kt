package com.example.walkdog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.models.Document
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import io.appwrite.Query


data class PerfilClienteState(
    val loading: Boolean = false,
    val cliente: Document<Map<String, Any>>? = null,
    val caes: List<Document<Map<String, Any>>> = emptyList(),
    val error: String? = null
)

class PerfilClienteViewModel : ViewModel() {


    private val _state = MutableStateFlow(PerfilClienteState())
    val state: StateFlow<PerfilClienteState> = _state


    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_CLIENTE = "69236f5200282814eb3c"

    private val COLLECTION_CAO = "692b22060025bfc8cade"


    fun getClienteData() {
        viewModelScope.launch {
            try {
                _state.value = PerfilClienteState(loading = true)

                val user = AppwriteService.account.get()

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CLIENTE,
                    queries = listOf(
                        Query.equal("userId", user.id)
                    )
                )

                if (result.documents.isNotEmpty()) {

                    val cliente = result.documents[0]

                    _state.value = PerfilClienteState(
                        cliente = cliente,
                        loading = false
                    )

                    // 🔹 carregar cães do cliente
                    loadCaesCliente(user.id)

                } else {
                    _state.value = PerfilClienteState(
                        error = "Cliente não encontrado",
                        loading = false
                    )
                }

            } catch (e: Exception) {
                Log.e("PERFIL_CLIENTE", "Erro ao obter cliente", e)
                _state.value = _state.value.copy(
                    error = e.message,
                    loading = false
                )
            }
        }
    }

private fun loadCaesCliente(userId: String) {
    viewModelScope.launch {
        try {
            val result = AppwriteService.databases.listDocuments(
                databaseId = DB_ID,
                collectionId = COLLECTION_CAO,
                queries = listOf(
                    Query.equal("userId", userId)
                )
            )

            _state.value = _state.value.copy(
                caes = result.documents
            )

        } catch (e: Exception) {
            Log.e("PERFIL_CLIENTE", "Erro ao obter cães", e)
        }
    }
}
}