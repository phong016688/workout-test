package com.example.workoutstest.data.remote.retofit.interceptor

import com.example.workoutstest.data.local.prefrence.AppPrefKey
import com.example.workoutstest.data.local.prefrence.AppSharedPreferences
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

private const val contentType = "Content-Type"
private const val contentTypeValue = "application/x-www-form-urlencoded"
private const val authorization = "Authorization"
private const val csrfToken = "X-CSRF-Token"

@Singleton
class TokenInterceptor @Inject constructor(
    private val preferences: AppSharedPreferences
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = initializeRequestWithHeaders(chain.request())
        val response = chain.proceed(request)
        val responseBody = response.body
        val responseBodyString = response.body.string()
        return createNewResponse(response, responseBody, responseBodyString)
    }

    private fun initializeRequestWithHeaders(request: Request): Request {
        return request.newBuilder().apply {
            header(contentType, contentTypeValue)
            method(request.method, request.body)
        }.build()
    }

    private fun createNewResponse(
        response: Response,
        responseBody: ResponseBody?,
        responseBodyString: String?,
    ): Response {
        val contentType = responseBody?.contentType()
        return response.newBuilder()
            .body((responseBodyString ?: "").toResponseBody(contentType))
            .build()
    }
}