package com.brightbox.hourglass.usecases

import android.util.Log
import com.brightbox.hourglass.constants.APP_LANGUAGE
import com.brightbox.hourglass.constants.LANGUAGES
import com.brightbox.hourglass.constants.OPEN_KEYBOARD_IN_APP_MENU
import com.brightbox.hourglass.constants.SEARCH_ENGINE
import com.brightbox.hourglass.constants.SHOW_SEARCH_ON_INTERNET
import com.brightbox.hourglass.constants.SOLID_BACKGROUND
import com.brightbox.hourglass.constants.THEME
import com.brightbox.hourglass.data.preferences.PreferencesImpl
import com.brightbox.hourglass.states.preferences.PreferencesState
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
}