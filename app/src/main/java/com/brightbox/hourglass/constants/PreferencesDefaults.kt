package com.brightbox.hourglass.constants

const val OPEN_KEYBOARD_IN_APP_MENU: String = "open_keyboard_in_app_menu"
const val SOLID_BACKGROUND: String = "solid_background"
const val APP_LANGUAGE: String = "app_language"
const val SHOW_SEARCH_ON_INTERNET: String = "show_search_on_internet"
const val SEARCH_ENGINE: String = "search_engine"
const val THEME: String = "theme"

val LANGUAGES = mapOf(
    "en" to "English",
    "es" to "Español"
)

val preferencesDefaults = mapOf(
    OPEN_KEYBOARD_IN_APP_MENU to true,
    SOLID_BACKGROUND to true,
    APP_LANGUAGE to "en",
    SHOW_SEARCH_ON_INTERNET to true,
    SEARCH_ENGINE to "Google",
    THEME to "system"
)