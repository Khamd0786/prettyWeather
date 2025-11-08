package com.hk.prettyweather.core.domain.usecase

import com.hk.prettyweather.core.domain.model.WeatherLocalData
import com.hk.prettyweather.core.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

fun interface ObserveWeatherUseCase {
    operator fun invoke(cityId: String): Flow<WeatherLocalData?>
}

class ObserveWeatherUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository,
) : ObserveWeatherUseCase {

    override fun invoke(cityId: String): Flow<WeatherLocalData?> =
        repository.observeWeather(cityId)
}

