package com.brightbox.hourglass.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.hourglass.model.TasksModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC")
    fun getTasks(): Flow<List<TasksModel>>

    @Upsert
    suspend fun upsertTask(task: TasksModel)

    @Delete
    suspend fun deleteTask(task: TasksModel)

    @Query("UPDATE tasks SET isCompleted = 1 WHERE id = :id")
    suspend fun setTaskCompleted(id: Int)

    @Query("UPDATE tasks SET isCompleted = 0 WHERE id = :id")
    suspend fun setTaskUncompleted(id: Int)
}