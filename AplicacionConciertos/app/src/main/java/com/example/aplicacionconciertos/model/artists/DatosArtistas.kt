package com.example.aplicacionconciertos.model.artists

data class DatosArtistas(
    val id: Int,
    val name: String,
    val genres: List<String>,
    val imageUrl: String,
    val birthYear: Int,
    val precioEntrada: Int, 
    val concertLocations: List<String>,
    val albums: List<String>
)
