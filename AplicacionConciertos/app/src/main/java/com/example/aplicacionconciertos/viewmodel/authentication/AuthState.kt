package com.example.aplicacionconciertos.viewmodel.authentication

sealed class AuthState {
    object Idle : AuthState() // Estado inicial
    object Loading : AuthState() // Cargando
    data class Success(val message: String) : AuthState() // Autenticación exitosa
    data class Error(val errorMessage: String) : AuthState() // Error en la autenticación
    data class Authenticated(val accessToken: String, val refreshToken: String, val email: String) : AuthState() // Usuario autenticado
    object SignedOut : AuthState() // Usuario cerró sesión
}