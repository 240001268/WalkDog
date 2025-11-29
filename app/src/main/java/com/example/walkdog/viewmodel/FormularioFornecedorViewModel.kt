package com.example.walkdog.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.walkdog.service.AppwriteService
import io.appwrite.ID

data class FormularioFornecedorUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class FormularioFornecedorViewModel : ViewModel() {

    private val _state = MutableStateFlow(FormularioFornecedorUiState())
    val state: StateFlow<FormularioFornecedorUiState> = _state

    private val DB_ID = "69236f45003447bc5844" // ID da base de dados walkdogDB
    private val COLLECTION_FORNECEDORES = "69236f93001828d82b6f" // ID da coleção fornecedor

    fun salvarFornecedor(
        nome: String,
        morada: String,
        codPostal: String,
        localidade: String,
        nif: String,
        email: String,
        password: String,
        fotoUri: Uri? = null
    ) {
        viewModelScope.launch {
            try {
                _state.value = FormularioFornecedorUiState(loading = true)

                // 1. Criar conta no Appwrite (autenticação)
                val user = AppwriteService.account.create(
                    userId = ID.unique(),
                    email = email,
                    password = password,
                    name = nome
                )

                // 2. Fazer login automaticamente
                AppwriteService.account.createEmailPasswordSession(email, password)

                // 3. Converter NIF para Integer
                val nifInt = nif.toIntOrNull() ?: 0

                // 4. Converter código postal para Integer (remover hífen se houver)
                val codPostalInt = codPostal.replace("-", "").toIntOrNull() ?: 0

                // 5. Salvar dados do fornecedor na base de dados
                val fornecedorData = mapOf(
                    "nome" to nome,
                    "morada" to listOf(morada), // morada é um array de strings
                    "codpostal" to codPostalInt,
                    "localidade" to localidade,
                    "nif" to nifInt,
                    "email" to email,
                    "senha" to password // NOTA: Em produção, NÃO salvar senha em texto plano!
                )

                AppwriteService.databases.createDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDORES,
                    documentId = ID.unique(),
                    data = fornecedorData
                )

                _state.value = FormularioFornecedorUiState(success = true)

            } catch (e: Exception) {
                _state.value = FormularioFornecedorUiState(
                    error = e.message ?: "Erro ao salvar fornecedor"
                )
            }
        }
    }

    fun resetState() {
        _state.value = FormularioFornecedorUiState()
    }
}
