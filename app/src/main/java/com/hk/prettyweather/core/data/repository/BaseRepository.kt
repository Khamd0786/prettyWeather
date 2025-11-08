package com.hk.prettyweather.core.data.repository

import com.hk.prettyweather.core.data.network.NetworkMonitor
import com.hk.prettyweather.core.data.network.NetworkUnavailableException
import com.hk.prettyweather.core.data.network.safeCall
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

/**
 * Common parent for repositories working with remote data sources.
 *
 * Provides helpers for enforcing network availability and mapping Retrofit responses to [Result].
 */
abstract class BaseRepository(
    private val networkMonitor: NetworkMonitor,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CoreRepository {

    /**
     * Run a Retrofit call after verifying network availability.
     *
     * @return [Result.success] when the call succeeds, [Result.failure] when network is absent or
     * the request fails.
     */
    protected suspend fun <T> networkCall(
        apiCall: suspend () -> Response<T>,
    ): Result<T> {
        if (!networkMonitor.isConnected()) {
            return Result.failure(NetworkUnavailableException())
        }
        return safeCall(dispatcher, apiCall)
    }

    /**
     * Extract the value from a [Result] or throw its failure.
     */
    protected fun <T> Result<T>.requireValue(): T = getOrThrow()
}

