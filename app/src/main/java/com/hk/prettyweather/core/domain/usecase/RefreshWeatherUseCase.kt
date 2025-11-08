package com.hk.prettyweather.core.domain.usecase

import com.hk.prettyweather.core.domain.repository.WeatherRepository
import javax.inject.Inject

fun interface RefreshWeatherUseCase {
    suspend operator fun invoke(cityId: String): Result<Unit>
}

class RefreshWeatherUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository,
) : RefreshWeatherUseCase {

    override suspend fun invoke(cityId: String): Result<Unit> =
        repository.refreshWeather(cityId)
}

