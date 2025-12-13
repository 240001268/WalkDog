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

data class FormularioFornecedorState(
    val loading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)

class FormularioFornecedorViewModel : ViewModel() {

    private val _state = MutableStateFlow(FormularioFornecedorState())
    val state: StateFlow<FormularioFornecedorState> = _state

    private val DB_ID = "69236f45003447bc5844"
    private val COLLECTION_FORNECEDOR = "69236f93001828d82b6f"
    private val BUCKET_FOTOS = "fornecedorFotos" // certifique-se que existe no Appwrite

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

                // ----------------------------------------------------------
                // 1) CRIAR CONTA AUTENTICADA
                // ----------------------------------------------------------
                val conta = AppwriteService.account.create(
                    userId = ID.unique(),
                    email = email,
                    password = password,
                    name = nome
                )

                val fornecedorId = conta.id  // Document ID será igual ao ID da Auth

                // ----------------------------------------------------------
                // 2) UPLOAD DA FOTO (se existir)
                // ----------------------------------------------------------
                var fotoUrl: String? = null

                if (fotoUri != null) {

                    val bytes = context.contentResolver.openInputStream(fotoUri)!!.readBytes()

                    val upload = AppwriteService.storage.createFile(
                        bucketId = BUCKET_FOTOS,
                        fileId = ID.unique(),
                        file = InputFile.fromBytes(
                            filename = "foto_${fornecedorId}.jpg",
                            bytes = bytes
                        )
                    )

                    // Gerar URL pública correta
                    val projectId = AppwriteService.client.config["project"]
                    fotoUrl =
                        "${AppwriteService.client.endpoint}/storage/buckets/$BUCKET_FOTOS/files/${upload.id}/view?project=$projectId"
                }

                // ----------------------------------------------------------
                // 3) CRIAR DOCUMENTO FORNECEDOR (CAMPOS CONFORME O APPWRITE)
                // ----------------------------------------------------------
                AppwriteService.databases.createDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_FORNECEDOR,
                    documentId = fornecedorId,
                    data = mapOf(
                        "nome" to nome,
                        "morada" to listOf(morada),         // String[]
                        "codpostal" to codPostal.toInt(),   // Integer
                        "localidade" to localidade,
                        "nif" to nif.toInt(),               // Integer
                        "email" to email,
                        "senha" to password,                // STRING REQUIRED
                        "IBAN" to iban,
                        "FotoID" to (fotoUrl ?: ""),
                        "fornecedorId" to fornecedorId
                    )
                )

                Log.d("FORM_FORNECEDOR", "Documento criado com ID = $fornecedorId")

                // ----------------------------------------------------------
                // 4) SUCESSO
                // ----------------------------------------------------------
                _state.value = FormularioFornecedorState(
                    success = true,
                    loading = false
                )

            } catch (e: Exception) {

                Log.e("FORM_FORNECEDOR", "ERRO AO CRIAR FORNECEDOR", e)

                _state.value = FormularioFornecedorState(
                    error = e.message,
                    loading = false
                )
            }
        }
    }
}
