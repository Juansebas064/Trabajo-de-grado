package com.brightbox.dino.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.dino.model.LimitsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface LimitsDao {

    @Query("SELECT * FROM limits")
    fun getLimits(): Flow<List<LimitsModel>>

    @Upsert
    suspend fun upsertLimit(limit: LimitsModel)

    @Query("UPDATE limits SET usedTime = :usedTime WHERE id = :id")
    suspend fun updateUsedTime(id: Int, usedTime: Int)

    @Query("DELETE FROM limits WHERE id = :id")
    suspend fun deleteLimit(id: Int)

    @Query("SELECT * FROM limits WHERE id = :id")
    fun getLimitById(id: Int): LimitsModel
}