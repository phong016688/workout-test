package com.example.workoutstest.data.repository

import com.example.workoutstest.data.local.prefrence.AppPrefKey
import com.example.workoutstest.data.local.prefrence.AppSharedPreferences
import com.example.workoutstest.data.remote.retofit.api.WorkoutApi
import com.example.workoutstest.domain.entries.Workout
import com.example.workoutstest.domain.repository.WorkoutRepository
import com.example.workoutstest.utils.getFromApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class WorkoutRepositoryImpl @Inject constructor(
    private val workoutApi: WorkoutApi,
    private val appSharedPreferences: AppSharedPreferences
) : WorkoutRepository {
    override fun fetchWorkouts(): Flow<List<Workout>> {
        return getFromApi {
            workoutApi.getWorkoutInfo()
        }.map { it.data }
    }

    override fun saveDataLocal(workouts: List<Workout>) {
        val listType = object : TypeToken<List<Workout>>() {}.type
        val json = Gson().toJson(workouts, listType)
        appSharedPreferences.put(AppPrefKey.WORKOUT_LOCAL, json)
    }

    override fun getDataLocal(): List<Workout>? {
        val listType = object : TypeToken<List<Workout>>() {}.type
        val json = appSharedPreferences.get(AppPrefKey.WORKOUT_LOCAL, String::class.java)
        return json?.takeIf { it.isNotEmpty() }?.let { Gson().fromJson(it, listType) }
    }
}