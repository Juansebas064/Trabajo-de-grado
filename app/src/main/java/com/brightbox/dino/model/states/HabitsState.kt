package com.brightbox.dino.model.states

import com.brightbox.dino.model.HabitsModel

data class HabitsState (
    val habits: List<HabitsModel> = emptyList(),
    val habitId: Int? = null,
    val habitTitle: String = "",
    val habitCategory: Int? = null,
    val habitDaysOfWeek: List<String> = emptyList(),
    val startDate: Long = 0L,
    val endDate: Long = 0L,

    val isAddingHabit: Boolean = false,
    val isDeletingHabits: Boolean = false,
    val isEditingHabit: Boolean = false
)
