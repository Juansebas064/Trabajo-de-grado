package com.brightbox.hourglass.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log

class TimeLimitService: Service() {

    private val TAG = "TimeLimitService"
    private val CHANNEL_ID = "HourglassTimeLimitChannel"
    private val NOTIFICATION_ID = 101

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: Foreground service created")
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // Solo para Android 8.0 (API 26) y superior
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Monitoreo de tiempo de aplicaciones de Hourglass",
                NotificationManager.IMPORTANCE_LOW // Nivel de importancia
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

}