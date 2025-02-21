package com.example.aplicacionconciertos.model.activities

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface ActivitiesClient {

    @GET("api/activities")
    suspend fun getAllActivities(
        @Header("Authorization") authHeader: String
    ): Response<List<ActivityResponse>>



    @GET("/api/participations/user/{userId}")
    fun getUserParticipations(
        @Path("userId") userId: String,
        @Header("Authorization") authHeader: String
    ): Call<List<ParticipationResponse>>

    @POST("/api/participations")
    @FormUrlEncoded
    fun createParticipation(
        @Field("userId") userId: String,
        @Field("activityId") activityId: Long
    ): Call<ParticipationResponse>

    @DELETE("/api/participations/{id}")
    fun deleteParticipation(@Path("id") participationId: Long): Call<Void>

    // Nuevo endpoint para crear una actividad. Se usa un Map para evitar crear un ActivityRequest.
    @POST("/api/activities")
    fun createActivity(
        @Body activityData: Map<String, Any>
    ): Call<ActivityResponse>

    // Nuevo endpoint para borrar una actividad.
    @DELETE("/api/activities/{id}")
    fun deleteActivity(
        @Path("id") activityId: Long
    ): Call<Void>
}
