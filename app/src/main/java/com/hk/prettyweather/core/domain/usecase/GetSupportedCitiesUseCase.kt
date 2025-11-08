package com.hk.prettyweather.core.domain.usecase

import com.hk.prettyweather.core.domain.model.WeatherCity
import com.hk.prettyweather.core.domain.repository.WeatherRepository
import javax.inject.Inject

fun interface GetSupportedCitiesUseCase {
    operator fun invoke(): List<WeatherCity>
}

class GetSupportedCitiesUseCaseImpl @Inject constructor(
    private val repository: WeatherRepository,
) : GetSupportedCitiesUseCase {

    override fun invoke(): List<WeatherCity> = repository.supportedCities()
}

