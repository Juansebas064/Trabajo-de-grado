package com.brightbox.dino

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.brightbox.dino.navigation.NavigationRoot
import com.brightbox.dino.services.NotificationSender
import com.brightbox.dino.services.TasksWorker
import com.brightbox.dino.services.TimeLimitService
import com.brightbox.dino.viewmodel.preferences.PreferencesViewModel
import com.brightbox.dino.view.theme.DinoLauncherTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        sendBroadcast(Intent("QUERY_APPLICATIONS_TO_DATABASE"))
        createNotificationChannels()

        scheduleMidnightTaskValidation()
        scheduleAppUsageMonitoring()

        setContent {
            DinoLauncherTheme {
                val preferencesViewModel: PreferencesViewModel = hiltViewModel()
                val state = preferencesViewModel.state.collectAsState()
                val context = LocalContext.current

                val requestPermissionLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestPermission()
                ) { isGranted: Boolean ->
                    if (isGranted) {
//                        Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT)
//                            .show()
                    } else {
//                        Toast.makeText(this, "Notifications permission denied", Toast.LENGTH_SHORT)
//                            .show()
                    }
                }

                LaunchedEffect(Unit) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) { // Android 13+
                        if (ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                }

                BackHandler(enabled = true) { }

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (state.value.solidBackground) MaterialTheme.colorScheme.background
                            else MaterialTheme.colorScheme.background.copy(0.1f)
                        )
                ) {
                    NavigationRoot()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val serviceIntent = Intent(this, TimeLimitService::class.java)
        stopService(serviceIntent)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        Log.d("MainActivity", "Configuración cambiada. ${newConfig.locales.get(0)}")
    }

    private fun scheduleMidnightTaskValidation() {
        // Verifica si la tarea ya está programada para evitar duplicados
        val existingWork = WorkManager.getInstance(this)
            .getWorkInfosForUniqueWork(TasksWorker.WORK_NAME)
            .get() // Bloquea el hilo hasta obtener el resultado (ok para onCreate)

        val isScheduled = existingWork.any { !it.state.isFinished }

        if (!isScheduled) {
            // Calcula el delay hasta la próxima medianoche
            val delayMillis = TasksWorker.calculateNextMidnightDelayMillis()

            // Crea la solicitud de trabajo única (OneTimeWorkRequest)
            val updateRequest = OneTimeWorkRequestBuilder<TasksWorker>()
                .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
                .addTag(TasksWorker.WORK_NAME) // Añade el tag para identificarla
                .build()

            // Encola la tarea única
            // ExistingWorkPolicy.KEEP: Si ya existe una tarea con el mismo nombre,
            // la mantiene (útil para programar en cada inicio de app sin duplicar)
            WorkManager.getInstance(this).enqueueUniqueWork(
                TasksWorker.WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                updateRequest
            )
            Log.d(
                "MainActivity",
                "Programación inicial de la tarea de medianoche en $delayMillis ms."
            )
        } else {
            Log.d("MainActivity", "La tarea de medianoche ya está programada.")
        }
    }

    fun scheduleAppUsageMonitoring() {
        val serviceIntent = Intent(this, TimeLimitService::class.java)
        startService(serviceIntent)
    }

    fun createNotificationChannels() {

        val monitoringChannel = NotificationChannel(
            NotificationSender.MONITORING_CHANNEL_ID,
            NotificationSender.MONITORING_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            description = "Dino app monitoring channel"
        }

        val alertChannel = NotificationChannel(
            NotificationSender.ALERT_CHANNEL_ID,
            NotificationSender.ALERT_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Dino notification channel"
        }

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannels(
            listOf(monitoringChannel, alertChannel)
        )
    }
}