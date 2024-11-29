package com.example.aplicacionconciertos.model.artistas

import retrofit2.http.GET

interface ClienteArtistas {
    @GET("25c12924-bcdb-4f2d-9fe5-6e335e1adee4")
    suspend fun getArtistas(): ArtistList
}