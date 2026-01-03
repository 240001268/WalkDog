package com.example.walkdog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.ID
import io.appwrite.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AvaliarFornecedorState(
    val rating: Int = 0,
    val comentario: String = "",
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class AvaliarFornecedorViewModel : ViewModel() {

    private val _state = MutableStateFlow(AvaliarFornecedorState())
    val state: StateFlow<AvaliarFornecedorState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_AVALIACOES = "6952b649000630f816ab"
    private val COLLECTION_FORNECEDOR = "69236f93001828d82b6f"

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
                        "avaliado" to true
                    )
                )

                // 2️⃣ Recalcular média
                val ratingMedio = calcularRatingMedio(fornecedorId)

                // 3️⃣ Atualizar fornecedor
                AppwriteService.databases.updateDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    documentId = fornecedorId,
                    data = mapOf(
                        "ratingMedio" to ratingMedio
                    )
                )

                // 4️⃣ Limpar estado (limpa o screen)
                _state.value = AvaliarFornecedorState(success = true)

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }

    // 🔢 Cálculo do rating médio
    private suspend fun calcularRatingMedio(fornecedorId: String): Double {

        val result = AppwriteService.databases.listDocuments(
            databaseId = DB_ID,
            collectionId = COLLECTION_AVALIACOES,
            queries = listOf(
                Query.equal("fornecedorId", fornecedorId)
            )
        )

        val ratings = result.documents
            .mapNotNull { it.data["rating"] as? Number }
            .map { it.toDouble() }

        return if (ratings.isNotEmpty()) {
            ratings.average()
        } else {
            0.0
        }
    }
}
