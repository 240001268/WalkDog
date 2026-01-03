package com.example.walkdog.ui

data class Cao(
    val nome: String,
    val raca: String,
    val porte: String,
    val peso: String,
    val localidade: String,
    val fotoUrl: String? = null,

    val nomeDono: String,
    val emailDono: String,
    val telefoneDono: String,
    val localidadeDono: String
)