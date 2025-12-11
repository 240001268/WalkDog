package com.example.walkdog.service

import io.appwrite.ID
import io.appwrite.services.Databases


class FornecedorService {

    private val databases: Databases = AppwriteService.databases

    private val databaseId = "appwrite-69236f45003447bc5844"
    private val collectionId = "69236f93001828d82b6f"

    suspend fun registarFornecedor(
        nome: String,
        morada: List<String>,
        codPostal: Int,
        localidade: String,
        nif: Int,
        email: String,
        senha: String,
        iban: String,
        fotoId: String,
        fornecId: String
    ): Boolean {
        return try {

            databases.createDocument(
                databaseId,
                collectionId,
                ID.unique(),
                mapOf(
                    "nome" to nome,
                    "morada" to morada,
                    "codpostal" to codPostal,
                    "localidade" to localidade,
                    "nif" to nif,
                    "email" to email,
                    "senha" to senha,
                    "IBAN" to iban,
                    "FotoID" to fotoId,
                    "fornecID" to fornecId
                )
            )

            true

        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
