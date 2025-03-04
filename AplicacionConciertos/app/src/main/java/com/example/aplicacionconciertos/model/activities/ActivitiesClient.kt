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

    @POST("api/participations")
    suspend fun createParticipation(
        @Header("Authorization") authHeader: String,
        @Query("userId") userId: String,
        @Query("activityId") activityId: Long
    ): Response<ParticipationResponse>



    @DELETE("/api/participations/{userId}/{activityId}")
    fun deleteParticipation(
        @Path("userId") userId: String,
        @Path("activityId") activityId: Long,
        @Header("Authorization") authHeader: String
    ): Call<Void>

}
