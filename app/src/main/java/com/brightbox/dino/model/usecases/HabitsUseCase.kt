package com.brightbox.dino.model.usecases

import com.brightbox.dino.config.DinoDatabase
import com.brightbox.dino.model.HabitsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HabitsUseCase @Inject constructor(
    private val db: DinoDatabase
) {
    fun getTodayHabits(): Flow<List<HabitsModel>> =
        db.habitsDao().getHabits()

    suspend fun validateHabitsOnMidnight(habits: List<HabitsModel>) {
        withContext(Dispatchers.IO) {
            habits.forEach { habit ->
                val habitUpdated = habit.copy(completedForToday = false)
                upsertHabit(habitUpdated)
            }
        }
    }

    suspend fun upsertHabit(habit: HabitsModel) {
        withContext(Dispatchers.IO) {
            db.habitsDao().upsertHabit(habit)
        }
    }

    suspend fun setHabitCompleted(id: Int) {
        db.habitsDao().setHabitCompleted(id)
    }

    suspend fun setHabitUncompleted(id: Int) {
        db.habitsDao().setHabitUncompleted(id)
    }

    suspend fun deleteHabit(id: Int) {
        db.habitsDao().deleteHabit(id)
    }
}
