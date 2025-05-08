package com.brightbox.hourglass.usecases

import com.brightbox.hourglass.constants.OPEN_KEYBOARD_IN_APP_MENU
import com.brightbox.hourglass.data.preferences.Preferences
import com.brightbox.hourglass.data.preferences.PreferencesImpl
import com.brightbox.hourglass.states.preferences.GeneralPreferencesState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GeneralPreferencesUseCase @Inject constructor(
    private val preferences: PreferencesImpl
) {

    suspend fun getGeneralPreferences(): Flow<GeneralPreferencesState> {
        return preferences.getGeneralPreferences()
    }

    suspend fun setOpenKeyboardInAppMenu(value: Boolean) {
        preferences.setOpenKeyboardInAppMenu(OPEN_KEYBOARD_IN_APP_MENU, value)
    }
}