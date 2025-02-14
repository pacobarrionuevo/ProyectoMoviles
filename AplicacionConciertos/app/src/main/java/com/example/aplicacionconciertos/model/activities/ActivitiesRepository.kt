package com.example.aplicacionconciertos.model.activities
import com.example.aplicacionconciertos.model.activities.ActivityResponse
import com.example.aplicacionconciertos.model.activities.ActivitiesClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ActivitiesRepository(private val activitiesClient: ActivitiesClient) {

    // Obtener todas las actividades
    suspend fun getAllActivities(): List<ActivityResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.getAllActivities().execute()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    throw Exception("Error al obtener actividades: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // Obtener participaciones de un usuario por su ID
    suspend fun getUserParticipations(userId: String): List<ParticipationResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.getUserParticipations(userId).execute()
                if (response.isSuccessful) {
                    response.body() ?: emptyList()
                } else {
                    throw Exception("Error al obtener participaciones: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        }
    }

    // Crear una nueva participación
    suspend fun createParticipation(userId: String, activityId: Long): ParticipationResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.createParticipation(userId, activityId).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    throw Exception("Error al crear la participación: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // Eliminar una participación por su ID
    suspend fun deleteParticipation(participationId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.deleteParticipation(participationId).execute()
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}