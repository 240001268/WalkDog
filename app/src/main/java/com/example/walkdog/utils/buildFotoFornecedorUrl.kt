package com.example.walkdog.utils

import com.example.walkdog.service.AppwriteService

fun buildFotoFornecedorUrl(fotoId: String?): String? {
    if (fotoId.isNullOrBlank()) return null

    val bucketId = "692ac9e20009e3efed1c" // ✅ fotosFornecedor
    val projectId = AppwriteService.client.config["project"]
    val endpoint = AppwriteService.client.endpoint

    return "$endpoint/storage/buckets/$bucketId/files/$fotoId/view?project=$projectId"
}