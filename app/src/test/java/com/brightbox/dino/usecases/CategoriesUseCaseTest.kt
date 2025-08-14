package com.brightbox.dino.usecases

import com.brightbox.dino.config.DinoDatabase
import com.brightbox.dino.model.CategoriesModel
import com.brightbox.dino.model.data.CategoriesDao
import com.brightbox.dino.model.usecases.CategoriesUseCase
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
class CategoriesUseCaseTest {

    private val mockDb: DinoDatabase = mockk()
    private val mockDao: CategoriesDao = mockk()

    init {
        every { mockDb.categoriesDao() } returns mockDao
    }

    @Test
    fun `getCategories should return flow from DAO`() = runTest {
        val categories = listOf(CategoriesModel(id = 1, name = "Test"))
        every { mockDao.getCategories() } returns flowOf(categories)

        val useCase = CategoriesUseCase(mockDb)
        val result = useCase.getCategories().first()

        assertEquals(categories, result)
    }

    @Test
    fun `upsertCategory should call DAO upsert`() = runTest {
        val category = CategoriesModel(id = 1, name = "Test")
        coEvery { mockDao.upsertCategory(category) } returns Unit

        val useCase = CategoriesUseCase(mockDb)
        useCase.upsertCategory(category)

        coVerify { mockDao.upsertCategory(category) }
    }

    @Test
    fun `deleteCategory should call DAO delete`() = runTest {
        val id = 1
        coEvery { mockDao.deleteCategory(id) } returns Unit

        val useCase = CategoriesUseCase(mockDb)
        useCase.deleteCategory(id)

        coVerify { mockDao.deleteCategory(id) }
    }
}