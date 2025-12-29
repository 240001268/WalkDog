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
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null,

    // 🐕 Dados do cliente
    val caes: List<Document<Map<String, Any>>> = emptyList(),
    val caoSelecionadoId: String? = null,

    // 🚶 Passeios disponíveis
    val passeios: List<Document<Map<String, Any>>> = emptyList(),
    val passeioSelecionadoId: String? = null,

    // 📋 Passeio selecionado
    val descricaoSelecionada: String = "",
    val duracaoSelecionada: String = "",
    val precoFinal: String = "",

    // 👤 Fornecedores
    val fornecedores: List<Document<Map<String, Any>>> = emptyList(),
    val fornecedorSelecionadoId: String? = null
)

// --------------------------------------------------
// VIEWMODEL
// --------------------------------------------------
class MarcarPasseioViewModel : ViewModel() {

    private val _state = MutableStateFlow(MarcarPasseioState())
    val state: StateFlow<MarcarPasseioState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_CAO = "692b22060025bfc8cade"
    private val COLLECTION_PASSEIO = "693aeccc002dfd874f6d"
    private val COLLECTION_PASSEIOS_MARCADOS = "694acd59001d3cf05135"
    private val COLLECTION_FORNECEDOR = "69236f93001828d82b6f"

    // --------------------------------------------------
    // 1️⃣ CARREGAR CÃES DO CLIENTE
    // --------------------------------------------------
    fun loadCaesCliente() {
        viewModelScope.launch {
            try {
                val authUserId = AppwriteService.account.get().id

                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CAO,
                    queries = listOf(Query.equal("userId", authUserId))
                )

                _state.value = _state.value.copy(caes = result.documents)

            } catch (e: Exception) {
                Log.e("MARCAR_PASSEIO", "Erro ao carregar cães", e)
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    // --------------------------------------------------
    // 2️⃣ CARREGAR PASSEIOS
    // --------------------------------------------------
    fun loadPasseios() {
        viewModelScope.launch {
            try {
                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIO
                )

                _state.value = _state.value.copy(passeios = result.documents)

            } catch (e: Exception) {
                Log.e("MARCAR_PASSEIO", "Erro ao carregar passeios", e)
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    // --------------------------------------------------
    // 3️⃣ CARREGAR FORNECEDORES
    // --------------------------------------------------
    fun loadFornecedores() {
        viewModelScope.launch {
            try {
                val result = AppwriteService.databases.listDocuments(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR
                )

                _state.value = _state.value.copy(fornecedores = result.documents)

            } catch (e: Exception) {
                Log.e("MARCAR_PASSEIO", "Erro ao carregar fornecedores", e)
                _state.value = _state.value.copy(error = e.message)
            }
        }
    }

    // --------------------------------------------------
    // 4️⃣ SELEÇÕES
    // --------------------------------------------------
    fun selecionarCao(caoId: String) {
        _state.value = _state.value.copy(caoSelecionadoId = caoId)
    }

    fun selecionarPasseio(passeio: Document<Map<String, Any>>) {
        _state.value = _state.value.copy(
            passeioSelecionadoId = passeio.id,
            descricaoSelecionada = passeio.data["descricao"]?.toString() ?: "",
            duracaoSelecionada = passeio.data["duracao"]?.toString() ?: "",
            precoFinal = passeio.data["preco"]?.toString() ?: ""
        )
    }

    fun selecionarFornecedor(fornecedorId: String) {
        _state.value = _state.value.copy(fornecedorSelecionadoId = fornecedorId)
    }

    // --------------------------------------------------
    // 5️⃣ CONFIRMAR PASSEIO
    // --------------------------------------------------
    fun confirmarPasseio(
        caoNome: String,
        localidade: String,
        horaInicio: String
    ) {
        viewModelScope.launch {
            try {
                _state.value = _state.value.copy(loading = true)

                val clienteId = AppwriteService.account.get().id
                val fornecedorId = state.value.fornecedorSelecionadoId

                if (fornecedorId.isNullOrBlank()) {
                    _state.value = _state.value.copy(
                        loading = false,
                        error = "Selecione um fornecedor"
                    )
                    return@launch
                }

                AppwriteService.databases.createDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_PASSEIOS_MARCADOS,
                    documentId = ID.unique(),
                    data = mapOf(
                        "passeioID" to ID.unique(),
                        "clienteId" to clienteId,
                        "fornecedorId" to fornecedorId,
                        "Cao" to caoNome,
                        "Localidade" to localidade,
                        "HoraInicio" to horaInicio,
                        "data" to java.time.OffsetDateTime.now().toString(),
                        "descricao" to state.value.descricaoSelecionada,
                        "tipoPasseio" to state.value.descricaoSelecionada,
                        "duracao" to state.value.duracaoSelecionada,
                        "preco" to state.value.precoFinal,
                        "estado" to "pendente"
                    )
                )

                _state.value = _state.value.copy(loading = false, success = true)

            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }
}
