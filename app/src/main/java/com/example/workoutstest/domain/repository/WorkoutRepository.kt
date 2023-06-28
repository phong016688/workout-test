package com.example.workoutstest.domain.repository

import com.example.workoutstest.domain.entries.Workout
import kotlinx.coroutines.flow.Flow

interface WorkoutRepository {
    fun fetchWorkouts(): Flow<List<Workout>>
    fun saveDataLocal(workouts: List<Workout>)
    fun getDataLocal(): List<Workout>?
}