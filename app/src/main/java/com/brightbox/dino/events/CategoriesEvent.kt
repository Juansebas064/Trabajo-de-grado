package com.brightbox.dino.events

sealed interface CategoriesEvent {
    data object ShowDialog: CategoriesEvent
    data object HideDialog: CategoriesEvent
    data class SetCategoryId(val categoryId: Int): CategoriesEvent
    data class SetCategoryName(val categoryName: String): CategoriesEvent
    data object SaveCategory: CategoriesEvent
    data object DeleteCategory: CategoriesEvent
    data object ClearDialogFields : CategoriesEvent
    data object ShowDeleteDialog: CategoriesEvent
    data object HideDeleteDialog: CategoriesEvent
}