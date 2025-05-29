package com.brightbox.dino.data

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.dino.model.CategoriesModel
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