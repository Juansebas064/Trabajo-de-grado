package com.brightbox.hourglass.usecases

import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.model.TasksModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksUseCase @Inject constructor(
    private val db: HourglassDatabase
) {
//    fun getTasks(date: String): Flow<List<TasksModel>> = flow {
//        while (true) {
//            val filteredTasks: MutableList<TasksModel> = mutableListOf()
//            CoroutineScope(Dispatchers.IO).launch {
//                val tasks = db.tasksDao().getTasks().collect { value ->
//                    value.forEach {
//                        if (it.isCompleted) {
//                            if (it.dateCompleted == date) {
//                                filteredTasks.add(it)
//                            }
//                        } else {
//                            filteredTasks.add(it)
//                        }
//                    }
//                }
//                emit(filteredTasks.toList())
//                delay(5000)
//            }
//        }
//    }

    fun getTasks(date: String) = db.tasksDao().getTasks()

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
}