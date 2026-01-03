package com.example.walkdog.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.walkdog.service.AppwriteService
import io.appwrite.ID
import io.appwrite.models.InputFile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

data class FormularioClienteState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class FormularioClienteViewModel : ViewModel() {

    private val _state = MutableStateFlow(FormularioClienteState())
    val state: StateFlow<FormularioClienteState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_CLIENTE = "69236f5200282814eb3c"
    private val BUCKET_FOTOS_CLIENTE = "693dc47f0030dd8f0250"

    fun salvarCliente(
        context: Context,
        nome: String,
        morada: String,
        codPostal: String,
        localidade: String,
        NIF: String,
        email: String,
        password: String,
        numeroCartao: String,
        validade: String,
        cvv: String,
        iban: String,
        fotoUri: Uri?
    ) {
        viewModelScope.launch {
            try {
                _state.value = FormularioClienteState(loading = true)

                // 1️⃣ Criar conta Auth
                AppwriteService.account.create(
                    userId = ID.unique(),
                    email = email,
                    password = password,
                    name = nome
                )

                // 2️⃣ Login imediato
                AppwriteService.account.createEmailPasswordSession(
                    email = email,
                    password = password
                )

                // 3️⃣ User autenticado
                val user = AppwriteService.account.get()
                val userId = user.id

                // 4️⃣ Upload foto (opcional)
                var fotoId = ""

                if (fotoUri != null) {
                    val resolver = context.contentResolver

                    // 1️⃣ Criar ficheiro temporário (API 24+)
                    val tempFile = File.createTempFile(
                        "upload_",
                        ".jpg",
                        context.cacheDir
                    )

                    resolver.openInputStream(fotoUri)?.use { input ->
                        tempFile.outputStream().use { output ->
                            input.copyTo(output)
                        }
                    } ?: throw Exception("Erro ao abrir imagem")

                    // 2️⃣ Upload para Appwrite (ESTÁVEL)
                    val uploadResult = AppwriteService.storage.createFile(
                        bucketId = BUCKET_FOTOS_CLIENTE,
                        fileId = ID.unique(),
                        file = InputFile.fromFile(tempFile)
                    )

                    fotoId = uploadResult.id

                    // 3️⃣ Limpar ficheiro temporário
                    tempFile.delete()
                }

                // 5️⃣ Criar documento CLIENTE (⚠️ userId INCLUÍDO)
                AppwriteService.databases.createDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CLIENTE,
                    documentId = userId,
                    data = mapOf(
                        "userId" to userId,
                        "nome" to nome,
                        "email" to email,
                        "senha" to password,
                        "localidade" to localidade,
                        "morada" to morada,
                        "numeroCartao" to numeroCartao,
                        "validade" to validade,
                        "iban" to iban,
                        "fotoId" to fotoId,
                        "cvv" to cvv,
                        "codpostal" to codPostal,
                        "NIF" to NIF
                    ),
                    permissions = listOf(
                        io.appwrite.Permission.read(io.appwrite.Role.user(userId)),
                        io.appwrite.Permission.update(io.appwrite.Role.user(userId)),
                        io.appwrite.Permission.delete(io.appwrite.Role.user(userId))
                    )
                )

                AppwriteService.account.deleteSession("current")

                _state.value = FormularioClienteState(
                    loading = false,
                    success = true
                )

            } catch (e: Exception) {
                Log.e("FORM_CLIENTE", "ERRO AO CRIAR CLIENTE", e)
                _state.value = FormularioClienteState(
                    loading = false,
                    error = e.message

                )
                Log.e("FORM_CLIENTE", "ERRO REAL APPWRITE: ${e.message}", e)
            }
        }
    }

    fun resetState() {
        _state.value = FormularioClienteState()
    }
}
