package com.brightbox.dino.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.dino.events.CategoriesEvent
import com.brightbox.dino.model.CategoriesModel
import com.brightbox.dino.model.states.CategoriesState
import com.brightbox.dino.model.usecases.CategoriesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val _categoriesUseCase: CategoriesUseCase
): ViewModel() {
    private val _categoriesList = _categoriesUseCase.getCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _state = MutableStateFlow(CategoriesState())

    val state = combine(_state, _categoriesList) { state, categories ->
        state.copy(
            categories = categories
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CategoriesState())

    private fun clearDialogFields() {
        _state.value = _state.value.copy(
            isAddingCategory = false,
            isDeletingCategory = false,
            categoryId = null,
            categoryName = ""
        )
    }

    fun onEvent(event: CategoriesEvent) {
        when (event) {
            CategoriesEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingCategory = false
                    )
                }
            }

            CategoriesEvent.SaveCategory -> {
                val categoryName = state.value.categoryName
                if (categoryName.isBlank()) {
                    return
                }

                val category = CategoriesModel(
                    name = categoryName
                )

                viewModelScope.launch {
                    _categoriesUseCase.upsertCategory(category)
                }

                clearDialogFields()
            }

            is CategoriesEvent.SetCategoryName -> {
                _state.update {
                    it.copy(
                        categoryName = event.categoryName
                    )
                }
            }

            CategoriesEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingCategory = true
                    )
                }
            }

            CategoriesEvent.ShowDeleteDialog -> {
                _state.update {
                    it.copy(
                        isDeletingCategory = true
                    )
                }
            }

            CategoriesEvent.HideDeleteDialog -> {
                _state.update {
                    it.copy(
                        isDeletingCategory = false
                    )
                }
            }

            CategoriesEvent.ClearDialogFields -> {
                clearDialogFields()
            }

            is CategoriesEvent.DeleteCategory -> {
                viewModelScope.launch {
                    _categoriesUseCase.deleteCategory(_state.value.categoryId!!)
                }
                clearDialogFields()
            }

            is CategoriesEvent.SetCategoryId -> {
                _state.update {
                    it.copy(
                        categoryId = event.categoryId
                    )
                }
            }
        }

    }
}