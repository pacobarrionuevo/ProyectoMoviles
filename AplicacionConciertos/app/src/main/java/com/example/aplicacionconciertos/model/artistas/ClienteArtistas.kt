package com.example.aplicacionconciertos.model.artistas

import retrofit2.http.GET

interface ClienteArtistas {
    @GET("31fc66ea-478b-48be-965b-a3f5ccbbf418")
    suspend fun getArtistas(): List<DatosArtistas>
}