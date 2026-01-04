package com.example.walkdog.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.ID
import io.appwrite.Query
import io.appwrite.models.Document
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.OffsetDateTime

// --------------------------------------------------
// STATE
// --------------------------------------------------
data class MarcarPasseioState(

    // 🧑‍💼 Fornecedores
    val fornecedores: List<Document<Map<String, Any>>> = emptyList(),
    val fornecedorSelecionadoId: String? = null,

    // 🐕 Cães
    val caes: List<Document<Map<String, Any>>> = emptyList(),
    val caoSelecionadoId: String? = null,

    // 🚶 Tipos de passeio
    val passeios: List<Document<Map<String, Any>>> = emptyList(),

    // 📋 Snapshot do passeio
    val tipoPasseioSelecionado: String? = null,
    val descricaoSelecionada: String? = null,
    val duracaoSelecionada: String? = null,
    val precoFinal: String? = null,

    // UI
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

// --------------------------------------------------
// VIEWMODEL
// --------------------------------------------------
class MarcarPasseioViewModel : ViewModel() {

    private val _state = MutableStateFlow(MarcarPasseioState())
    val state: StateFlow<MarcarPasseioState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COL_CAES = "692b22060025bfc8cade"
    private val COL_PASSEIOS = "693aeccc002dfd874f6d"
    private val COL_PASSEIOS_MARCADOS = "694acd59001d3cf05135"
    private val COL_FORNECEDORES = "69236f93001828d82b6f"

    // --------------------------------------------------
    // LOADS
    // --------------------------------------------------
    fun loadCaesCliente() {
        viewModelScope.launch {
            val userId = AppwriteService.account.get().id
            val result = AppwriteService.databases.listDocuments(
                DB_ID,
                COL_CAES,
                listOf(Query.equal("userId", userId))
            )
            _state.value = _state.value.copy(caes = result.documents)
        }
    }

    fun loadPasseios() {
        viewModelScope.launch {
            val result = AppwriteService.databases.listDocuments(
                DB_ID,
                COL_PASSEIOS
            )
            _state.value = _state.value.copy(passeios = result.documents)
        }
    }

    fun loadFornecedores() {
        viewModelScope.launch {
            val result = AppwriteService.databases.listDocuments(
                DB_ID,
                COL_FORNECEDORES
            )
            _state.value = _state.value.copy(fornecedores = result.documents)
        }
    }

    // --------------------------------------------------
    // SELEÇÕES
    // --------------------------------------------------
    fun selecionarCao(caoId: String) {
        _state.value = _state.value.copy(caoSelecionadoId = caoId)
    }

    fun selecionarPasseio(passeio: Document<Map<String, Any>>) {
        _state.value = _state.value.copy(
            tipoPasseioSelecionado = passeio.data["TipoPasseio"]?.toString(),
            descricaoSelecionada = passeio.data["descricao"]?.toString(),
            duracaoSelecionada = passeio.data["duracao"]?.toString(),
            precoFinal = passeio.data["preco"]?.toString()
        )
    }

    fun selecionarFornecedor(fornecedorId: String?) {
        _state.value = _state.value.copy(
            fornecedorSelecionadoId = fornecedorId
        )
    }

    // --------------------------------------------------
    // CONFIRMAR PASSEIO
    // --------------------------------------------------
    fun confirmarPasseio(
        localidade: String,
        horaInicio: String
    ) {
        viewModelScope.launch {
            try {
                val s = _state.value

                val caoId = s.caoSelecionadoId
                    ?: throw IllegalStateException("Cão não selecionado")

                val clienteId = AppwriteService.account.get().id
                val dataRegisto = OffsetDateTime.now().toString()

                _state.value = s.copy(loading = true, error = null)

                AppwriteService.databases.createDocument(
                    databaseId = DB_ID,
                    collectionId = COL_PASSEIOS_MARCADOS,
                    documentId = ID.unique(),
                    data = mapOf(
                        "clienteId" to clienteId,
                        "caoId" to caoId,
                        "TipoPasseio" to s.tipoPasseioSelecionado,
                        "descricao" to s.descricaoSelecionada,
                        "duracao" to s.duracaoSelecionada,
                        "preco" to s.precoFinal,
                        "Localidade" to localidade,
                        "HoraInicio" to horaInicio,
                        "data" to dataRegisto,

                        // 🔒 REGRA FINAL
                        "fornecedorId" to s.fornecedorSelecionadoId,
                        "estado" to "pendente"
                    )
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
