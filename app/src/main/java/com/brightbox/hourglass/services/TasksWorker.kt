package com.brightbox.hourglass.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

@HiltWorker
class TasksWorker @AssistedInject constructor(
    @ApplicationContext private val context: Context,
    @Assisted val workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val WORK_NAME = "TasksWorker"

        fun calculateNextMidnightDelayMillis(): Long {
            val now =
                LocalDateTime.now(ZoneId.systemDefault()) // Hora y fecha actuales en la zona del sistema
            val midnight = LocalTime.MIDNIGHT // 00:00

            // Determinar si la próxima medianoche es hoy o mañana
            val nextMidnight =
                if (now.toLocalTime().isBefore(midnight) && now.toLocalTime() != midnight) {
                    // Si es antes de medianoche (y no es exactamente medianoche), programar para medianoche hoy
                    LocalDateTime.of(now.toLocalDate(), midnight)
                } else {
                    // Si es medianoche o después, programar para medianoche de mañana
                    LocalDateTime.of(now.toLocalDate().plusDays(1), midnight)
                }

            // Calcular la duración entre ahora y la próxima medianoche
            // Es importante usar ZonedDateTime para manejar correctamente los cambios de horario de verano/invierno
            val nowZoned = now.atZone(ZoneId.systemDefault())
            val nextMidnightZoned = nextMidnight.atZone(ZoneId.systemDefault())

            // Asegurarse de que el tiempo calculado es realmente en el futuro
            // Esto puede ocurrir si la función se llama exactamente a medianoche
            val finalNextMidnightZoned =
                if (nextMidnightZoned.isBefore(nowZoned) || nextMidnightZoned == nowZoned) {
                    nextMidnight.plusDays(1)
                        .atZone(ZoneId.systemDefault()) // Sumar un día más si ya pasó
                } else {
                    nextMidnightZoned
                }

            val delay = Duration.between(nowZoned, finalNextMidnightZoned).toMillis()

            // Asegurarse de que el delay no sea negativo (aunque la lógica anterior debería evitarlo)
            return maxOf(
                0,
                delay
            ) // Devolver 0 si el delay es negativo, aunque es mejor que sea positivo
//            return 30000L
        }
    }

    override suspend fun doWork(): Result {
        try {
            Log.d("TasksWorker", "Iniciando actualización de tareas a medianoche.")
            val intent = Intent("UPDATE_TASKS")
            applicationContext.sendBroadcast(intent)
            scheduleNextRun(applicationContext)
            return Result.success()
        } catch (e: Exception) {
            // Manejar cualquier error que ocurra durante la actualización
            Log.d("TasksWorker", "Error al ejecutar la actualización de tareas: ${e.message}")
            e.printStackTrace() // Imprimir el stack trace para depuración
            return Result.failure()
        }
    }

    private fun scheduleNextRun(context: Context) {
        val delayMillis = calculateNextMidnightDelayMillis()

        val updateRequest = OneTimeWorkRequestBuilder<TasksWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .addTag(WORK_NAME) // Asignar el mismo tag para poder gestionar la tarea
            .build()

        // Encolar la tarea única. Usamos ExistingWorkPolicy.REPLACE
        // para asegurarnos de que si por alguna razón ya hay una tarea con el mismo nombre programada,
        // la reemplazamos por la nueva programación (la de la próxima medianoche).
        WorkManager.getInstance(context).enqueueUniqueWork(
            WORK_NAME, // Nombre único para identificar la tarea
            ExistingWorkPolicy.REPLACE, // Si ya existe una tarea con este nombre, reemplázala.
            updateRequest
        )
        Log.d("TasksWorker", "Reprogramado para la próxima medianoche en ${delayMillis} ms.")
    }
}