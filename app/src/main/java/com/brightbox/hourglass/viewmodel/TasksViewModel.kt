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
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val _tasksUseCase: TasksUseCase,
) : ViewModel() {

    private val _tasksList = _tasksUseCase.getTasks(formatMillisToDate(System.currentTimeMillis()))
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _state = MutableStateFlow(TasksState())

    val state = combine(_state, _tasksList) { state, tasks ->
        state.copy(
            tasks = tasks
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TasksState())

    private val _selectedTasks = MutableStateFlow(emptyList<Int>()) // Will store id's
    val selectedTasks = _selectedTasks.asStateFlow()

    // Format the date according to SQLite dates best practices
    fun formatMillisToDate(date: Long): String {
        val localDate = Instant.ofEpochMilli(date)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        return localDate.format(formatter)
    }

    fun formatDateToMillis(date: String): Long {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = formatter.parse(date)
        return formattedDate?.time ?: 0L
    }

    private fun clearDialogFields() {
        _state.update {
            it.copy(
                isAddingTask = false,
                taskId = null,
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

            is TasksEvent.DeleteTasks -> {
                viewModelScope.launch {
                    selectedTasks.value.forEach {
                        _tasksUseCase.deleteTask(it)
                    }
                }
                _selectedTasks.update {
                    emptyList()
                }
            }

            is TasksEvent.SaveTask -> {
                val taskId = state.value.taskId
                val taskTitle = state.value.taskTitle
                val taskDescription = state.value.taskDescription
                val taskDateCreated = formatMillisToDate(System.currentTimeMillis())
                val taskDueDate =
                    if (state.value.taskDueDate == 0L) "" else formatMillisToDate(state.value.taskDueDate)
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
                    id = taskId,
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
                clearDialogFields()
            }

            is TasksEvent.EditTask -> {

                _selectedTasks.update {
                    emptyList()
                }

                val task = state.value.tasks.find { it.id == event.id }

                _state.update {
                    it.copy(
                        isAddingTask = true,
                        taskId = task!!.id,
                        taskTitle = task.title,
                        taskDescription = task.description,
                        taskDueDate = if (task.dateDue!!.isEmpty()) 0L else formatDateToMillis(task.dateDue),
                        taskPriority = task.priority,
                        taskCategory = task.categoryId,
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

            is TasksEvent.ClearDialogFields -> {
                clearDialogFields()
            }

            is TasksEvent.SetTaskCompleted -> {
                viewModelScope.launch {
                    event.id?.let {
                        _tasksUseCase.setTaskCompleted(
                            it,
                            formatMillisToDate(System.currentTimeMillis())
                        )
                    }
                }
            }

            is TasksEvent.SetTaskUncompleted -> {
                viewModelScope.launch {
                    event.id?.let { _tasksUseCase.setTaskUncompleted(it) }
                }
            }

            is TasksEvent.MarkTaskSelected -> {
                _selectedTasks.update {
                    it + event.id
                }
            }

            is TasksEvent.UnmarkTaskSelected -> {
                _selectedTasks.update {
                    it - event.id
                }
            }

        }
    }
}