package com.hk.prettyweather.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    const val FILE_NAME = "weather_prefs.pb"
}

