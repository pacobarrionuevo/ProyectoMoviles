package com.example.aplicacionconciertos.model.authentication

class AuthRepository {

    private val authClient = RetrofitInstance.authClient

    suspend fun login(email: String, password: String): Result<LoginResponse> {
        return try {
            val response = authClient.login(AuthRequest(email, password)).execute()
            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Login fallido: ${response.errorBody()?.string()}"))
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
                Result.failure(Exception("Registro fallido: ${response.errorBody()?.string()}"))
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
                Result.failure(Exception("Refresco del token fallido: ${response.errorBody()?.string()}"))
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
                Result.failure(Exception("No se han podido obtener los datos del usuario: ${response.errorBody()?.string()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}