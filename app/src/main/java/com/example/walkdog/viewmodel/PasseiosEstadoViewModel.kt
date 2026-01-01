package com.example.walkdog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import com.example.walkdog.utils.buildFotoCaoUrl
import io.appwrite.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PasseioEstadoUi(
    val id: String,
    val fornecedorId: String,
    val fornecedorNome: String,
    val fornecedorRating: Float,
    val estado: String,
    val avaliado: Boolean,
    val descricao: String,
    val caoNome: String,
    val localidade: String,
    val hora: String,
    val preco: String,
    val fotoCaoUrl: String?
)

data class PasseiosEstadoState(
    val loading: Boolean = false,
    val error: String? = null,
    val passeios: List<PasseioEstadoUi> = emptyList(),
    val estadoSelecionado: String = "todos",
    val userId: String? = null,
    val isFornecedor: Boolean = false
)

class PasseiosEstadoViewModel : ViewModel() {

    private val _state = MutableStateFlow(PasseiosEstadoState())
    val state: StateFlow<PasseiosEstadoState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COL_PASSEIOS = "694acd59001d3cf05135"
    private val COL_FORNECEDORES = "69236f93001828d82b6f"

    fun loadPasseiosUser() {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val user = AppwriteService.account.get()
                val userId = user.id

                val fornecedorDocs = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COL_FORNECEDORES,
                    queries = listOf(Query.equal("userId", userId))
                )

                val isFornecedor = fornecedorDocs.documents.isNotEmpty()

                val queries = mutableListOf(
                    Query.or(
                        listOf(
                            Query.equal("clienteId", userId),
                            Query.equal("fornecedorId", userId)
                        )
                    )
                )

                if (_state.value.estadoSelecionado != "todos") {
                    queries.add(Query.equal("estado", _state.value.estadoSelecionado))
                }

                val passeiosDocs = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COL_PASSEIOS,
                    queries = queries
                )

                val fornecedoresCache = mutableMapOf<String, Pair<String, Float>>()

                val lista = passeiosDocs.documents.map { doc ->

                    val fornecedorId = doc.data["fornecedorId"].toString()

                    val fornecedorInfo = fornecedoresCache.getOrPut(fornecedorId) {
                        val fDoc = AppwriteService.databases.getDocument(
                            databaseId = DB_ID,
                            collectionId = COL_FORNECEDORES,
                            documentId = fornecedorId
                        )
                        Pair(
                            fDoc.data["nome"]?.toString() ?: "Fornecedor",
                            (fDoc.data["rating"] as? Number)?.toFloat() ?: 0f
                        )
                    }

                    PasseioEstadoUi(
                        id = doc.id,
                        fornecedorId = fornecedorId,
                        fornecedorNome = fornecedorInfo.first,
                        fornecedorRating = fornecedorInfo.second,
                        estado = doc.data["estado"]?.toString() ?: "",
                        avaliado = doc.data["avaliado"] as? Boolean ?: false,
                        descricao = doc.data["descricao"]?.toString() ?: "Passeio",
                        caoNome = doc.data["Cao"]?.toString() ?: "-",
                        localidade = doc.data["Localidade"]?.toString() ?: "-",
                        hora = doc.data["HoraInicio"]?.toString()?.take(5) ?: "-",
                        preco = doc.data["preco"]?.toString() ?: "-",
                        fotoCaoUrl = buildFotoCaoUrl(doc.data["fotoId"]?.toString())
                    )
                }

                _state.value = _state.value.copy(
                    loading = false,
                    passeios = lista,
                    userId = userId,
                    isFornecedor = isFornecedor
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }

    fun alterarEstadoFiltro(novoEstado: String) {
        _state.value = _state.value.copy(estadoSelecionado = novoEstado)
        loadPasseiosUser()
    }

    fun atualizarEstadoPasseio(passeioId: String, novoEstado: String) {
        viewModelScope.launch {
            AppwriteService.databases.updateDocument(
                databaseId = DB_ID,
                collectionId = COL_PASSEIOS,
                documentId = passeioId,
                data = mapOf("estado" to novoEstado)
            )
            loadPasseiosUser()
        }
    }
}
