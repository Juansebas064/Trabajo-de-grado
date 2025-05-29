package com.brightbox.dino.states

import com.brightbox.dino.model.CategoriesModel

data class CategoriesState(
    val categories: List<CategoriesModel> = emptyList(),
    val categoryName: String = "",
    val isAddingCategory: Boolean = false
)