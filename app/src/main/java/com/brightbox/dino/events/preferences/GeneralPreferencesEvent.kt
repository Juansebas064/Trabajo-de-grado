package com.brightbox.dino.events.preferences

sealed interface GeneralPreferencesEvent {
    data class SetOpenKeyboardInAppMenu(val value: Boolean) : GeneralPreferencesEvent
    data class SetSolidBackground(val value: Boolean) : GeneralPreferencesEvent
    data class SetAppLanguage(val value: String) : GeneralPreferencesEvent
    data class SetShowSearchOnInternet(val value: Boolean) : GeneralPreferencesEvent
    data class SetSearchEngine(val value: String) : GeneralPreferencesEvent
    data object ChangeTheme : GeneralPreferencesEvent
    data class SetShowEssentialShortcuts(val value: Boolean) : GeneralPreferencesEvent
}