package com.example.walkdog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AvaliarFornecedorState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,
    val rating: Int = 0,
    val comentario: String = ""
)

class AvaliarFornecedorViewModel : ViewModel() {

    private val _state = MutableStateFlow(AvaliarFornecedorState())
    val state: StateFlow<AvaliarFornecedorState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_AVALIACOES = "6952b649000630f816ab"
    private val COLLECTION_PASSEIOS_MARCADOS = "694acd59001d3cf05135"
    private val COLLECTION_FORNECEDORES = "69236f93001828d82b6f"

    fun setRating(value: Int) {
        _state.value = _state.value.copy(rating = value)
    }

    fun setComentario(value: String) {
        _state.value = _state.value.copy(comentario = value)
    }

    fun enviarAvaliacao(
        passeioId: String,
        fornecedorId: String
    ) {
        viewModelScope.launch {
            try {
                if (_state.value.rating == 0) {
                    _state.value = _state.value.copy(error = "Selecione uma avaliação")
                    return@launch
                }

                _state.value = _state.value.copy(loading = true)

                val clienteId = AppwriteService.account.get().id

                // 1️⃣ Criar avaliação
                AppwriteService.databases.createDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_AVALIACOES,
                    documentId = ID.unique(),
                    data = mapOf(
                        "passeioId" to passeioId,
                        "fornecedorId" to fornecedorId,
                        "clienteId" to clienteId,
                        "rating" to _state.value.rating,
                        "comentario" to _state.value.comentario,
                        //"data" to System.currentTimeMillis()
                    )
                )

                // 2️⃣ Marcar passeio como avaliado
                AppwriteService.databases.updateDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIOS_MARCADOS,
                    documentId = passeioId,
                    data = mapOf("avaliado" to true)
                )

                // 3️⃣ Atualizar rating do fornecedor
                val fornecedorDoc = AppwriteService.databases.getDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDORES,
                    documentId = fornecedorId
                )

                val ratingAtual =
                    fornecedorDoc.data["rating"]?.toString()?.toFloatOrNull() ?: 0f
                val totalAvaliacoes =
                    fornecedorDoc.data["totalAvaliacoes"]?.toString()?.toIntOrNull() ?: 0

                val novoTotal = totalAvaliacoes + 1
                val novoRating =
                    ((ratingAtual * totalAvaliacoes) + _state.value.rating) / novoTotal

                AppwriteService.databases.updateDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDORES,
                    documentId = fornecedorId,
                    data = mapOf(
                        "rating" to novoRating,
                        "totalAvaliacoes" to novoTotal
                    )
                )

                _state.value = _state.value.copy(
                    loading = false,
                    success = true
                )

            } catch (e: Exception) {
                Log.e("AVALIAR_FORNECEDOR", "Erro", e)
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }
}
