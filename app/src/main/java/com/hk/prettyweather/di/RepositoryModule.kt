package com.hk.prettyweather.di

import com.hk.prettyweather.core.data.repository.DefaultWeatherRepository
import com.hk.prettyweather.core.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        repository: DefaultWeatherRepository,
    ): WeatherRepository
}

