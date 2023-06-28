package com.example.workoutstest.data.remote.retofit.api

import com.example.workoutstest.data.remote.retofit.response.WorkoutResponse
import retrofit2.Response
import retrofit2.http.GET

interface WorkoutApi {
    @GET("/workouts")
    suspend fun getWorkoutInfo(): Response<WorkoutResponse>
}