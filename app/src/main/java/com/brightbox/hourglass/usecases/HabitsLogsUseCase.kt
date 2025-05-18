package com.brightbox.hourglass.usecases

import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.model.HabitsLogsModel
import com.brightbox.hourglass.model.HabitsModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

class HabitsLogsUseCase @Inject constructor(
    private val db: HourglassDatabase
) {
    fun getHabitsLogs(): Flow<List<HabitsLogsModel>> =
        db.habitsLogsDao().getHabitsLogs()

    suspend fun upsertHabitLog(habitLog: HabitsLogsModel) =
        db.habitsLogsDao().upsertHabitLog(habitLog)

    suspend fun setHabitLogCompleted(id: Int) =
        db.habitsLogsDao().setHabitLogCompleted(id)

    suspend fun setHabitLogUncompleted(id: Int) =
        db.habitsLogsDao().setHabitLogUncompleted(id)

    suspend fun deleteHabitLog(id: Int) =
        db.habitsLogsDao().deleteHabitLog(id)

    suspend fun validateTodayHabitsOnMidnight(yesterdayDate: String, yesterdayDayOfWeek: String) {
        var habits: List<HabitsModel> = emptyList()
        var habitsLogs: List<HabitsLogsModel> = emptyList()

        db.habitsDao().getHabits().collectLatest { habitsList ->
            habits = habitsList.filter {
                yesterdayDayOfWeek in it.daysOfWeek
            }
        }

        db.habitsLogsDao().getHabitsLogs().collectLatest { habitsLogsList ->
            habitsLogs = habitsLogsList.filter { 
                it.date == yesterdayDate
            }
        }

        habits.forEach { habit ->
            if (habitsLogs.none { it.habitId == habit.id }) {
                val habitLog = HabitsLogsModel(
                    id = null,
                    habitId = habit.id!!,
                    date = yesterdayDate,
                    completed = false,
                )
                upsertHabitLog(habitLog)
            }
        }
    }
}