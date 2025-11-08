package com.hk.prettyweather.home.presentation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.hk.prettyweather.core.data.work.WeatherSyncScheduler
import com.hk.prettyweather.core.domain.usecase.GetSupportedCitiesUseCase
import com.hk.prettyweather.core.domain.usecase.ObserveWeatherUseCase
import com.hk.prettyweather.core.domain.usecase.RefreshWeatherUseCase
import com.hk.prettyweather.core.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    getSupportedCitiesUseCase: GetSupportedCitiesUseCase,
    private val observeWeatherUseCase: ObserveWeatherUseCase,
    private val refreshWeatherUseCase: RefreshWeatherUseCase,
    private val scheduler: WeatherSyncScheduler,
) : BaseViewModel<HomeState, HomeSideEffect>(
    initialState = HomeState(isLoading = true),
) {

    private val selectedCity = MutableStateFlow<String?>(null)

    init {
        val cities = getSupportedCitiesUseCase()
        val firstCityId = cities.firstOrNull()?.id
        setState(
            currentState.copy(
                cities = cities,
                selectedCityId = firstCityId,
                isLoading = true,
                error = null,
            ),
        )
        if (firstCityId != null) {
            selectedCity.value = firstCityId
        }
        scheduler.scheduleBatch(cities.map { it.id })
        observeSelectedCity()
        preloadCities(cities.mapNotNull { it.id })
    }

    private fun observeSelectedCity() {
        viewModelScope.launch {
            selectedCity
                .filterNotNull()
                .distinctUntilChanged()
                .flatMapLatest { cityId ->
                    observeWeatherUseCase(cityId).map { cityId to it }
                }
                .collectLatest { (cityId, weather) ->
                    updateState {
                        copy(
                            selectedCityId = cityId,
                            weather = weather,
                            isLoading = weather == null,
                            error = if (weather != null) null else error,
                        )
                    }
                }
        }
    }

    private fun preloadCities(cityIds: List<String>) {
        viewModelScope.launch {
            cityIds.forEach { cityId ->
                val result = refreshWeatherUseCase(cityId)
                if (result.isFailure && currentState.selectedCityId == cityId) {
                    val message = result.exceptionOrNull()?.localizedMessage
                        ?: "Unable to load weather data"
                    updateState { copy(isLoading = false, error = message) }
                    postSideEffect(HomeSideEffect.ShowMessage(message))
                }
            }
        }
    }

    fun onCitySelected(cityId: String) {
        if (selectedCity.value == cityId) return
        selectedCity.value = cityId
        updateState {
            copy(
                selectedCityId = cityId,
                isLoading = weather?.cityId != cityId,
                error = null,
            )
        }
        scheduler.schedule(cityId)
    }

    fun onManualRefresh() {
        val cityId = currentState.selectedCityId ?: return
        if (currentState.isRefreshing) return
        viewModelScope.launch {
            updateState { copy(isRefreshing = true, error = null) }
            val result = refreshWeatherUseCase(cityId)
            scheduler.schedule(cityId)
            if (result.isFailure) {
                val message = result.exceptionOrNull()?.localizedMessage
                    ?: "Unable to refresh weather"
                updateState { copy(isRefreshing = false, error = message) }
                postSideEffect(HomeSideEffect.ShowMessage(message))
            } else {
                updateState { copy(isRefreshing = false, error = null) }
            }
        }
    }
}

