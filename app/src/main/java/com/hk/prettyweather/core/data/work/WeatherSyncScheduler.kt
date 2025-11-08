package com.hk.prettyweather.core.data.work

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import java.time.Duration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherSyncScheduler @Inject constructor(
    private val workManager: WorkManager,
) {

    @RequiresApi(Build.VERSION_CODES.O)
    fun schedule(cityId: String, delay: Duration = Duration.ofSeconds(30)) {
        val request = OneTimeWorkRequestBuilder<WeatherSyncWorker>()
            .setInitialDelay(delay)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build(),
            )
            .setInputData(
                workDataOf(WeatherSyncWorker.KEY_CITY_ID to cityId),
            )
            .build()

        workManager.enqueueUniqueWork(
            uniqueNameFor(cityId),
            ExistingWorkPolicy.REPLACE,
            request,
        )
    }

    fun cancel(cityId: String) {
        workManager.cancelUniqueWork(uniqueNameFor(cityId))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun scheduleBatch(cityIds: List<String>) {
        cityIds.forEach { schedule(it) }
    }

    private fun uniqueNameFor(cityId: String): String = "weather_sync_$cityId"
}

