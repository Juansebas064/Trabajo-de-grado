package com.brightbox.dino.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.dino.events.HabitsEvent
import com.brightbox.dino.model.HabitsLogsModel
import com.brightbox.dino.model.HabitsModel
import com.brightbox.dino.model.states.HabitsState
import com.brightbox.dino.model.usecases.HabitsLogsUseCase
import com.brightbox.dino.model.usecases.HabitsUseCase
import com.brightbox.dino.utils.formatMillisecondsToDay
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
class HabitsViewModel @Inject constructor(
    private val _habitsUseCase: HabitsUseCase,
    private val _habitsLogsUseCase: HabitsLogsUseCase,
    @ApplicationContext private val application: Context
) : ViewModel() {

    private val updateHabitsReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // El Worker envió el evento, actualizar la lista de tareas
            validateTodayHabitsOnMidnight()
        }
    }

    init {
        val filter = IntentFilter("UPDATE_TASKS")
        ContextCompat.registerReceiver(
            application,
            updateHabitsReceiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    private val _habitsList = MutableStateFlow(emptyList<HabitsModel>())

    private val _state = MutableStateFlow(HabitsState())

    val state = combine(_state, _habitsList) { state, habits ->
        state.copy(
            habits = habits,
        )
    }.onStart {
        loadTodayHabitsList()
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), HabitsState())

    private val _selectedHabits = MutableStateFlow(emptyList<Int>()) // Will store id's

    val selectedHabits = _selectedHabits.asStateFlow()

    fun validateTodayHabitsOnMidnight() {
        viewModelScope.launch {
            val yesterdayTime = System.currentTimeMillis() - 86400000
            _habitsLogsUseCase.validateTodayHabitsLogsOnMidnight(
                formatMillisecondsToSQLiteDate(yesterdayTime),
                formatMillisecondsToDay(yesterdayTime)
            )
            _habitsUseCase.validateHabitsOnMidnight(_habitsList.value)
        }
    }

    private fun loadTodayHabitsList() {
        _habitsUseCase.getTodayHabits()
            .onEach { habits ->
                _habitsList.value = habits
            }.launchIn(viewModelScope)
    }

    val habitsLogs = _habitsLogsUseCase.getHabitsLogs()
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun upsertHabitLog(habitId: Int, habitDate: String) {
        val habitLog = HabitsLogsModel(
            id = null,
            habitId = habitId,
            date = habitDate,
            completed = true
        )
        viewModelScope.launch {
            _habitsLogsUseCase.upsertHabitLog(habitLog)
        }
    }

    fun setHabitLogCompleted(id: Int) {
        viewModelScope.launch {
            _habitsLogsUseCase.setHabitLogCompleted(id)
            _habitsUseCase.setHabitCompleted(id)
        }
    }

    fun setHabitLogUncompleted(id: Int) {
        viewModelScope.launch {
            _habitsLogsUseCase.setHabitLogUncompleted(id)
            _habitsUseCase.setHabitUncompleted(id)
        }
    }

    private fun clearDialogFields() {
        _state.value = _state.value.copy(
            habitId = -1,
            habitTitle = "",
            habitCategory = null,
            habitDaysOfWeek = emptyList(),
            startDate = 0L,
            endDate = 0L,
            isAddingHabit = false,
            isEditingHabit = false
        )
    }

    fun onEvent(event: HabitsEvent) {
        when (event) {
            is HabitsEvent.SetHabitId -> {
                _state.value = _state.value.copy(
                    habitId = event.id
                )
            }

            is HabitsEvent.SetHabitTitle -> {
                _state.value = _state.value.copy(
                    habitTitle = event.title
                )
            }

            is HabitsEvent.SetHabitCategory -> {
                _state.value = _state.value.copy(
                    habitCategory = event.categoryId
                )
            }

            is HabitsEvent.SetHabitDaysOfWeek -> {
                _state.value = _state.value.copy(
                    habitDaysOfWeek = event.days
                )
            }

            is HabitsEvent.SetStartDate -> {
                _state.value = _state.value.copy(
                    startDate = event.date
                )
            }

            is HabitsEvent.SetEndDate -> {
                _state.value = _state.value.copy(
                    endDate = event.date
                )
            }

            HabitsEvent.SaveHabit -> {
                val habitId = state.value.habitId
                val habitName = state.value.habitTitle
                val habitCategory = state.value.habitCategory
                val habitDaysOfWeek = state.value.habitDaysOfWeek
                val habitStartDate =
                    if (state.value.startDate == 0L) "" else formatMillisecondsToSQLiteDate(state.value.startDate)
                val habitEndDate =
                    if (state.value.endDate == 0L) null else formatMillisecondsToSQLiteDate(state.value.endDate)
                val habitDeleted = false

                if (
                    habitName.isBlank() ||
                    habitDaysOfWeek.isEmpty() ||
                    habitStartDate.isEmpty()
                ) {
                    return
                }

                val habit = HabitsModel(
                    id = habitId,
                    title = habitName,
                    categoryId = habitCategory,
                    daysOfWeek = habitDaysOfWeek.joinToString(","),
                    startDate = habitStartDate,
                    endDate = habitEndDate,
                    deleted = habitDeleted
                )

                viewModelScope.launch {
                    _habitsUseCase.upsertHabit(habit)
                }

                clearDialogFields()
            }

            HabitsEvent.DeleteHabits -> {
                viewModelScope.launch {
                    selectedHabits.value.forEach {
                        _habitsUseCase.deleteHabit(it)
                    }
                }
                _selectedHabits.update {
                    emptyList()
                }
            }

            is HabitsEvent.EditHabit -> {
                _selectedHabits.update {
                    emptyList()
                }
                val habit = state.value.habits.find { it.id == event.id }
                _state.update {
                    it.copy(
                        habitId = habit!!.id,
                        habitTitle = habit.title,
                        habitCategory = habit.categoryId,
                        habitDaysOfWeek = habit.daysOfWeek.split(","),
                        startDate = formatSQLiteDateToMilliseconds(habit.startDate),
                        endDate = if (habit.endDate.isNullOrEmpty()) 0L else formatSQLiteDateToMilliseconds(
                            habit.endDate
                        ),
                        isAddingHabit = true,
                        isEditingHabit = true
                    )
                }
            }

            is HabitsEvent.MarkHabitSelected -> {
                _selectedHabits.update {
                    it + event.habitId
                }
            }

            is HabitsEvent.UnmarkHabitSelected -> {
                _selectedHabits.update {
                    it - event.habitId
                }
            }

            is HabitsEvent.ClearDialogFields -> {
                clearDialogFields()
            }

            HabitsEvent.HideAddHabitDialog -> {
                _state.value = _state.value.copy(
                    isAddingHabit = false
                )
            }

            HabitsEvent.ShowAddHabitDialog -> {
                _state.value = _state.value.copy(
                    isAddingHabit = true
                )
            }

            HabitsEvent.HideDeleteHabitDialog -> {
                _state.value = _state.value.copy(
                    isDeletingHabits = false
                )
                _selectedHabits.update {
                        emptyList()
                    }
            }

            HabitsEvent.ShowDeleteHabitDialog -> {
                _state.value = _state.value.copy(
                    isDeletingHabits = true
                )
            }
        }
    }
}
