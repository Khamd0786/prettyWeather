package com.hk.prettyweather.home.presentation

import com.hk.prettyweather.core.domain.model.WeatherCity
import com.hk.prettyweather.core.domain.model.WeatherLocalData
import com.hk.prettyweather.core.presentation.SideEffect
import com.hk.prettyweather.core.presentation.UiState

data class HomeState(
    override val isLoading: Boolean = false,
    override val error: String? = null,
    val cities: List<WeatherCity> = emptyList(),
    val selectedCityId: String? = null,
    val weather: WeatherLocalData? = null,
    val isRefreshing: Boolean = false,
) : UiState {
    val lastUpdatedEpochSeconds: Long? = weather?.lastUpdatedEpochSeconds
}

sealed interface HomeSideEffect : SideEffect {
    data class ShowMessage(val message: String) : HomeSideEffect
}

