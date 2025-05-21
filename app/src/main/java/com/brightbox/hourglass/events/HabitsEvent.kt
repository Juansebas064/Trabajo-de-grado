package com.brightbox.hourglass.events

sealed interface HabitsEvent {

    // Events related to adding or editing a habit
    data class SetHabitId(val id: Int) : HabitsEvent
    data class SetHabitTitle(val title: String) : HabitsEvent
    data class SetHabitCategory(val categoryId: Int?) : HabitsEvent
    data class SetHabitDaysOfWeek(val days: List<String>) : HabitsEvent
    data class SetStartDate(val date: Long) : HabitsEvent
    data class SetEndDate(val date: Long) : HabitsEvent

    // Events related to deleting habits
    data object SaveHabit : HabitsEvent
    data object DeleteHabits : HabitsEvent // Or a more specific event for confirming deletion
    data class EditHabit(val id: Int) : HabitsEvent // Or a more specific event for canceling deletion

    data class MarkHabitSelected(val habitId: Int) : HabitsEvent // If you need to select habits for deletion
    data class UnmarkHabitSelected(val habitId: Int) : HabitsEvent

    data object ShowAddHabitDialog : HabitsEvent
    data object HideAddHabitDialog : HabitsEvent

    // Other potential events
    data object ClearDialogFields : HabitsEvent // To clear the input fields for adding/editing
    // Add other events as needed based on user interactions or background processes
}