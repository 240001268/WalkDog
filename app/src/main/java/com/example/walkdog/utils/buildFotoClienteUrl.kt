package com.example.walkdog.utils

import com.example.walkdog.service.AppwriteService

fun buildFotoClienteUrl(fotoId: String?): String? {
    if (fotoId.isNullOrBlank()) return null

    val bucketId = "693dc47f0030dd8f0250"
    val projectId = AppwriteService.client.config["project"]
    val endpoint = AppwriteService.client.endpoint

    return "$endpoint/storage/buckets/$bucketId/files/$fotoId/view?project=$projectId"
}