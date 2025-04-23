package com.brightbox.hourglass.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.hourglass.constants.PrioritiesEnum
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.model.TasksModel
import com.brightbox.hourglass.states.TasksState
import com.brightbox.hourglass.usecases.TasksUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val _tasksUseCase: TasksUseCase,
) : ViewModel() {

    private val _tasksList = _tasksUseCase.getTasks()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _state = MutableStateFlow(TasksState())

    val state = combine(_state, _tasksList) { state, tasks ->
        state.copy(
            tasks = tasks
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TasksState())

    private fun formatDate(date: Long): String {
        val localDate = Instant.ofEpochMilli(date)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()

        val formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy")
        return localDate.format(formatter)
    }

    private fun clearDialogFields() {
        _state.update {
            it.copy(
                isAddingTask = false,
                taskTitle = "",
                taskDescription = "",
                taskDueDate = 0,
                taskCategory = null,
                taskPriority = PrioritiesEnum.HIGH.priority
            )
        }
    }

    fun onEvent(event: TasksEvent) {
        when (event) {
            is TasksEvent.DeleteTask -> {
                viewModelScope.launch {
                    _tasksUseCase.deleteTask(event.task)
                }
            }

            is TasksEvent.HideDialog -> {
                _state.update {
                    it.copy(
                        isAddingTask = false
                    )
                }
            }

            is TasksEvent.ShowDialog -> {
                _state.update {
                    it.copy(
                        isAddingTask = true
                    )
                }
            }

            is TasksEvent.SetTaskTitle -> {
                _state.update {
                    it.copy(
                        taskTitle = event.title
                    )
                }
            }

            is TasksEvent.SetTaskDescription -> {
                _state.update {
                    it.copy(
                        taskDescription = event.description
                    )
                }
            }

            is TasksEvent.SetTaskCategory -> {
                _state.update {
                    it.copy(
                        taskCategory = event.categoryId
                    )
                }
            }

            is TasksEvent.SetTaskDueDate -> {
                _state.update {
                    it.copy(
                        taskDueDate = event.dueDate
                    )
                }
            }

            is TasksEvent.SetTaskPriority -> {
                _state.update {
                    it.copy(
                        taskPriority = event.priority
                    )
                }
            }

            is TasksEvent.SaveTask -> {
                val taskTitle = state.value.taskTitle
                val taskDescription = state.value.taskDescription
                val taskDateCreated = formatDate(System.currentTimeMillis())
                val taskDueDate = formatDate(state.value.taskDueDate)
                val taskCategory = state.value.taskCategory
                val taskPriority = state.value.taskPriority

                if (
                    taskTitle.isBlank()
                    || taskDescription.isBlank()
                    || taskPriority.isBlank()
                ) {
                    return
                }
                val task = TasksModel(
                    title = taskTitle,
                    description = taskDescription,
                    dateDue = taskDueDate,
                    isCompleted = false,
                    wasDelayed = false,
                    categoryId = taskCategory,
                    priority = taskPriority,
                    dateCreated = taskDateCreated,
                    dateCompleted = null,
                )

                viewModelScope.launch {
                    _tasksUseCase.upsertTask(task)
                }
            }

            is TasksEvent.ClearDialogFields -> {
                clearDialogFields()
            }

            is TasksEvent.SetTaskCompleted -> {
                viewModelScope.launch {
                    event.id?.let { _tasksUseCase.setTaskCompleted(it) }
                }
            }
            is TasksEvent.SetTaskUncompleted -> {
                viewModelScope.launch {
                    event.id?.let { _tasksUseCase.setTaskUncompleted(it) }
                }
            }
        }
    }
}