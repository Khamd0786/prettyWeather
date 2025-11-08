package com.hk.prettyweather.di

import android.content.Context
import com.hk.prettyweather.core.data.network.DefaultNetworkMonitor
import com.hk.prettyweather.core.data.network.NetworkMonitor
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(
        @ApplicationContext context: Context,
    ): Context = context

    @Provides
    @Singleton
    fun provideNetworkMonitor(
        defaultNetworkMonitor: DefaultNetworkMonitor,
    ): NetworkMonitor = defaultNetworkMonitor

    @Provides
    @Singleton
    fun provideWorkManager(
        @ApplicationContext context: Context,
    ): WorkManager = WorkManager.getInstance(context)
}

