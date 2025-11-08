package com.hk.prettyweather.core.data.repository

import com.hk.prettyweather.core.data.datastore.WeatherLocalDataSource
import com.hk.prettyweather.core.data.network.datasource.WeatherRemoteDataSource
import com.hk.prettyweather.core.data.network.model.toLocal
import com.hk.prettyweather.core.data.network.model.toRemote
import com.hk.prettyweather.core.domain.model.WeatherCity
import com.hk.prettyweather.core.domain.repository.WeatherRepository
import com.hk.prettyweather.core.data.network.NetworkMonitor
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DefaultWeatherRepository @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val localDataSource: WeatherLocalDataSource,
    networkMonitor: NetworkMonitor
) : BaseRepository(networkMonitor), WeatherRepository {

    private val cityCatalog: List<WeatherCity> = listOf(
        WeatherCity(id = "new_york", name = "New York", latitude = 40.7128, longitude = -74.0060),
        WeatherCity(id = "london", name = "London", latitude = 51.5074, longitude = -0.1278),
        WeatherCity(id = "tokyo", name = "Tokyo", latitude = 35.6762, longitude = 139.6503),
    )

    override fun supportedCities(): List<WeatherCity> = cityCatalog

    override fun observeWeather(cityId: String) =
        localDataSource.observe(cityId)

    override suspend fun refreshWeather(cityId: String): Result<Unit> {
        val city = cityCatalog.firstOrNull { it.id == cityId }
            ?: return Result.failure(IllegalArgumentException("Unsupported city: $cityId"))

        val networkResult = networkCall { remoteDataSource.fetch(city) }
            .map { response ->
                response.toRemote(city.id, city.name).toLocal()
            }

        networkResult.onSuccess { weather ->
            localDataSource.update(weather)
        }

        return networkResult.map {}
    }

    override suspend fun refreshAll(): Result<Unit> = supervisorScope {
        val deferredResults = cityCatalog.map { city ->
            async {
                refreshWeather(city.id)
            }
        }

        val failures = deferredResults.map { it.await() }.filter { it.isFailure }
        if (failures.isNotEmpty()) {
            failures.first()
        } else {
            Result.success(Unit)
        }
    }
}

