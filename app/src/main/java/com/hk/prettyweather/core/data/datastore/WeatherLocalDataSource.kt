package com.hk.prettyweather.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.hk.prettyweather.core.domain.model.WeatherLocalData
import com.hk.prettyweather.di.DataStoreModule.FILE_NAME
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherLocalDataSource @Inject constructor(
    @ApplicationContext context: Context,
    serializer: WeatherPreferencesSerializer,
) {
    private val dataStore: DataStore<WeatherPreferences> =  DataStoreFactory.create(
        serializer = serializer,
        produceFile = { context.dataStoreFile(FILE_NAME) },
    )

    fun observe(cityId: String): Flow<WeatherLocalData?>
       = dataStore.data.map { preferences ->
            preferences.cityWeatherList.firstOrNull { it.cityId == cityId }?.toLocal()
        }

    suspend fun update(weather: WeatherLocalData) {
        dataStore.updateData { preferences ->
            val builder = preferences.toBuilder()
            val proto = weather.toProto()
            val index = builder.cityWeatherList.indexOfFirst { it.cityId == weather.cityId }
            if (index >= 0) {
                builder.setCityWeather(index, proto)
            } else {
                builder.addCityWeather(proto)
            }
            builder.build()
        }
    }

    suspend fun updateAll(items: List<WeatherLocalData>) {
        dataStore.updateData { preferences ->
            val builder = preferences.toBuilder()
            items.forEach { weather ->
                val proto = weather.toProto()
                val index = builder.cityWeatherList.indexOfFirst { it.cityId == weather.cityId }
                if (index >= 0) {
                    builder.setCityWeather(index, proto)
                } else {
                    builder.addCityWeather(proto)
                }
            }
            builder.build()
        }
    }
}

