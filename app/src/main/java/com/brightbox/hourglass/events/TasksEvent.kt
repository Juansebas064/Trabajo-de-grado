package com.brightbox.hourglass.events

sealed interface TasksEvent {
    data class SetTaskTitle(val title: String): TasksEvent
    data class SetTaskDescription(val description: String): TasksEvent
    data class SetTaskDueDate(val dueDate: Long): TasksEvent
    data class SetTaskCategory(val categoryId: Int?): TasksEvent
    data class SetTaskPriority(val priority: String): TasksEvent
    class SetTaskCompleted(val id: Int?): TasksEvent
    class SetTaskUncompleted(val id: Int?): TasksEvent

    data class MarkTaskSelected(val id: Int): TasksEvent
    data class UnmarkTaskSelected(val id: Int): TasksEvent

    data object SaveTask: TasksEvent
    data object DeleteTasks: TasksEvent
    data class EditTask(val id: Int): TasksEvent
    data object LoadTasks: TasksEvent

    data object ClearDialogFields: TasksEvent

    data object ShowAddTaskDialog: TasksEvent
    data object HideAddTaskDialog: TasksEvent

    data object ShowDeleteTasksDialog: TasksEvent
    data object HideDeleteTasksDialog: TasksEvent
}