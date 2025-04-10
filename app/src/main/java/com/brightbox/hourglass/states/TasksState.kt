package com.brightbox.hourglass.states

import com.brightbox.hourglass.model.TasksModel

data class TasksState(
    val tasks: List<TasksModel> = emptyList(),
    val taskTitle: String = "",
    val taskDescription: String = "",
    val taskDueDate: String = "",
    val taskCategory: Int? = null,
    val taskPriority: Int = 1,
    val isAddingTask: Boolean = false
)
