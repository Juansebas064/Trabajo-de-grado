package com.brightbox.hourglass.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.hourglass.model.TasksModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks ORDER BY priority")
    fun getTasks(): Flow<List<TasksModel>>

    @Upsert
    suspend fun upsertTask(task: TasksModel)

    @Delete
    suspend fun deleteTask(task: TasksModel)
}