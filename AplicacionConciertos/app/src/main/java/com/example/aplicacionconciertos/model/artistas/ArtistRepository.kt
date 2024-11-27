package com.example.aplicacionconciertos.model.artistas

class ArtistRepository {
    private val artistaService = RetroFitInstance.artistasApi

    suspend fun getArtistsRepository(): List<DatosArtistas> {
        return artistaService.getArtistas()
    }
}