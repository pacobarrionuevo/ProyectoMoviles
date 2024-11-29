package com.example.aplicacionconciertos.model.artistas

class ArtistRepository {
    private val artistaService = RetroFitInstance.artistasApiService

    suspend fun getArtistRepository(): List<DatosArtistas> {
        val response = artistaService.getArtistas()
        return response.artists
    }
}