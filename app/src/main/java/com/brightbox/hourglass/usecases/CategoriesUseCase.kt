package com.brightbox.hourglass.usecases

import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.model.CategoriesModel
import javax.inject.Inject

class CategoriesUseCase @Inject constructor(
    private val db: HourglassDatabase
) {
    fun getCategories() = db.categoriesDao().getCategories()

    suspend fun upsertCategory(category: CategoriesModel) {
        db.categoriesDao().upsertCategory(category)
    }

    suspend fun deleteCategory(id: Int) {
        db.categoriesDao().deleteCategory(id)
    }
}