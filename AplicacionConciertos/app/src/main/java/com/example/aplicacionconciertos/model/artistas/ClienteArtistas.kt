package com.example.aplicacionconciertos.model.artistas

import retrofit2.http.GET

interface ClienteArtistas {
    @GET("6f9f9107-c4ea-445d-b15a-128b5f38d557")
    suspend fun getArtistas(): List<DatosArtistas>
}