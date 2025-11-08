package com.hk.prettyweather.core.domain.model

data class WeatherLocalData(
    val cityId: String,
    val cityName: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val lastUpdatedEpochSeconds: Long,
)

