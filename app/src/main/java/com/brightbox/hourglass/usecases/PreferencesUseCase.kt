package com.brightbox.hourglass.usecases

import com.brightbox.hourglass.constants.OPEN_KEYBOARD_IN_APP_MENU
import com.brightbox.hourglass.constants.SOLID_BACKGROUND
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
}