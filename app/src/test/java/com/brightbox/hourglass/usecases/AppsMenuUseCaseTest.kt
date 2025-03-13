//package com.brightbox.hourglass.usecases
//
//import android.content.Context
//import android.content.pm.PackageManager
//import io.mockk.MockKAnnotations
//import io.mockk.every
//import io.mockk.impl.annotations.RelaxedMockK
//import kotlinx.coroutines.runBlocking
//import org.junit.Before
//import org.junit.Test
//
//class AppsMenuUseCaseTest{
//
//    @RelaxedMockK
//    private lateinit var mockContext: Context
//
//    private lateinit var mockPackageManager: PackageManager
//
//    private lateinit var appsMenuUseCase: AppsMenuUseCase
//
//    @Before
//    fun onBefore() {
//        MockKAnnotations.init(this) // Inicializa MockK
//        every { mockContext.packageManager } returns mockPackageManager // Mockea el PackageManager
//        appsMenuUseCase = AppsMenuUseCase(mockContext)
//    }
//
//    @Test
//    fun getInstalledApplications() = runBlocking {
//        //Given
//
//        //When
//
//        //Then
//    }
//}