package com.brightbox.dino.model.states

import com.brightbox.dino.model.CategoriesModel

data class CategoriesState(
    val categories: List<CategoriesModel> = emptyList(),
    val categoryId: Int? = null,
    val categoryName: String = "",
    val isAddingCategory: Boolean = false,
    val isDeletingCategory: Boolean = false,
)