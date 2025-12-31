package com.example.walkdog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class EditarCaoState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val data: Map<String, Any>? = null
)

class EditarCaoViewModel : ViewModel() {

    private val _state = MutableStateFlow(EditarCaoState())
    val state: StateFlow<EditarCaoState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_CAO = "692b22060025bfc8cade"

    // --------------------------------------------------
    // CARREGAR DADOS DO CÃO
    // --------------------------------------------------
    fun loadCao(caoId: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val doc = AppwriteService.databases.getDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CAO,
                    documentId = caoId
                )

                _state.value = _state.value.copy(
                    loading = false,
                    data = doc.data
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }

    // --------------------------------------------------
    // SALVAR ALTERAÇÕES (SEM FOTO)
    // --------------------------------------------------
    fun salvarAlteracoes(
        caoId: String,
        nome: String,
        raca: String,
        porte: String,
        peso: String,
        localidade: String
    ) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val data = mapOf(
                    "nome" to nome,
                    "raca" to raca,
                    "porte" to porte,
                    "peso" to peso,
                    "localidade" to localidade
                )

                AppwriteService.databases.updateDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CAO,
                    documentId = caoId,
                    data = data
                )

                _state.value = _state.value.copy(
                    loading = false,
                    success = true
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }
}
