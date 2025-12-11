package com.example.walkdog.viewmodel

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.ID
import io.appwrite.models.InputFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

data class FormularioFornecedorUiState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class FormularioFornecedorViewModel : ViewModel() {

    private val _state = MutableStateFlow(FormularioFornecedorUiState())
    val state: StateFlow<FormularioFornecedorUiState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_FORNECEDORES = "69236f93001828d82b6f"
    private val BUCKET_ID = "default"

    fun salvarFornecedor(
        context: Context,
        nome: String,
        morada: String,
        codPostal: String,
        localidade: String,
        nif: String,
        email: String,
        password: String,
        iban: String,
        fotoUri: Uri?
    ) {
        viewModelScope.launch {

            try {
                _state.value = FormularioFornecedorUiState(loading = true)

                // --------------------------------------------------------------------
                // 1. Upload da foto do fornecedor para o Storage (opcional)
                // --------------------------------------------------------------------
                var fotoId = ""

                if (fotoUri != null) {
                    val inputStream = context.contentResolver.openInputStream(fotoUri)
                    val tempFile = File.createTempFile("foto_", ".jpg", context.cacheDir)

                    inputStream.use { inp ->
                        tempFile.outputStream().use { out ->
                            inp?.copyTo(out)
                        }
                    }

                    val fileUploaded = AppwriteService.storage.createFile(
                        bucketId = BUCKET_ID,
                        fileId = ID.unique(),
                        file = InputFile.fromFile(tempFile)
                    )

                    fotoId = fileUploaded.id
                }

                // --------------------------------------------------------------------
                // 2. Criar conta Appwrite
                // --------------------------------------------------------------------
                val user = AppwriteService.account.create(
                    userId = ID.unique(),
                    email = email,
                    password = password,
                    name = nome
                )

                // --------------------------------------------------------------------
                // 3. Login automático
                // --------------------------------------------------------------------
                AppwriteService.account.createEmailPasswordSession(email, password)

                // --------------------------------------------------------------------
                // 4. Conversões necessárias
                // --------------------------------------------------------------------
                val nifInt = nif.toIntOrNull() ?: 0
                val codPostalInt = codPostal.replace("-", "").toIntOrNull() ?: 0

                // Id interno do fornecedor (vai para fornecID)
                val fornecId = user.id

                // --------------------------------------------------------------------
                // 5. Criar documento na DB
                // --------------------------------------------------------------------
                val fornecedorData = mapOf(
                    "nome" to nome,
                    "morada" to listOf(morada),
                    "codpostal" to codPostalInt,
                    "localidade" to localidade,
                    "nif" to nifInt,
                    "email" to email,
                    "senha" to password,
                    "IBAN" to iban,
                    "FotoID" to fotoId,
                    "fornecID" to fornecId
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
                    loading = false,
                    success = false,
                    error = e.message ?: "Erro ao salvar fornecedor"
                )
            }
        }
    }

    fun resetState() {
        _state.value = FormularioFornecedorUiState()
    }
}
