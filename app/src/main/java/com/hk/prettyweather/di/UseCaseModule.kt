package com.hk.prettyweather.di

import com.hk.prettyweather.core.domain.usecase.GetSupportedCitiesUseCase
import com.hk.prettyweather.core.domain.usecase.GetSupportedCitiesUseCaseImpl
import com.hk.prettyweather.core.domain.usecase.ObserveWeatherUseCase
import com.hk.prettyweather.core.domain.usecase.ObserveWeatherUseCaseImpl
import com.hk.prettyweather.core.domain.usecase.RefreshWeatherUseCase
import com.hk.prettyweather.core.domain.usecase.RefreshWeatherUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindRefreshWeatherUseCase(
        useCase: RefreshWeatherUseCaseImpl,
    ): RefreshWeatherUseCase

    @Binds
    @Singleton
    abstract fun bindObserveWeatherUseCase(
        useCase: ObserveWeatherUseCaseImpl,
    ): ObserveWeatherUseCase

    @Binds
    @Singleton
    abstract fun bindGetSupportedCitiesUseCase(
        useCase: GetSupportedCitiesUseCaseImpl,
    ): GetSupportedCitiesUseCase
}

