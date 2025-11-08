package com.hk.prettyweather.core.data.work

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.hk.prettyweather.core.domain.usecase.RefreshWeatherUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.Duration

@HiltWorker
class WeatherSyncWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val refreshWeatherUseCase: RefreshWeatherUseCase,
    private val scheduler: WeatherSyncScheduler,
) : CoroutineWorker(context, workerParams) {

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val cityId = inputData.getString(KEY_CITY_ID) ?: return Result.failure()
        val result = refreshWeatherUseCase(cityId)
        scheduler.schedule(cityId, Duration.ofSeconds(30))
        return if (result.isSuccess) {
            Result.success()
        } else {
            Result.success(workDataOf(KEY_CITY_ID to cityId))
        }
    }

    companion object {
        const val KEY_CITY_ID = "key_city_id"
    }
}

