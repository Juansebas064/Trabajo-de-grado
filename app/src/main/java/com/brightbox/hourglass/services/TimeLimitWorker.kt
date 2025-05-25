package com.brightbox.hourglass.services

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext

@HiltWorker
class TimeLimitWorker @AssistedInject constructor(
    @ApplicationContext context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val TAG = "TimeLimitWorker"

    override suspend fun doWork(): Result {
        Log.d(TAG, "AppUsageMonitorWorker started...")
        val context = applicationContext
        context.sendBroadcast(
            Intent("TIME_LIMIT_WORKER")
        )
        return Result.success()
    }

}