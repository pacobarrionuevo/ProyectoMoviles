package com.example.aplicacionconciertos.model.artistas

class ArtistRepository {
    private val artistaService = RetroFitInstance.artistasApiService

    suspend fun getArtistRepository(): List<DatosArtistas> {
        return artistaService.getArtistas()
    }
}