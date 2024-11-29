package com.example.aplicacionconciertos.model.artistas

data class DatosArtistas (
    val id: String,
    val name: String,
    val genres: List<String>,
    val image_url: String,
    val birth_year: String,
    val precio_entrada: String,
    val concert_locations: List<String>,
    val albums: List<String>
)
