package com.brightbox.hourglass.usecases

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetInstalledApplicationsUseCaseTest{

    @RelaxedMockK
    private lateinit var mockContext: Context

    private lateinit var mockPackageManager: PackageManager

    private lateinit var getInstalledApplicationsUseCase: GetInstalledApplicationsUseCase

    @Before
    fun onBefore() {
        MockKAnnotations.init(this) // Inicializa MockK
        every { mockContext.packageManager } returns mockPackageManager // Mockea el PackageManager
        getInstalledApplicationsUseCase = GetInstalledApplicationsUseCase(mockContext)
    }

    @Test
    fun getInstalledApplications() = runBlocking {
        //Given

        //When

        //Then
    }
}