package com.example.aplicacionconciertos.model.activities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log
import com.example.aplicacionconciertos.model.authentication.LoginResponse
class ActivitiesRepository(private val activitiesClient: ActivitiesClient) {

    private var accessToken: String = ""

    fun setAccessToken(token: String) {
        accessToken = token
    }

    suspend fun getAllActivities(): List<ActivityResponse> {
        return try {
            val response = activitiesClient.getAllActivities("Bearer $accessToken") // Sin `.execute()`
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                Log.e("ActivitiesRepository", "Error getAllActivities: ${response.code()}, body: ${response.errorBody()?.string()}")
                emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }


    suspend fun getUserParticipations(Id: String): List<ParticipationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("ActivitiesRepository", "userId antes de llamada: '${Id}'") // Asegúrate de que aquí sea correcto

                val response = activitiesClient.getUserParticipations(Id).execute()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    Log.e("ActivitiesRepository", "Error getUserParticipations: ${response.code()}, body: ${response.errorBody()?.string()}")
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }


    suspend fun createParticipation(userId: String, activityId: Long): ParticipationResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.createParticipation(userId, activityId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("ActivitiesRepository", "Error createParticipation: ${response.code()}, body: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun deleteParticipation(participationId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.deleteParticipation(participationId).execute()
                if (!response.isSuccessful) {
                    Log.e("ActivitiesRepository", "Error deleteParticipation: ${response.code()}, body: ${response.errorBody()?.string()}")
                }
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    suspend fun createActivity(
        name: String,
        description: String,
        date: String,
        place: String,
        category: String
    ): ActivityResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val activityData = mapOf(
                    "name" to name,
                    "description" to description,
                    "date" to date,       // "YYYY-MM-DD"
                    "place" to place,
                    "category" to category
                )
                val response = activitiesClient.createActivity(activityData).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("ActivitiesRepository", "Error createActivity: ${response.code()}, body: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // 6. Borrar una actividad por su ID
    suspend fun deleteActivity(activityId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.deleteActivity(activityId).execute()
                if (!response.isSuccessful) {
                    Log.e("ActivitiesRepository", "Error deleteActivity: ${response.code()}, body: ${response.errorBody()?.string()}")
                }
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

}
