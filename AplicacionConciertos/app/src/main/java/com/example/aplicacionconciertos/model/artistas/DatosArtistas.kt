package com.example.aplicacionconciertos.model.artistas

data class DatosArtistas (
    val id: Int,
    val name: String,
    val genres: List<String>,
    val image_url: String,
    val birth_year: Int,
    val precio_entrada: Int,
    val concert_locations: List<String>,
    val albums: List<String>
)
