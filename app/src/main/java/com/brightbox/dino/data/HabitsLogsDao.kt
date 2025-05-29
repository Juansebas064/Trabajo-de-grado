package com.brightbox.dino.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.dino.model.HabitsLogsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsLogsDao {
    @Query("SELECT * FROM habits_logs")
    fun getHabitsLogs(): Flow<List<HabitsLogsModel>>

    @Upsert
    suspend fun upsertHabitLog(habitLog: HabitsLogsModel)

    @Query("UPDATE habits_logs SET completed = 1 WHERE id = :id")
    suspend fun setHabitLogCompleted(id: Int)

    @Query("UPDATE habits_logs SET completed = 0 WHERE id = :id")
    suspend fun setHabitLogUncompleted(id: Int)

    @Query("DELETE FROM habits_logs WHERE id = :id")
    suspend fun deleteHabitLog(id: Int)
}