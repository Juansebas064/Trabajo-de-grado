package com.brightbox.dino.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.dino.constants.PrioritiesEnum
import com.brightbox.dino.events.TasksEvent
import com.brightbox.dino.model.TasksModel
import com.brightbox.dino.states.TasksState
import com.brightbox.dino.usecases.TasksUseCase
import com.brightbox.dino.utils.formatMillisecondsToSQLiteDate
import com.brightbox.dino.utils.formatSQLiteDateToMilliseconds
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TasksViewModel @Inject constructor(
    private val _tasksUseCase: TasksUseCase,
    @ApplicationContext private val application: Context
) : ViewModel() {

    private val updateTasksReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // El Worker envió el evento, actualizar la lista de tareas
            validateCurrentTasksOnMidnight()
        }
    }

    init {
        // Registrar un broadcast receiver para recibir el evento del Worker
        val filter = IntentFilter("UPDATE_TASKS")
        ContextCompat.registerReceiver(
            application,
            updateTasksReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    private val _tasksList = MutableStateFlow(emptyList<TasksModel>())

    private val _state = MutableStateFlow(TasksState())
    val state = combine(_state, _tasksList) { state, tasks ->
        state.copy(
            tasks = tasks
        )
    }.onStart {
        loadTasksList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TasksState())

    private val _selectedTasks = MutableStateFlow(emptyList<Int>()) // Will store id's

    val selectedTasks = _selectedTasks.asStateFlow()

    private fun loadTasksList() {
        _tasksUseCase.getTasks()
            .onEach { tasks ->
                _tasksList.value = tasks
            }.launchIn(viewModelScope)
    }

    private fun validateCurrentTasksOnMidnight() {
        viewModelScope.launch {
            _tasksUseCase.validateCurrentTasksOnMidnight(
                formatMillisecondsToSQLiteDate(System.currentTimeMillis() - 86400000),
                state.value.tasks
            )
        }
    }

    private fun clearDialogFields() {
        _state.update {
            it.copy(
                isAddingTask = false,
                isEditingTask = false,
                taskId = null,
                taskTitle = "",
                taskDescription = "",
                taskDateCreated = null,
                taskDueDate = 0,
                wasTaskDelayed = false,
                taskCategory = null,
                taskPriority = PrioritiesEnum.High,
                taskVisible = true
            )
        }
    }

    fun onEvent(event: TasksEvent) {
        when (event) {
            TasksEvent.HideDeleteTasksDialog -> {
                _state.update {
                    it.copy(
                        isDeletingTasks = false,
                    )
                }

                _selectedTasks.update {
                    emptyList()
                }

            }

            TasksEvent.ShowDeleteTasksDialog -> {
                _state.update {
                    it.copy(
                        isDeletingTasks = true
                    )
                }
            }

            is TasksEvent.HideAddTaskDialog -> {
                _state.update {
                    it.copy(
                        isAddingTask = false
                    )
                }
            }

            is TasksEvent.ShowAddTaskDialog -> {
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
                val taskDateCreated = state.value.taskDateCreated ?: formatMillisecondsToSQLiteDate(System.currentTimeMillis())
                val taskDueDate =
                    if (state.value.taskDueDate == 0L) "" else formatMillisecondsToSQLiteDate(state.value.taskDueDate)
                val taskCategory = state.value.taskCategory
                val taskPriority = state.value.taskPriority
                val wasTaskDelayed = state.value.wasTaskDelayed
                val taskVisible = state.value.taskVisible

                if (
                    taskTitle.isBlank()
                ) {
                    return
                }
                val task = TasksModel(
                    id = taskId,
                    title = taskTitle,
                    description = taskDescription,
                    dateDue = taskDueDate,
                    isCompleted = false,
                    wasDelayed = wasTaskDelayed,
                    categoryId = taskCategory,
                    priority = taskPriority.priority,
                    dateCreated = taskDateCreated,
                    dateCompleted = null,
                    visible = taskVisible
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
                        isEditingTask = true,
                        taskId = task!!.id,
                        taskTitle = task.title,
                        taskDescription = task.description,
                        taskDateCreated = task.dateCreated.toString(),
                        taskDueDate = if (task.dateDue!!.isEmpty()) 0L else formatSQLiteDateToMilliseconds(
                            task.dateDue
                        ),
                        wasTaskDelayed = task.wasDelayed,
                        taskPriority = PrioritiesEnum.valueOf(task.priority),
                        taskCategory = task.categoryId,
                        taskVisible = task.visible
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
                        taskPriority = PrioritiesEnum.valueOf(event.priority)
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
                            formatMillisecondsToSQLiteDate(System.currentTimeMillis())
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