package com.example.aplicacionconciertos.model.activities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

class ActivitiesRepository(private val activitiesClient: ActivitiesClient) {

    suspend fun getAllActivities(accessToken: String): List<ActivityResponse> {
        return try {
            Log.d("ActivitiesRepository", "Token antes de llamada: '$accessToken'")
            val response = activitiesClient.getAllActivities("Bearer $accessToken")
            Log.d("ActivitiesRepository", "Código de respuesta: ${response.code()}")

            if (response.isSuccessful) {
                val body = response.body()
                Log.d("ActivitiesRepository", "Cuerpo de respuesta: $body")
                body ?: emptyList()
            } else {
                Log.e("ActivitiesRepository", "Error en getAllActivities: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ActivitiesRepository", "Excepción en getAllActivities", e)
            emptyList()
        }
    }




    suspend fun getUserParticipations(userId: String, accessToken: String): List<ParticipationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("ActivitiesRepository", "Llamando getUserParticipations para userId: $userId")
                val response = activitiesClient.getUserParticipations(userId, "Bearer $accessToken").execute()
                if (response.isSuccessful) {
                    val participations = response.body() ?: emptyList()
                    Log.d("ActivitiesRepository", "getUserParticipations exitosa: ${participations.size} participaciones recibidas")
                    participations
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("ActivitiesRepository", "Error getUserParticipations: ${response.code()} - $error")
                    emptyList()
                }
            } catch (e: Exception) {
                Log.e("ActivitiesRepository", "Excepción en getUserParticipations", e)
                emptyList()
            }
        }
    }



    suspend fun createParticipation(userId: String, activityId: Long, accessToken: String): ParticipationResponse? {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("ActivitiesRepository", "Llamando createParticipation para userId: $userId, activityId: $activityId")
                val response = activitiesClient.createParticipation("Bearer $accessToken", userId, activityId)
                if (response.isSuccessful) {
                    val participation = response.body()
                    Log.d("ActivitiesRepository", "createParticipation exitosa: $participation")
                    participation
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("ActivitiesRepository", "Error createParticipation: ${response.code()} - $error")
                    null
                }
            } catch (e: Exception) {
                Log.e("ActivitiesRepository", "Excepción en createParticipation", e)
                null
            }
        }
    }



    suspend fun deleteParticipation(
        userId: String,
        activityId: Long,
        accessToken: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("ActivitiesRepository", "Eliminando participación para userId: $userId, activityId: $activityId")
                val response = activitiesClient.deleteParticipation(
                    userId,
                    activityId,
                    "Bearer $accessToken"
                ).execute()
                if (response.isSuccessful) {
                    Log.d("ActivitiesRepository", "deleteParticipation exitosa")
                } else {
                    val error = response.errorBody()?.string()
                    Log.e("ActivitiesRepository", "Error deleteParticipation: ${response.code()} - $error")
                }
                response.isSuccessful
            } catch (e: Exception) {
                Log.e("ActivitiesRepository", "Excepción en deleteParticipation", e)
                false
            }
        }
    }





}
