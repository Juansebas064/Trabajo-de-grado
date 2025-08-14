package com.brightbox.dino.usecases

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import com.brightbox.dino.config.DinoDatabase
import com.brightbox.dino.model.ApplicationsModel
import com.brightbox.dino.model.constants.SearchEnginesEnum
import com.brightbox.dino.model.data.ApplicationsDao
import com.brightbox.dino.model.usecases.ApplicationsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

@ExperimentalCoroutinesApi
class ApplicationsUseCaseTest {

    private val mockDb: DinoDatabase = mockk()
    private val mockContext: Context = mockk()
    private val mockPackageManager: PackageManager = mockk()
    private val mockDao: ApplicationsDao = mockk()

    init {
        every { mockContext.packageManager } returns mockPackageManager
        every { mockDb.applicationsDao() } returns mockDao
    }

    @Test
    fun `getApplicationsFromDatabase should return flow from DAO`() = runTest {
        val apps = listOf(
            ApplicationsModel(
                packageName = "com.a", name = "A",
                isPinned = false,
                limitTimeReached = false
            ),
            ApplicationsModel(
                packageName = "com.b", name = "B",
                isPinned = false,
                limitTimeReached = false
            ),
            ApplicationsModel(
                packageName = "com.c", name = "C",
                isPinned = false,
                limitTimeReached = false
            )
        )

        every { mockDao.getApplications() } returns flowOf(apps)

        val useCase = ApplicationsUseCase(mockDb, mockContext)
        val result = useCase.getApplicationsFromDatabase().first()

        assertEquals(listOf("A", "B", "C"), result.map { it.name })
    }

    @Test
    fun `toggleAppPinnedState should update database`() = runTest {
        val app = ApplicationsModel(
            packageName = "com.example.app", isPinned = false,
            name = "example",
            limitTimeReached = false
        )
        coEvery { mockDao.upsertApplication(any()) } returns Unit

        val useCase = ApplicationsUseCase(mockDb, mockContext)
        useCase.toggleAppPinnedState(app)

        coVerify {
            mockDao.upsertApplication(app.copy(isPinned = true))
        }
    }


}