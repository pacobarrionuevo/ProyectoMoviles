package com.example.aplicacionconciertos.model.authentication

data class SignUpResponse(
    val id: String,
    val email: String,
    val role: String
)