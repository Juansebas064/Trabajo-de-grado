package com.brightbox.dino.services

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.brightbox.dino.R

class NotificationSender {
    companion object {
        const val MONITORING_CHANNEL_ID =
            "dino_monitoring_channel" // Debe coincidir con el ID del canal
        const val MONITORING_CHANNEL_NAME = "Dino app monitoring"
        const val MONITORING_NOTIFICATION_ID = 101 // ID único para la notificación
        const val ALERT_CHANNEL_ID =
            "dino_notification_channel" // Debe coincidir con el ID del canal
        const val ALERT_CHANNEL_NAME = "Dino notifications"
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
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(notificationManager.getNotificationChannel(channelId).importance)
                .setAutoCancel(true)

            notificationManager.notify(
                notificationId,
                builder.build()
            )
        }
    }
}