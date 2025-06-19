package com.brightbox.dino.model.usecases

import com.brightbox.dino.config.DinoDatabase
import com.brightbox.dino.model.constants.PrioritiesEnum
import com.brightbox.dino.model.TasksModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksUseCase @Inject constructor(
    private val db: DinoDatabase
) {
    fun getTasks(): Flow<List<TasksModel>> =
        db.tasksDao().getTasks().map { taskList ->
            taskList.sortedWith(
                compareBy(
                    { it.isCompleted },
                    { it.dateDue },
                    { PrioritiesEnum.valueOf(it.priority).value }
                ))
        }

    suspend fun validateCurrentTasksOnMidnight(
        previousDate: String,
        tasks: List<TasksModel>
    ) {
        tasks.forEach { task ->
            if (task.dateDue == previousDate) {
                setTaskDelayed(task.id!!)
            }
            if (task.dateCompleted == previousDate) {
                hideTask(task.id!!)
            }
        }
    }

    suspend fun upsertTask(task: TasksModel) {
        db.tasksDao().upsertTask(task)
    }

    suspend fun deleteTask(id: Int) {
        db.tasksDao().deleteTask(id)
    }

    private suspend fun hideTask(id: Int) {
        db.tasksDao().hideTask(id)
    }

    suspend fun setTaskCompleted(id: Int, dateCompleted: String) {
        db.tasksDao().setTaskCompleted(id, dateCompleted)
    }

    suspend fun setTaskUncompleted(id: Int) {
        db.tasksDao().setTaskUncompleted(id)
    }

    private suspend fun setTaskDelayed(id: Int) {
        db.tasksDao().setTaskDelayed(id)
    }
}