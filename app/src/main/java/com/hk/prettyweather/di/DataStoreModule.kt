package com.hk.prettyweather.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.hk.prettyweather.core.data.datastore.WeatherPreferences
import com.hk.prettyweather.core.data.datastore.WeatherPreferencesSerializer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    const val FILE_NAME = "weather_prefs.pb"

//    @Provides
//    @Singleton
//    fun provideWeatherPreferencesDataStore(
//        @ApplicationContext context: Context,
//        serializer: WeatherPreferencesSerializer,
//    ): DataStore<WeatherPreferences> =
//        DataStoreFactory.create(
//            serializer = serializer,
//            produceFile = { context.dataStoreFile(FILE_NAME) },
//        )
}

