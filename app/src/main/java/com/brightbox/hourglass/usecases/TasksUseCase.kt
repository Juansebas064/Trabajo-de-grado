package com.brightbox.hourglass.usecases

import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.model.TasksModel
import javax.inject.Inject

class TasksUseCase @Inject constructor(
    private val db: HourglassDatabase
) {
    fun getTasks() = db.tasksDao().getTasks()

    suspend fun upsertTask(task: TasksModel) {
        db.tasksDao().upsertTask(task)
    }

    suspend fun deleteTask(task: TasksModel) {
        db.tasksDao().deleteTask(task)
    }

    suspend fun setTaskCompleted(id: Int) {
        db.tasksDao().setTaskCompleted(id)
    }

    suspend fun setTaskUncompleted(id: Int) {
        db.tasksDao().setTaskUncompleted(id)
    }
}