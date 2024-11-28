package com.example.aplicacionconciertos.model.artistas

data class DatosArtistas (
    val id: String,
    val nombre: String,
    val generosMusicales: List<String>,
    val urlImagen: String,
    val birthYear: String,
    val precioEntrada: String,
    val lugarConciertos: List<String>,
    val albumes: List<String>
)