package com.hk.prettyweather.core.data.network.api

import com.hk.prettyweather.core.data.network.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
    ): Response<WeatherResponse>

    @GET("data/2.5/weather")
    suspend fun getCurrentWeatherByLocation(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric",
    ): Response<WeatherResponse>
}

