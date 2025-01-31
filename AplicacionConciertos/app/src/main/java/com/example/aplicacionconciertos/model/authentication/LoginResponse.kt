package com.example.aplicacionconciertos.model.authentication

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)