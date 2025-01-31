package com.example.aplicacionconciertos.model.authentication


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthClient {

    // Endpoint para iniciar sesión
    @POST("/api/auth")
    fun login(@Body authRequest: AuthRequest): Call<LoginResponse>

    // Endpoint para registrar un nuevo usuario
    @POST("/api/users")
    fun signUp(@Body authRequest: AuthRequest): Call<SignUpResponse>

    // Endpoint para refrescar el token de acceso
    @POST("/api/auth/refresh")
    fun refreshToken(@Body tokenRequest: TokenRequest): Call<TokenResponse>

    // Endpoint para obtener los datos del usuario autenticado
    @GET("/api/users/me")
    fun getUserDetails(@Header("Authorization") token: String): Call<SignUpResponse>
}