package com.example.aplicacionconciertos.model.authentication


import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthClient {
    //
    @POST("/api/auth")
    fun login(@Body authRequest: AuthRequest): Call<LoginResponse>

    @POST("/api/users")
    fun signUp(@Body authRequest: AuthRequest): Call<SignUpResponse>

    @POST("/api/auth/refresh")
    fun refreshToken(@Body tokenRequest: TokenRequest): Call<TokenResponse>

    @GET("/api/users/me")
    fun getUserDetails(@Header("Authorization") token: String): Call<SignUpResponse>
}