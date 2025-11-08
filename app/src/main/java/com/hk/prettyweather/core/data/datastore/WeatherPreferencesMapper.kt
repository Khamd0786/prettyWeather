package com.hk.prettyweather.core.data.datastore

import com.hk.prettyweather.core.domain.model.WeatherLocalData

fun CityWeather.toLocal(): WeatherLocalData =
    WeatherLocalData(
        cityId = cityId,
        cityName = cityName,
        temperature = temperature,
        condition = condition,
        humidity = humidity,
        windSpeed = windSpeed,
        lastUpdatedEpochSeconds = lastUpdatedEpochSeconds,
    )

fun WeatherLocalData.toProto(): CityWeather =
    CityWeather.newBuilder()
        .setCityId(cityId)
        .setCityName(cityName)
        .setTemperature(temperature)
        .setCondition(condition)
        .setHumidity(humidity)
        .setWindSpeed(windSpeed)
        .setLastUpdatedEpochSeconds(lastUpdatedEpochSeconds)
        .build()

