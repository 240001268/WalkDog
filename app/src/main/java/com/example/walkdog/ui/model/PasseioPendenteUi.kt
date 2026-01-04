package com.example.walkdog.ui.model

data class PasseioPendenteUi(
    val id: String,
    val nomePasseio: String,
    val nomeCao: String,
    val fotoCaoUrl: String?,
    val localidade: String,
    val hora: String,
    val preco: String,
    val fornecedor: String,
    val estado: String
)