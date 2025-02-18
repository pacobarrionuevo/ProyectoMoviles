package com.example.aplicacionconciertos.model.activities

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

    // Crear una nueva participación (apuntarse a una actividad)
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

    // Eliminar una participación por su ID (borrarse de la actividad)
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

    // Crear una nueva actividad (evento) usando un Map para enviar los datos
    suspend fun createActivity(
        name: String,
        description: String,
        date: String,
        place: String,
        category: String
    ): ActivityResponse? {
        return withContext(Dispatchers.IO) {
            try {
                val data = mapOf(
                    "name" to name,
                    "description" to description,
                    "date" to date,       // Asegúrate del formato (por ejemplo, "YYYY-MM-DD")
                    "place" to place,
                    "category" to category
                )
                val response = activitiesClient.createActivity(data).execute()
                if (response.isSuccessful) {
                    response.body()
                } else {
                    throw Exception("Error al crear la actividad: ${response.code()}")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    // Borrar una actividad por su ID
    suspend fun deleteActivity(activityId: Long): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val response = activitiesClient.deleteActivity(activityId).execute()
                response.isSuccessful
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}
