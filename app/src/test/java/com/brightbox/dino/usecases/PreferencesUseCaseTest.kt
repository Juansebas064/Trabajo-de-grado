package com.brightbox.dino.usecases

import com.brightbox.dino.model.constants.*
import com.brightbox.dino.model.data.preferences.PreferencesImpl
import com.brightbox.dino.model.usecases.PreferencesUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class PreferencesUseCaseTest {

    private val mockPreferences: PreferencesImpl = mockk()
    private val useCase = PreferencesUseCase(mockPreferences)

    @Test
    fun `setTheme should update preferences`() = runTest {
        coEvery { mockPreferences.setStringPreference(THEME, "dark") } returns Unit

        useCase.setTheme("dark")

        coVerify { mockPreferences.setStringPreference(THEME, "dark") }
    }

    @Test
    fun `setShowEssentialShortcuts should update preferences`() = runTest {
        coEvery { mockPreferences.setBooleanPreference(SHOW_ESSENTIAL_SHORTCUTS, true) } returns Unit

        useCase.setShowEssentialShortcuts(true)

        coVerify { mockPreferences.setBooleanPreference(SHOW_ESSENTIAL_SHORTCUTS, true) }
    }

    @Test
    fun `setSearchEngine should update preferences`() = runTest {
        coEvery { mockPreferences.setStringPreference(SEARCH_ENGINE, "google") } returns Unit

        useCase.setSearchEngine("google")

        coVerify { mockPreferences.setStringPreference(SEARCH_ENGINE, "google") }
    }
}