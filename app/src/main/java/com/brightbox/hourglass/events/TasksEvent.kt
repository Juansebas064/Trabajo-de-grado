package com.brightbox.hourglass.events

import com.brightbox.hourglass.model.TasksModel

sealed interface TasksEvent {
    object SaveTask: TasksEvent
    data class SetTaskTitle(val title: String): TasksEvent
    data class SetTaskDescription(val description: String): TasksEvent
    data class SetTaskDueDate(val dueDate: String): TasksEvent
    data class SetTaskCategory(val categoryId: Int): TasksEvent
    data class SetTaskPriority(val priority: Int): TasksEvent
    data class DeleteTask(val task: TasksModel): TasksEvent

    object ShowDialog: TasksEvent
    object HideDialog: TasksEvent
}