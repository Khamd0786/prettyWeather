package com.hk.prettyweather.core.data.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response

/**
 * Execute a Retrofit request safely, mapping common failure cases onto [Result].
 */
suspend fun <T> safeCall(
    apiCall: suspend () -> Response<T>,
): Result<T> = withContext(Dispatchers.IO) {
    try {
        val response = apiCall()
        if (response.isSuccessful) {
            val body = response.body()
            if (body != null) {
                Result.success(body)
            } else {
                Result.failure(EmptyBodyException(response))
            }
        } else {
            Result.failure(HttpException(response))
        }
    } catch (throwable: Throwable) {
        Result.failure(throwable)
    }
}

class EmptyBodyException(response: Response<*>) :
    IllegalStateException("Response body was null for call: ${response.raw().request.url}")

