package com.example.walkdog.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.ID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File
import kotlinx.coroutines.delay

/* ---------- STATE ---------- */

data class FormularioFornecedorState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

/* ---------- VIEWMODEL ---------- */

class FormularioFornecedorViewModel : ViewModel() {

    private val _state = MutableStateFlow(FormularioFornecedorState())
    val state: StateFlow<FormularioFornecedorState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_FORNECEDOR = "69236f93001828d82b6f"
    private val BUCKET_FOTOS = "692ac9e20009e3efed1c"

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
                _state.value = FormularioFornecedorState(loading = true)

                // 1️⃣ Criar conta Auth
                AppwriteService.account.create(
                    userId = ID.unique(),
                    email = email,
                    password = password,
                    name = nome
                )

                // 2️⃣ Login
                AppwriteService.account.createEmailPasswordSession(
                    email = email,
                    password = password
                )

                delay(600)

                val userId = AppwriteService.account.get().id

                // 3️⃣ Upload da foto
                var fotoId = ""

                if (fotoUri != null) {

                    val tempFile = File.createTempFile(
                        "fornecedor_",
                        ".jpg",
                        context.cacheDir
                    )

                    context.contentResolver.openInputStream(fotoUri)?.use { input ->
                        tempFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    } ?: throw Exception("Não foi possível abrir a imagem")

                    if (tempFile.length() == 0L) {
                        tempFile.delete()
                        throw Exception("Imagem inválida ou vazia")
                    }

                    val upload = AppwriteService.storage.createFile(
                        bucketId = BUCKET_FOTOS,
                        fileId = ID.unique(),
                        file = io.appwrite.models.InputFile.fromFile(tempFile)
                    )

                    fotoId = upload.id
                    tempFile.delete()
                }

                // 4️⃣ Criar documento
                AppwriteService.databases.createDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    documentId = userId,
                    data = mapOf(
                        "userId" to userId,
                        "nome" to nome,
                        "email" to email,
                        "senha" to password,
                        "fotoId" to fotoId
                    )
                )

                // 5️⃣ Logout
                AppwriteService.account.deleteSession("current")

                _state.value = FormularioFornecedorState(success = true)

            } catch (e: Exception) {
                Log.e("FORM_FORNECEDOR", "ERRO REAL", e)
                _state.value = FormularioFornecedorState(
                    loading = false,
                    error = e.message
                )
            }
        }
    }
}
