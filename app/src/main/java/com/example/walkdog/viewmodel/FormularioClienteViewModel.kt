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

                // ------------------------------------------------------------------
                // 1) Criar conta Appwrite
                // ------------------------------------------------------------------
                val conta = AppwriteService.account.create(
                    userId = ID.unique(),
                    email = email,
                    password = password,
                    name = nome
                )

                val clienteId = conta.id  // DocumentId será o mesmo id da auth

                // ------------------------------------------------------------------
                // 2) Upload da foto
                // ------------------------------------------------------------------
                var fotoUrl: String? = null

                if (fotoUri != null) {

                    val bytes = context.contentResolver.openInputStream(fotoUri)!!.readBytes()

                    val upload = AppwriteService.storage.createFile(
                        bucketId = BUCKET_FOTOS_CLIENTE,
                        fileId = ID.unique(),
                        file = InputFile.fromBytes(
                            filename = "foto_${clienteId}.jpg",
                            bytes = bytes
                        )
                    )

                    val projectId = AppwriteService.client.config["project"]
                    fotoUrl =
                        "${AppwriteService.client.endpoint}/storage/buckets/$BUCKET_FOTOS_CLIENTE/files/${upload.id}/view?project=$projectId"
                }

                // ------------------------------------------------------------------
                // 3) Criar documento cliente
                //      → Campos EXATAMENTE como estão na tua Base de Dados
                // ------------------------------------------------------------------
                AppwriteService.databases.createDocument(
                    databaseId = DB_ID,
                    collectionId = COLLECTION_CLIENTE,
                    documentId = clienteId,
                    data = mapOf(
                        "userId" to clienteId,     // ID da conta
                        "nome" to nome,
                        "morada" to morada,
                        "codpostal" to codPostal,
                        "localidade" to localidade,
                        "NIF" to NIF,
                        "email" to email,
                        "senha" to password,          // existe na tua BD
                        "numeroCartao" to numeroCartao,
                        "validade" to validade,
                        "cvv" to cvv,
                        "iban" to iban,
                        "fotoId" to (fotoUrl ?: "")
                    )
                )

                // ------------------------------------------------------------------
                // 4) Sucesso
                // ------------------------------------------------------------------
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
            }
        }
    }

    fun resetState() {
        _state.value = FormularioClienteState()
    }
}
