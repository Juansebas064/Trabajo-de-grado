package com.brightbox.hourglass

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.brightbox.hourglass.navigation.NavigationRoot
import com.brightbox.hourglass.services.TasksWorker
import com.brightbox.hourglass.viewmodel.preferences.PreferencesViewModel
import com.brightbox.hourglass.views.theme.HourglassProductivityLauncherTheme
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

        scheduleMidnightTask()

        setContent {
            HourglassProductivityLauncherTheme {
                val preferencesViewModel: PreferencesViewModel = hiltViewModel()
                val state = preferencesViewModel.state.collectAsState()

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            if (state.value.solidBackground) MaterialTheme.colorScheme.background
                            else Color.Transparent
                        )
                ) {
                    NavigationRoot()
                }
            }
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
        Log.d("onUserLeaveHint", "onUserLeaveHint")
        val intent = Intent("HOME_BUTTON_PRESSED")
        sendBroadcast(intent)
    }

    private fun scheduleMidnightTask() {
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
                ExistingWorkPolicy.KEEP,
                updateRequest
            )
            Log.d(
                "MainActivty",
                "Programación inicial de la tarea de medianoche en $delayMillis ms."
            )
        } else {
            Log.d("MainActivty", "La tarea de medianoche ya está programada.")
        }
    }
}