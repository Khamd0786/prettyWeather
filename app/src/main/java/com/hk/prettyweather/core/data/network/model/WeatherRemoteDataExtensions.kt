package com.hk.prettyweather.core.data.network.model

import com.hk.prettyweather.core.domain.model.WeatherLocalData

fun WeatherRemoteData.toLocal(): WeatherLocalData =
    WeatherLocalData(
        cityId = cityId,
        cityName = cityName,
        temperature = temperature,
        condition = condition,
        humidity = humidity,
        windSpeed = windSpeed,
        lastUpdatedEpochSeconds = lastUpdatedEpochSeconds,
    )

