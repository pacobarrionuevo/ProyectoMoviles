package com.example.aplicacionconciertos.model.artistas

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetroFitInstance {
    private const val BASE_URL = "https://run.mocky.io/v3/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val artistasApi: ClienteArtistas by lazy {
        retrofit.create(ClienteArtistas::class.java)
    }

}