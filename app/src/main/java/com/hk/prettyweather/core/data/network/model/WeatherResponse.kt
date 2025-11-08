package com.hk.prettyweather.core.data.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val name: String? = null,
    val main: WeatherMain? = null,
    val weather: List<WeatherCondition> = emptyList(),
    val wind: WeatherWind? = null,
    @SerialName("dt") val timestamp: Long? = null,
)

@Serializable
data class WeatherMain(
    val temp: Double? = null,
    val humidity: Int? = null,
)

@Serializable
data class WeatherCondition(
    val main: String? = null,
    val description: String? = null,
)

@Serializable
data class WeatherWind(
    val speed: Double? = null,
)

