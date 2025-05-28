package com.brightbox.hourglass.states.preferences

import com.brightbox.hourglass.constants.APP_LANGUAGE
import com.brightbox.hourglass.constants.OPEN_KEYBOARD_IN_APP_MENU
import com.brightbox.hourglass.constants.SEARCH_ENGINE
import com.brightbox.hourglass.constants.SHOW_SEARCH_ON_INTERNET
import com.brightbox.hourglass.constants.SOLID_BACKGROUND
import com.brightbox.hourglass.constants.preferencesDefaults

data class PreferencesState(
    val isLoading: Boolean = true,
    val openKeyboardInAppMenu: Boolean = preferencesDefaults.getValue(OPEN_KEYBOARD_IN_APP_MENU) as Boolean,
    val solidBackground: Boolean = preferencesDefaults.getValue(SOLID_BACKGROUND) as Boolean,
    val appLanguage: String = preferencesDefaults.getValue(APP_LANGUAGE).toString(),
    val showSearchOnInternet: Boolean = preferencesDefaults.getValue(SHOW_SEARCH_ON_INTERNET) as Boolean,
    val searchEngine: String = preferencesDefaults.getValue(SEARCH_ENGINE).toString(),
    val theme: String = preferencesDefaults.getValue(SEARCH_ENGINE).toString()
)
