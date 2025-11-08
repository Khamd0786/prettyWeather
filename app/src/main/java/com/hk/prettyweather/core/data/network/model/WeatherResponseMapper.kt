package com.hk.prettyweather.core.data.network.model

import kotlin.math.roundToInt

fun WeatherResponse.toRemote(
    cityId: String,
    fallbackCityName: String,
): WeatherRemoteData {
    val resolvedCityName = name?.takeIf { it.isNotBlank() } ?: fallbackCityName
    val resolvedCondition = weather.firstOrNull()?.description
        ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
        ?: weather.firstOrNull()?.main
        ?: "Unknown"
    val resolvedHumidity = main?.humidity ?: 0
    val resolvedTemperature = main?.temp ?: 0.0
    val resolvedWindSpeed = wind?.speed ?: 0.0
    val resolvedTimestamp = ((timestamp ?: System.currentTimeMillis()) / 1000L)

    return WeatherRemoteData(
        cityId = cityId,
        cityName = resolvedCityName,
        temperature = (resolvedTemperature * 10).roundToInt() / 10.0,
        condition = resolvedCondition,
        humidity = resolvedHumidity,
        windSpeed = resolvedWindSpeed,
        lastUpdatedEpochSeconds = resolvedTimestamp,
    )
}

