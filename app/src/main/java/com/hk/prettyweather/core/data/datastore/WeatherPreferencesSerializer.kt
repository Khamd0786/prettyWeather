package com.hk.prettyweather.core.data.datastore

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherPreferencesSerializer @Inject constructor() :
    Serializer<WeatherPreferences> {

    override val defaultValue: WeatherPreferences = WeatherPreferences.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): WeatherPreferences =
        try {
            WeatherPreferences.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            defaultValue
        }

    override suspend fun writeTo(t: WeatherPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}

