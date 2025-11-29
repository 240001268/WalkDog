package com.example.walkdog.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.example.walkdog.service.AppwriteService
import io.appwrite.ID
import io.appwrite.models.InputFile
import java.io.File

data class FormularioClienteUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class FormularioClienteViewModel : ViewModel() {

    private val _state = MutableStateFlow(FormularioClienteUiState())
    val state: StateFlow<FormularioClienteUiState> = _state

    private val DB_ID = "69236f45003447bc5844" // ID da base de dados walkdogDB
    private val COLLECTION_CLIENTES = "69236f5200282814eb3c" // ID da coleção cliente
    private val BUCKET_ID = "fotos" // ID do bucket de fotos (ajustar se necessário)

    fun salvarCliente(
        nome: String,
        morada: String,
        codPostal: String,
        localidade: String,
        nif: String,
        email: String,
        password: String,
        numeroCartao: String,
        validade: String,
        cvv: String,
        iban: String,
        fotoUri: Uri? = null
    ) {
        viewModelScope.launch {
            try {
                _state.value = FormularioClienteUiState(loading = true)

                // 1. Criar conta no Appwrite (autenticação)
                val user = AppwriteService.account.create(
                    userId = ID.unique(),
                    email = email,
                    password = password,
                    name = nome
                )

                // 2. Fazer login automaticamente
                AppwriteService.account.createEmailPasswordSession(email, password)

                // 3. Upload da foto (se houver)
                var fotoId: String? = null
                if (fotoUri != null) {
                    try {
                        // Nota: Para upload de foto, seria necessário converter Uri para File
                        // Isso requer Context, então por enquanto vamos deixar opcional
                        // fotoId = uploadFoto(fotoUri)
                    } catch (e: Exception) {
                        // Continuar mesmo se o upload da foto falhar
                        println("Erro ao fazer upload da foto: ${e.message}")
                    }
                }

                // 4. Salvar dados do cliente na base de dados
                val clienteData = mapOf(
                    "nome" to nome,
                    "email" to email,
                    "senha" to password, // NOTA: Em produção, NÃO salvar senha em texto plano!
                    "localizacao" to localidade,
                    "endereco" to "$morada, $codPostal",
                    "numeroCartao" to numeroCartao,
                    "validade" to validade,
                    "cvv" to cvv,
                    "iban" to iban,
                    "fotoId" to (fotoId ?: "")
                )

                AppwriteService.databases.createDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CLIENTES,
                    documentId = ID.unique(),
                    data = clienteData
                )

                _state.value = FormularioClienteUiState(success = true)

            } catch (e: Exception) {
                _state.value = FormularioClienteUiState(
                    error = e.message ?: "Erro ao salvar cliente"
                )
            }
        }
    }

    fun resetState() {
        _state.value = FormularioClienteUiState()
    }
}
