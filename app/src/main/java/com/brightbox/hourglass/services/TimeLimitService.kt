package com.brightbox.hourglass.services

import android.app.Service
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.brightbox.hourglass.TimeLimitOverlayActivity
import com.brightbox.hourglass.model.LimitsModel
import com.brightbox.hourglass.usecases.LimitsUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TimeLimitService : Service() {

    @Inject
    lateinit var limitsUseCase: LimitsUseCase
    private var limitsList: List<LimitsModel> = listOf()
    var usageAccessPermission: Boolean = false
    var systemWindowAlertPermission: Boolean = false

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


        val timeLimitOverlayIntent = Intent(
            applicationContext, TimeLimitOverlayActivity::class.java
        ).addFlags(FLAG_ACTIVITY_NEW_TASK)

        serviceScope.launch {
            Log.d(TAG, "Coroutine started.")

            while (true) {
                usageAccessPermission = limitsUseCase.isUsageAccessPermissionGranted()
                systemWindowAlertPermission = limitsUseCase.isSystemAlertPermissionGranted()
                Log.d(TAG, "Permissions: $usageAccessPermission and $systemWindowAlertPermission")

                if (usageAccessPermission && systemWindowAlertPermission) {

                    limitsList.forEach { limit ->
                        limitsUseCase.upsertLimit(limit)
                    }

                    Log.d(TAG, "Limits: $limitsList")
                    val limitWithTimeReached = limitsList.find {
                        it.usedTime >= it.timeLimit
                                && it.previousUsedTime != it.usedTime
                    }

                    if (limitWithTimeReached != null) {
                        Log.d(TAG, "App with time limit reached: $limitWithTimeReached")
                        timeLimitOverlayIntent.putExtra(
                            "PACKAGE_NAME",
                            limitWithTimeReached.applicationPackageName
                        )
                        limitsUseCase.updateLimit(
                            limitWithTimeReached.copy(
                                previousUsedTime = limitWithTimeReached.usedTime
                            )
                        )
                        startActivity(timeLimitOverlayIntent)
                        Log.d(TAG, "App updated in bd: ${limitsList.find { it.applicationPackageName == limitWithTimeReached.applicationPackageName }}")
                    }
                } else {
                    NotificationSender.sendNotification(
                        applicationContext,
                        "Hourglass time monitoring stopped",
                        "One or more permissions are not granted, please go to application limits to re-enable them",
                        NotificationSender.ALERT_CHANNEL_ID,
                        NotificationSender.ALERT_NOTIFICATION_ID
                    )
                    onDestroy()
                }
                delay(30000)
            }
        }
    }
}
