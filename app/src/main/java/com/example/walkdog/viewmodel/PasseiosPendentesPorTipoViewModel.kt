package com.example.walkdog.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import com.example.walkdog.ui.model.PasseioPendenteUi
import com.example.walkdog.utils.buildFotoCaoUrl
import io.appwrite.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



// --------------------------------------------------
// STATE
// --------------------------------------------------
data class PasseiosPendentesPorTipoState(
    val loading: Boolean = false,
    val error: String? = null,
    val passeios: List<PasseioPendenteUi> = emptyList(),
    val aceiteComSucesso: Boolean = false
)

// --------------------------------------------------
// VIEWMODEL
// --------------------------------------------------
class PasseiosPendentesPorTipoViewModel : ViewModel() {

    private val _state = MutableStateFlow(PasseiosPendentesPorTipoState())
    val state: StateFlow<PasseiosPendentesPorTipoState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COL_PASSEIOS_MARCADOS = "694acd59001d3cf05135"

    private val COL_PASSEIOS = "693aeccc002dfd874f6d"

    private val COL_CAES = "692b22060025bfc8cade"

    // --------------------------------------------------
    // 🔍 CARREGAR PASSEIOS PENDENTES
    // --------------------------------------------------
    fun loadPasseiosPendentes(tipopasseio: String) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true, error = null)

                val marcados = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COL_PASSEIOS_MARCADOS,
                    queries = listOf(
                        Query.equal("TipoPasseio", tipopasseio),
                        Query.equal("estado", "pendente"),
                        Query.isNull("fornecedorId")
                    )
                )

                val passeiosUi = marcados.documents.map { marcado ->

                    val caoId = marcado.data["caoId"]?.toString()

                    // 🛡️ Proteção para passeios antigos
                    if (caoId.isNullOrBlank()) {
                        return@map PasseioPendenteUi(
                            id = marcado.id,
                            nomePasseio = marcado.data["descricao"]?.toString() ?: "Passeio",
                            nomeCao = "—",
                            fotoCaoUrl = null,
                            localidade = marcado.data["Localidade"]?.toString() ?: "—",
                            hora = marcado.data["HoraInicio"]?.toString() ?: "—",
                            preco = marcado.data["preco"]?.toString() ?: "—",
                            fornecedor = "Aguarda fornecedor",
                            estado = marcado.data["estado"]?.toString() ?: "Pendente"
                        )
                    }

                    // 🔗 JOIN com a collection `cao`
                    val caoDoc = AppwriteService.databases.getDocument(
                        databaseId = DB_ID,
                        collectionId = COL_CAES,
                        documentId = caoId
                    )

                    PasseioPendenteUi(
                        id = marcado.id,
                        nomePasseio = marcado.data["descricao"]?.toString() ?: "Passeio",
                        nomeCao = caoDoc.data["nome"]?.toString() ?: "—",
                        fotoCaoUrl = caoDoc.data["fotoId"]?.toString()
                            ?.takeIf { it.isNotBlank() }
                            ?.let { buildFotoCaoUrl(it) },
                        localidade = marcado.data["Localidade"]?.toString() ?: "—",
                        hora = marcado.data["HoraInicio"]?.toString() ?: "—",
                        preco = marcado.data["preco"]?.toString() ?: "—",
                        fornecedor = "Aguarda fornecedor",
                        estado = marcado.data["estado"]?.toString() ?: "Pendente"
                    )
                }

                _state.value = _state.value.copy(
                    loading = false,
                    passeios = passeiosUi
                )

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }
    fun resetAceiteFlag() {
        _state.value = _state.value.copy(aceiteComSucesso = false)
    }

    fun aceitarPasseio(passeioMarcadoId: String) {
        viewModelScope.launch {
            try {
                val fornecedorId = AppwriteService.account.get().id

                AppwriteService.databases.updateDocument(
                    databaseId = DB_ID,
                    collectionId = COL_PASSEIOS_MARCADOS,
                    documentId = passeioMarcadoId,
                    data = mapOf(
                        "estado" to "aceite",
                        "fornecedorId" to fornecedorId
                    )
                )

                _state.value = _state.value.copy(aceiteComSucesso = true)

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    error = e.message ?: "Erro ao aceitar passeio"
                )
            }
        }
    }

}
