package com.example.walkdog.utils

import com.example.walkdog.service.AppwriteService

fun buildFotoFornecedorUrl(fotoId: String?): String? {
    if (fotoId.isNullOrBlank()) return null

    val bucketId = "692ac9e20009e3efed1c"
    val endpoint = AppwriteService.ENDPOINT
    val projectId = AppwriteService.PROJECT_ID

    return "$endpoint/storage/buckets/$bucketId/files/$fotoId/view?project=$projectId"
}