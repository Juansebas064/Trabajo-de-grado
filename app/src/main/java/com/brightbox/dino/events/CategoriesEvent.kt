package com.brightbox.dino.events

sealed interface CategoriesEvent {
    data object ShowDialog: CategoriesEvent
    data object HideDialog: CategoriesEvent
    data class SetCategoryName(val categoryName: String): CategoriesEvent
    data object SaveCategory: CategoriesEvent
    data class DeleteCategory(val categoryId: Int): CategoriesEvent
    data object ClearDialogFields : CategoriesEvent
}