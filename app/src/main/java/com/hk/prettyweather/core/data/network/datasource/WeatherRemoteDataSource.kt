package com.hk.prettyweather.core.data.network.datasource

import com.hk.prettyweather.core.data.network.api.WeatherApiService
import com.hk.prettyweather.BuildConfig
import com.hk.prettyweather.core.data.network.model.WeatherResponse
import com.hk.prettyweather.core.domain.model.WeatherCity
import retrofit2.Response
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val apiService: WeatherApiService,
) {

    suspend fun fetch(city: WeatherCity): Response<WeatherResponse> =
        apiService.getCurrentWeatherByLocation(
            latitude = city.latitude,
            longitude = city.longitude,
            apiKey = BuildConfig.OPEN_WEATHER_API_KEY,
        )
}

