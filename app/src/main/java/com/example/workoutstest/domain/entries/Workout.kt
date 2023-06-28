package com.example.workoutstest.domain.entries

import com.google.gson.annotations.SerializedName

data class Workout (
    @SerializedName("_id")
    val id: String,
    val assignments: List<Assignment>
)

data class Assignment (
    @SerializedName("_id")
    val id: String? = null,
    val status: Long? = null,
    val title: String? = null,
    @SerializedName("exercises_count")
    val exercisesCount: Long? = null,
    val client: String? = null
)
