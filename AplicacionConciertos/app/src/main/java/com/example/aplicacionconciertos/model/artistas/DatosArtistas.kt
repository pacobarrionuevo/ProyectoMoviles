package com.example.aplicacionconciertos.model.artistas

data class DatosArtistas (
    val id: Int,
    val nombre: String,
    val generosMusicales: List<String>,
    val urlImagen: String,
    val birthYear: Int,
    val precioEntrada: Double,
    val lugarConciertos: List<String>,
    val albumes: List<String>
)