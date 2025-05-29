package com.brightbox.dino.states

import com.brightbox.dino.constants.PrioritiesEnum
import com.brightbox.dino.model.TasksModel

data class TasksState(
    val tasks: List<TasksModel> = emptyList(),
    val taskId: Int? = null,
    val taskTitle: String = "",
    val taskDescription: String = "",
    val taskDueDate: Long = 0L,
    val wasTaskDelayed: Boolean = false,
    val taskCategory: Int? = null,
    val taskPriority: String = PrioritiesEnum.High.priority,

    val isAddingTask: Boolean = false,
    val isDeletingTasks: Boolean = false,
    val isEditingTask: Boolean = false
)
