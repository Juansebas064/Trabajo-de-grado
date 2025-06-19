package com.brightbox.dino.model.usecases

import com.brightbox.dino.model.constants.APP_LANGUAGE
import com.brightbox.dino.model.constants.OPEN_KEYBOARD_IN_APP_MENU
import com.brightbox.dino.model.constants.SEARCH_ENGINE
import com.brightbox.dino.model.constants.SHOW_ESSENTIAL_SHORTCUTS
import com.brightbox.dino.model.constants.SHOW_SEARCH_ON_INTERNET
import com.brightbox.dino.model.constants.SOLID_BACKGROUND
import com.brightbox.dino.model.constants.THEME
import com.brightbox.dino.model.database.preferences.PreferencesImpl
import com.brightbox.dino.model.states.preferences.PreferencesState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class PreferencesUseCase @Inject constructor(
    private val preferences: PreferencesImpl
) {

    suspend fun getPreferences(): Flow<PreferencesState> {
        return preferences.getGeneralPreferences()
    }

    suspend fun setOpenKeyboardInAppMenu(value: Boolean) {
        preferences.setBooleanPreference(OPEN_KEYBOARD_IN_APP_MENU, value)
    }

    suspend fun setSolidBackground(value: Boolean) {
        preferences.setBooleanPreference(SOLID_BACKGROUND, value)
    }

    suspend fun setAppLanguage(value: String) {
        preferences.setStringPreference(APP_LANGUAGE, value)
    }

    suspend fun setShowSearchOnInternet(value: Boolean) {
        preferences.setBooleanPreference(SHOW_SEARCH_ON_INTERNET, value)
    }

    suspend fun setSearchEngine(value: String) {
        preferences.setStringPreference(SEARCH_ENGINE, value)
    }

    suspend fun setTheme(value: String) {
        preferences.setStringPreference(THEME, value)
    }

    suspend fun setShowEssentialShortcuts(value: Boolean) {
        preferences.setBooleanPreference(SHOW_ESSENTIAL_SHORTCUTS, value)
    }
}