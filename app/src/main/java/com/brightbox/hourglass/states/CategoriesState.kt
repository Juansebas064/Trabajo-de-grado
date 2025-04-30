package com.brightbox.hourglass.states

import com.brightbox.hourglass.model.CategoriesModel

data class CategoriesState(
    val categories: List<CategoriesModel> = emptyList(),
    val categoryName: String = "",
    val isAddingCategory: Boolean = false
)