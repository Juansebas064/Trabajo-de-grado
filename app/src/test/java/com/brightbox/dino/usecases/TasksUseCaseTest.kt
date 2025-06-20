package com.brightbox.dino.usecases

import com.brightbox.dino.config.DinoDatabase
import com.brightbox.dino.model.TasksModel
import com.brightbox.dino.model.constants.PrioritiesEnum
import com.brightbox.dino.model.data.TasksDao
import com.brightbox.dino.model.usecases.TasksUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class TasksUseCaseTest {

    private val mockDb: DinoDatabase = mockk()
    private val mockDao: TasksDao = mockk()

    init {
        every { mockDb.tasksDao() } returns mockDao
    }

    @Test
    fun `getTasks should sort correctly`() = runTest {
        val task1 = TasksModel(
            id = 1,
            isCompleted = false,
            dateDue = "2025-06-21",
            priority = PrioritiesEnum.Medium.name,
            title = "task 1",
            description = "",
            dateCreated = "2025-06-20",
            dateCompleted = "",
            wasDelayed = false,
            categoryId = null,
            visible = true
        )
        val task2 = TasksModel(
            id = 2,
            isCompleted = false,
            dateDue = "2025-06-21",
            priority = PrioritiesEnum.High.name,
            title = "task 2",
            description = "",
            dateCreated = "2025-06-20",
            dateCompleted = "",
            wasDelayed = false,
            categoryId = null,
            visible = true
        )
        val task3 = TasksModel(
            id = 3,
            isCompleted = false,
            dateDue = "2025-06-21",
            priority = PrioritiesEnum.Low.name,
            title = "task 3",
            description = "",
            dateCreated = "2025-06-20",
            dateCompleted = "",
            wasDelayed = false,
            categoryId = null,
            visible = true
        )

        every { mockDao.getTasks() } returns flowOf(listOf(task1, task2, task3))

        val useCase = TasksUseCase(mockDb)
        val result = useCase.getTasks().first()

        assertEquals(listOf(task2, task1, task3), result)
    }

    @Test
    fun `upsertTask should call DAO`() = runTest {
        val task = TasksModel(
            id = 1,
            isCompleted = false,
            dateDue = "2025-06-21",
            priority = PrioritiesEnum.Medium.name,
            title = "task 1",
            description = "",
            dateCreated = "2025-06-20",
            dateCompleted = "",
            wasDelayed = false,
            categoryId = null,
            visible = true
        )
        coEvery { mockDao.upsertTask(task) } returns Unit

        val useCase = TasksUseCase(mockDb)
        useCase.upsertTask(task)

        coVerify { mockDao.upsertTask(task) }
    }

    @Test
    fun `deleteTask should call DAO`() = runTest {
        val id = 1
        coEvery { mockDao.deleteTask(id) } returns Unit

        val useCase = TasksUseCase(mockDb)
        useCase.deleteTask(id)

        coVerify { mockDao.deleteTask(id) }
    }

    @Test
    fun `setTaskCompleted should update database`() = runTest {
        val id = 1
        val date = "2023-10-01"
        coEvery { mockDao.setTaskCompleted(id, date) } returns Unit

        val useCase = TasksUseCase(mockDb)
        useCase.setTaskCompleted(id, date)

        coVerify { mockDao.setTaskCompleted(id, date) }
    }
}