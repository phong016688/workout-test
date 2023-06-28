package com.example.workoutstest.utils

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response

fun <T : Any> getFromApi(f: suspend () -> Response<T>): Flow<T> {
    return flow {
        val res = f()
        if (res.isSuccessful) {
            res.body()?.let { emit(it) } ?: error("DATA NULL")
        } else {
            error(res.errorBody() ?: "")
        }
    }
}