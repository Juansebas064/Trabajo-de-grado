package com.brightbox.hourglass.usecases

import android.util.Log
import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.constants.PrioritiesEnum
import com.brightbox.hourglass.model.TasksModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksUseCase @Inject constructor(
    private val db: HourglassDatabase
) {
    fun getTasks(): Flow<List<TasksModel>> =
        db.tasksDao().getTasks().map { taskList ->
            taskList.sortedBy {
                PrioritiesEnum.valueOf(it.priority).value
            }.sortedBy { it.dateDue }
        }

//    suspend fun getTodayTasksAtMidnight(date: String): List<TasksModel> {
//        var todayTaskList: List<TasksModel> = emptyList()
//        db.tasksDao().getTasks().map { tasks ->
//            tasks.filter { task ->
//                if (task.isCompleted) {
//                    task.dateCompleted == date
//                } else {
//                    true
//                }
//            }
//        }.collectLatest { taskList ->
//            todayTaskList = taskList
//        } // Use toList() to collect the flow into a list
//        return todayTaskList
//    }

    suspend fun validateCurrentTasksOnMidnight(
        previousDate: String,
        currentDate: String,
        tasks: List<TasksModel>
    ) {
        Log.d("TasksUseCase", "Yesterday: $previousDate, Today: $currentDate")
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