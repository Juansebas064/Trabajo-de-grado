package com.brightbox.hourglass.usecases

import android.util.Log
import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.model.TasksModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksUseCase @Inject constructor(
    private val db: HourglassDatabase
) {
    fun getTodayTasks(date: String): Flow<List<TasksModel>> =
        db.tasksDao().getTasks().map { tasks ->
            tasks.filter { task ->
                if (task.isCompleted) {
                    task.dateCompleted == date
                } else {
                    true
                }
            }
        }

    suspend fun validateCurrentTasksOnMidnight(
        previousDate: String,
        currentDate: String,
        tasks: List<TasksModel>
    ) {
        Log.d("TasksUseCase", "Yesterday: $previousDate, Today: $currentDate")
        tasks.forEach { task ->
            Log.d("TasksUseCase", "${task.title}, Date: ${task.dateDue}")
            if (task.dateDue == previousDate) {
                setTaskDelayed(task.id!!)
            }
        }
    }

    suspend fun upsertTask(task: TasksModel) {
        db.tasksDao().upsertTask(task)
    }

    suspend fun deleteTask(id: Int) {
        db.tasksDao().deleteTask(id)
    }

    suspend fun setTaskCompleted(id: Int, dateCompleted: String) {
        db.tasksDao().setTaskCompleted(id, dateCompleted)
    }

    suspend fun setTaskUncompleted(id: Int) {
        db.tasksDao().setTaskUncompleted(id)
    }

    suspend fun setTaskDelayed(id: Int) {
        db.tasksDao().setTaskDelayed(id)
    }
}