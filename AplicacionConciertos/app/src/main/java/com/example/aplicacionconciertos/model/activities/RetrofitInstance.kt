package com.example.aplicacionconciertos.model.activities

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "http://10.0.2.2:8080"

    val api: ActivitiesClient by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ActivitiesClient::class.java)
    }
}
