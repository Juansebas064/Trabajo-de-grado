package com.brightbox.hourglass.services

import android.R
import android.app.NotificationManager
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat

class NotificationSender {
    companion object {
        const val MONITORING_CHANNEL_ID =
            "hourglass_monitoring_channel" // Debe coincidir con el ID del canal
        const val MONITORING_CHANNEL_NAME = "Hourglass app monitoring"
        const val MONITORING_NOTIFICATION_ID = 101 // ID único para la notificación
        const val ALERT_CHANNEL_ID =
            "hourglass_notification_channel" // Debe coincidir con el ID del canal
        const val ALERT_CHANNEL_NAME = "Hourglass notifications"
        const val ALERT_NOTIFICATION_ID = 102 // ID único para la notificación

        fun sendNotification(
            context: Context,
            title: String,
            content: String,
            channelId: String,
            notificationId: Int
        ) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.sym_def_app_icon) // Icono pequeño de la notificación
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(notificationManager.getNotificationChannel(channelId).importance)
                .setAutoCancel(true) // Cierra la notificación al tocarla

            notificationManager.notify(
                notificationId,
                builder.build()
            )
            Log.d("NotificationSender", "Notification sent")
        }
    }
}