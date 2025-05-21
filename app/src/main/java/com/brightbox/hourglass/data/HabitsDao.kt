package com.brightbox.hourglass.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.hourglass.model.HabitsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitsDao {
    @Query("SELECT * FROM habits WHERE deleted = 0")
    fun getHabits(): Flow<List<HabitsModel>>

    @Upsert
    suspend fun upsertHabit(habit: HabitsModel)

    @Query("UPDATE habits SET deleted = 1 WHERE id = :id")
    suspend fun deleteHabit(id: Int)
}