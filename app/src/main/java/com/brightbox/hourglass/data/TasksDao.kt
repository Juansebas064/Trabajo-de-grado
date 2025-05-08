package com.brightbox.hourglass.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.hourglass.model.TasksModel
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {
    @Query("SELECT * FROM tasks ORDER BY isCompleted ASC, dateDue ASC")
    fun getTasks(): Flow<List<TasksModel>>

    @Upsert
    suspend fun upsertTask(task: TasksModel)

    @Query("DELETE FROM tasks WHERE id = :id")
    suspend fun deleteTask(id: Int)

    @Query("UPDATE tasks SET isCompleted = 1, dateCompleted = :dateCompleted WHERE id = :id")
    suspend fun setTaskCompleted(id: Int, dateCompleted: String?)

    @Query("UPDATE tasks SET isCompleted = 0, dateCompleted = NULL WHERE id = :id")
    suspend fun setTaskUncompleted(id: Int)

    @Query("UPDATE tasks SET wasDelayed = 1 WHERE id = :id")
    suspend fun setTaskDelayed(id: Int)
}