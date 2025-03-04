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
        //Response
    ): Call<List<ParticipationResponse>>

    @POST("/api/participations")
    @FormUrlEncoded
    fun createParticipation(
        @Field("userId") userId: String,
        @Field("activityId") activityId: Long
    ): Call<ParticipationResponse>

    @DELETE("/api/participations/{id}")
    fun deleteParticipation(@Path("id") participationId: Long): Call<Void>

}
