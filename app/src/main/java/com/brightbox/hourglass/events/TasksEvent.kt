package com.brightbox.hourglass.events

import com.brightbox.hourglass.model.TasksModel

sealed interface TasksEvent {
    data object SaveTask: TasksEvent
    data class SetTaskTitle(val title: String): TasksEvent
    data class SetTaskDescription(val description: String): TasksEvent
    data class SetTaskDueDate(val dueDate: Long): TasksEvent
    data class SetTaskCategory(val categoryId: Int?): TasksEvent
    data class SetTaskPriority(val priority: String): TasksEvent
    data class DeleteTask(val task: TasksModel): TasksEvent
    class SetTaskCompleted(val id: Int?): TasksEvent
    class SetTaskUncompleted(val id: Int?): TasksEvent

    data object ClearDialogFields: TasksEvent

    data object ShowDialog: TasksEvent
    data object HideDialog: TasksEvent
}