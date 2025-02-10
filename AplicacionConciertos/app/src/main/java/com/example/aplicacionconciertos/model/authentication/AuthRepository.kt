package com.example.aplicacionconciertos.model.authentication

import retrofit2.Call
import retrofit2.Response

class AuthRepository {

    private val authClient = RetrofitInstance.authClient

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = authClient.login(AuthRequest(email, password)).execute()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login failed: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun signUp(email: String, password: String): Result<SignUpResponse> {
        return try {
            val response = authClient.signUp(AuthRequest(email, password, "USER")).execute()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Sign up failed: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun refreshToken(refreshToken: String): Result<TokenResponse> {
        return try {
            val response = authClient.refreshToken(TokenRequest(refreshToken)).execute()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Token refresh failed: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserDetails(accessToken: String): Result<SignUpResponse> {
        return try {
            val response = authClient.getUserDetails("Bearer $accessToken").execute()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to get user details: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}