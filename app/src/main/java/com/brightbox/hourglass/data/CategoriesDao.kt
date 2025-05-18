package com.brightbox.hourglass.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.hourglass.model.CategoriesModel
import com.brightbox.hourglass.model.TasksModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoriesDao {
    @Query("SELECT * FROM categories WHERE deleted = 0")
    fun getCategories(): Flow<List<CategoriesModel>>

    @Upsert
    suspend fun upsertCategory(category: CategoriesModel)

    @Query("UPDATE categories SET deleted = 1 WHERE id = :id")
    suspend fun deleteCategory(id: Int)
}