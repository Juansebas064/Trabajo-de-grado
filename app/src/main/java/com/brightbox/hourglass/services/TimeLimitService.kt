package com.brightbox.hourglass.services

import android.app.Service
import android.app.usage.UsageStats
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.compose.ui.util.fastFilteredMap
import androidx.core.app.NotificationCompat
import com.brightbox.hourglass.model.LimitsModel
import com.brightbox.hourglass.usecases.LimitsUseCase
import com.brightbox.hourglass.utils.formatMillisecondsToMinutes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimeLimitService : Service() {

    @Inject
    lateinit var limitsUseCase: LimitsUseCase
    private var limitsList: List<LimitsModel> = listOf()

    private var serviceJob: Job = Job()
    private var serviceScope: CoroutineScope = CoroutineScope(Dispatchers.IO + serviceJob)
    private var isMonitoringActive = false

    private val TAG = "TimeLimitService"

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()

        Log.d(TAG, "onCreate: Foreground service created")
        observeLimits()
    }

    private fun observeLimits() {
        // Lanza una corrutina separada para observar los límites continuamente
        serviceScope.launch {
            limitsUseCase.getLimits().collectLatest { limits ->
                Log.d(TAG, "Limits updated from Flow: $limits")
                limitsList = limits // Actualiza la variable con la lista más reciente
            }
            // Esta corrutina se quedará aquí mientras el Flow limits esté activo
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        val notification =
            NotificationCompat.Builder(this, NotificationSender.MONITORING_CHANNEL_ID)
                .setContentTitle("Hourglass") // Title for the notification
                .setContentText("Monitoring app usage") // Text for the notification
                .setSmallIcon(android.R.drawable.sym_def_app_icon) // **REQUIRED:** An icon for the notification
                .setPriority(NotificationCompat.PRIORITY_LOW) // Set priority (LOW is less intrusive)
                .setOngoing(true)
                .build()

        startForeground(NotificationSender.MONITORING_NOTIFICATION_ID, notification)

        if (!isMonitoringActive) {
            startMonitoring()
            Log.d(TAG, "Monitoring loop started.")
            isMonitoringActive = true
        } else {
            Log.d(TAG, "Monitoring loop is already active. Skipping re-launch.")
        }

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isMonitoringActive = false
        serviceJob.cancel()
        stopForeground(STOP_FOREGROUND_REMOVE)
        Log.d(TAG, "onDestroy: Foreground service stopped")
    }

    fun startMonitoring() {
        var usageAccessPermission: Boolean
        var systemWindowAlertPermission: Boolean

        var usageStats = limitsUseCase.getUsageStats()
        var filteredAndUpdatedLimits: List<LimitsModel> = listOf()

        serviceScope.launch {
            Log.d(TAG, "Coroutine started.")

            while (true) {
                usageAccessPermission = limitsUseCase.isUsageAccessPermissionGranted()
                systemWindowAlertPermission = limitsUseCase.isSystemAlertPermissionGranted()
                Log.d(TAG, "Permissions: $usageAccessPermission and $systemWindowAlertPermission")

                if (usageAccessPermission && systemWindowAlertPermission) {
                    usageStats = limitsUseCase.getUsageStats()

                    filteredAndUpdatedLimits = filterAndUpdateLimits(usageStats)

                    Log.d(TAG, "Usage stats: $filteredAndUpdatedLimits")

                    filteredAndUpdatedLimits.forEach { limit ->
                        limitsUseCase.upsertLimit(limit)
                    }
                }
                delay(15000)
            }
        }
    }

    private fun filterAndUpdateLimits(stats: Map<String, Int>): List<LimitsModel> {

        val limitsFilteredAndUpdated = limitsList.filter { limit ->
            stats.any { stat ->
                stat.key == limit.applicationPackageName
            } == true
        }.map { limit ->
            val timeInForeground = stats[limit.applicationPackageName]!!
            limit.copy(
                usedTime = timeInForeground
            )
        }

        return limitsFilteredAndUpdated
    }
}
