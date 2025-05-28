package com.brightbox.hourglass.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import com.brightbox.hourglass.model.LimitsModel
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

}