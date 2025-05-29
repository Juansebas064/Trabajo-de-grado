package com.brightbox.dino.usecases

import com.brightbox.dino.config.DinoDatabase
import com.brightbox.dino.model.CategoriesModel
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(
    private val db: DinoDatabase
) {
    fun getCategories() = db.categoriesDao().getCategories()

    suspend fun upsertCategory(category: CategoriesModel) {
        db.categoriesDao().upsertCategory(category)
    }

    suspend fun deleteCategory(id: Int) {
        db.categoriesDao().deleteCategory(id)
    }
}