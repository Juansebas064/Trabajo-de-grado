package com.brightbox.hourglass.states

import com.brightbox.hourglass.constants.PrioritiesEnum
import com.brightbox.hourglass.model.TasksModel

data class TasksState(
    val tasks: List<TasksModel> = emptyList(),
    val taskTitle: String = "",
    val taskDescription: String = "",
    val taskDueDate: Long = 0,
    val taskCategory: Int? = null,
    val taskPriority: String = PrioritiesEnum.HIGH.priority,
    val isAddingTask: Boolean = false
)
