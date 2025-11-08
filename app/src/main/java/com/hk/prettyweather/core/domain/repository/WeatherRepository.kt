package com.hk.prettyweather.core.domain.repository

import com.hk.prettyweather.core.domain.model.WeatherCity
import com.hk.prettyweather.core.domain.model.WeatherLocalData
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun supportedCities(): List<WeatherCity>
    fun observeWeather(cityId: String): Flow<WeatherLocalData?>
    suspend fun refreshWeather(cityId: String): Result<Unit>
    suspend fun refreshAll(): Result<Unit>
}

