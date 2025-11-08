package com.hk.prettyweather.core.data.network.model

data class WeatherRemoteData(
    val cityId: String,
    val cityName: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val lastUpdatedEpochSeconds: Long,
)

