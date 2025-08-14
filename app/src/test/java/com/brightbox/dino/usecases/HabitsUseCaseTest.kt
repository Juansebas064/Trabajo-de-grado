package com.brightbox.dino.usecases

import com.brightbox.dino.config.DinoDatabase
import com.brightbox.dino.model.HabitsModel
import com.brightbox.dino.model.data.HabitsDao
import com.brightbox.dino.model.usecases.HabitsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class HabitsUseCaseTest {

    private val mockDb: DinoDatabase = mockk()
    private val mockDao: HabitsDao = mockk()

    init {
        every { mockDb.habitsDao() } returns mockDao
    }

    @Test
    fun `validateHabitsOnMidnight should reset completions`() = runTest {
        val habits = listOf(
            HabitsModel(
                id = 1,
                completedForToday = true,
                title = "habit",
                categoryId = null,
                daysOfWeek = "monday",
                startDate = "2025/06/20",
                endDate = null,
                deleted = false
            ),
            HabitsModel(
                id = 2,
                completedForToday = true,
                title = "habit 2",
                categoryId = null,
                daysOfWeek = "tuesday",
                startDate = "2025/06/21",
                endDate = null,
                deleted = false
            ),
        )

        coEvery { mockDao.upsertHabit(any()) } returns Unit

        val useCase = HabitsUseCase(mockDb)
        useCase.validateHabitsOnMidnight(habits)

        coVerify(exactly = 2) {
            mockDao.upsertHabit(match { !it.completedForToday })
        }
    }

    @Test
    fun `setHabitCompleted should call DAO`() = runTest {
        val id = 1
        coEvery { mockDao.setHabitCompleted(id) } returns Unit

        val useCase = HabitsUseCase(mockDb)
        useCase.setHabitCompleted(id)

        coVerify { mockDao.setHabitCompleted(id) }
    }

    @Test
    fun `upsertHabit should call DAO`() = runTest {
        val habit = HabitsModel(
            id = 1,
            completedForToday = true,
            title = "habit",
            categoryId = null,
            daysOfWeek = "monday",
            startDate = "2025/06/20",
            endDate = null,
            deleted = false
        )
        coEvery { mockDao.upsertHabit(habit) } returns Unit

        val useCase = HabitsUseCase(mockDb)
        useCase.upsertHabit(habit)

        coVerify { mockDao.upsertHabit(habit) }
    }

    @Test
    fun `deleteHabit should call DAO`() = runTest {
        val id = 1
        coEvery { mockDao.deleteHabit(id) } returns Unit

        val useCase = HabitsUseCase(mockDb)
        useCase.deleteHabit(id)

        coVerify { mockDao.deleteHabit(id) }
    }
}