package com.example.aplicacionconciertos.model.activities

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.util.Log

class ActivitiesRepository(private val activitiesClient: ActivitiesClient) {

    suspend fun getAllActivities(accessToken: String): List<ActivityResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.getAllActivities("Bearer $accessToken").execute()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    Log.e("ActivitiesRepository", "Error code: ${response.code()}, body: ${response.errorBody()?.string()}")
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }


    // 2. Obtener participaciones de un usuario por su ID
    suspend fun getUserParticipations(userId: String): List<ParticipationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.getUserParticipations(userId).execute()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    Log.e("ActivitiesRepository", "Error code: ${response.code()}, body: ${response.errorBody()?.string()}")
                    emptyList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // 3. Crear una nueva participación (apuntarse a una actividad)
    suspend fun createParticipation(userId: String, activityId: Long): ParticipationResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.createParticipation(userId, activityId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("ActivitiesRepository", "Error code: ${response.code()}, body: ${response.errorBody()?.string()}")
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // 4. Eliminar una participación por su ID (borrarse de la actividad)
    suspend fun deleteParticipation(participationId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.deleteParticipation(participationId).execute()
                if (!response.isSuccessful) {
                    Log.e("ActivitiesRepository", "Error code: ${response.code()}, body: ${response.errorBody()?.string()}")
                }
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }

    // 5. Crear una nueva actividad (evento)
    suspend fun createActivity(
        name: String,
        description: String,
        date: String,
        place: String,
        category: String
    ): ActivityResponse? {
        return withContext(Dispatchers.IO) {
            try {
                // Construimos un map sin necesidad de una clase Request
                val activityData = mapOf(
                    "name" to name,
                    "description" to description,
                    "date" to date,       // Formato "YYYY-MM-DD"
                    "place" to place,
                    "category" to category
                )
                val response = activitiesClient.createActivity(activityData).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("ActivitiesRepository", "Error code: ${response.code()}, body: ${response.errorBody()?.string()}")
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
                    Log.e("ActivitiesRepository", "Error code: ${response.code()}, body: ${response.errorBody()?.string()}")
                }
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}
