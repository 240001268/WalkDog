package com.example.walkdog.utils

import com.example.walkdog.service.AppwriteService

fun buildFotoCaoUrl(fotoId: String?): String? {
    if (fotoId.isNullOrBlank()) return null

    val bucketId = "6930dbe0002154093a9f" // fotosCao
    val projectId = AppwriteService.client.config["project"]
    val endpoint = AppwriteService.client.endpoint

    return "$endpoint/storage/buckets/$bucketId/files/$fotoId/view?project=$projectId"
}