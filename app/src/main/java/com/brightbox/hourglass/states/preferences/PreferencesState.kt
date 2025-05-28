package com.brightbox.hourglass.states.preferences

import com.brightbox.hourglass.constants.OPEN_KEYBOARD_IN_APP_MENU
import com.brightbox.hourglass.constants.SOLID_BACKGROUND
import com.brightbox.hourglass.constants.preferencesDefaults

data class PreferencesState(
    val isLoading: Boolean = true,
    val openKeyboardInAppMenu: Boolean = preferencesDefaults.getValue(OPEN_KEYBOARD_IN_APP_MENU),
    val solidBackground: Boolean = preferencesDefaults.getValue(SOLID_BACKGROUND)
)
