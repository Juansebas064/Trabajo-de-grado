package com.brightbox.hourglass.events.preferences

sealed interface GeneralPreferencesEvent {
    data class SetOpenKeyboardInAppMenu(val value: Boolean) : GeneralPreferencesEvent
}