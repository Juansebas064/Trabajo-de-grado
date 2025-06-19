package com.brightbox.dino.model.states.preferences

import com.brightbox.dino.model.constants.APP_LANGUAGE
import com.brightbox.dino.model.constants.OPEN_KEYBOARD_IN_APP_MENU
import com.brightbox.dino.model.constants.SEARCH_ENGINE
import com.brightbox.dino.model.constants.SHOW_SEARCH_ON_INTERNET
import com.brightbox.dino.model.constants.SOLID_BACKGROUND
import com.brightbox.dino.model.constants.preferencesDefaults

data class PreferencesState(
    val isLoading: Boolean = true,
    val openKeyboardInAppMenu: Boolean = preferencesDefaults.getValue(OPEN_KEYBOARD_IN_APP_MENU) as Boolean,
    val solidBackground: Boolean = preferencesDefaults.getValue(SOLID_BACKGROUND) as Boolean,
    val appLanguage: String = preferencesDefaults.getValue(APP_LANGUAGE).toString(),
    val showSearchOnInternet: Boolean = preferencesDefaults.getValue(SHOW_SEARCH_ON_INTERNET) as Boolean,
    val searchEngine: String = preferencesDefaults.getValue(SEARCH_ENGINE).toString(),
    val theme: String = preferencesDefaults.getValue(SEARCH_ENGINE).toString(),
    val showEssentialShortcuts: Boolean = true,
)
