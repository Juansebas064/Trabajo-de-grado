package com.brightbox.hourglass.usecases

import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.model.HabitsModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HabitsUseCase @Inject constructor(
    private val db: HourglassDatabase
) {
    fun getTodayHabits(): Flow<List<HabitsModel>> =
        db.habitsDao().getHabits()


    suspend fun upsertHabit(habit: HabitsModel) {
        db.habitsDao().upsertHabit(habit)
    }

    suspend fun deleteHabit(id: Int) {
        db.habitsDao().deleteHabit(id)
    }
}
