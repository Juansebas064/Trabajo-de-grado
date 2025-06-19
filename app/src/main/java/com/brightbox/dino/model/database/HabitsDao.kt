package com.brightbox.dino.model.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.dino.model.HabitsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {
    @Query("SELECT * FROM habits WHERE deleted = 0 ORDER BY completedForToday")
    fun getHabits(): Flow<List<HabitsModel>>

    @Upsert
    suspend fun upsertHabit(habit: HabitsModel)

    @Query("UPDATE habits SET completedForToday = 1 WHERE id = :id")
    suspend fun setHabitCompleted(id: Int)

    @Query("UPDATE habits SET completedForToday = 0 WHERE id = :id")
    suspend fun setHabitUncompleted(id: Int)

    @Query("UPDATE habits SET deleted = 1 WHERE id = :id")
    suspend fun deleteHabit(id: Int)
}