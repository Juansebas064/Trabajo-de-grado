package com.brightbox.dino.model.database.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.brightbox.dino.model.constants.APP_LANGUAGE
import com.brightbox.dino.model.constants.OPEN_KEYBOARD_IN_APP_MENU
import com.brightbox.dino.model.constants.SEARCH_ENGINE
import com.brightbox.dino.model.constants.SHOW_ESSENTIAL_SHORTCUTS
import com.brightbox.dino.model.constants.SHOW_SEARCH_ON_INTERNET
import com.brightbox.dino.model.constants.SOLID_BACKGROUND
import com.brightbox.dino.model.constants.THEME
import com.brightbox.dino.model.constants.preferencesDefaults
import com.brightbox.dino.model.database.preferences.PreferencesImpl.PreferencesKeys.APP_LANGUAGE_KEY
import com.brightbox.dino.model.database.preferences.PreferencesImpl.PreferencesKeys.OPEN_KEYBOARD_IN_APP_MENU_KEY
import com.brightbox.dino.model.database.preferences.PreferencesImpl.PreferencesKeys.SEARCH_ENGINE_KEY
import com.brightbox.dino.model.database.preferences.PreferencesImpl.PreferencesKeys.SHOW_SEARCH_ON_INTERNET_KEY
import com.brightbox.dino.model.database.preferences.PreferencesImpl.PreferencesKeys.SOLID_BACKGROUND_KEY
import com.brightbox.dino.model.states.preferences.PreferencesState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("preferences")

class PreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
): Preferences {

    private object PreferencesKeys {
        val OPEN_KEYBOARD_IN_APP_MENU_KEY = booleanPreferencesKey(OPEN_KEYBOARD_IN_APP_MENU)
        val SOLID_BACKGROUND_KEY = booleanPreferencesKey(SOLID_BACKGROUND)
        val APP_LANGUAGE_KEY = stringPreferencesKey(APP_LANGUAGE)
        val SHOW_SEARCH_ON_INTERNET_KEY = booleanPreferencesKey(SHOW_SEARCH_ON_INTERNET)
        val SEARCH_ENGINE_KEY = stringPreferencesKey(SEARCH_ENGINE)
        val THEME_KEY = stringPreferencesKey(THEME)
        val SHOW_ESSENTIAL_SHORTCUTS_KEY = booleanPreferencesKey(SHOW_ESSENTIAL_SHORTCUTS)
    }

    override suspend fun getGeneralPreferences(): Flow<PreferencesState> {
        return dataStore.data
            .map { preferences ->
                val openKeyboard = preferences[OPEN_KEYBOARD_IN_APP_MENU_KEY] ?: preferencesDefaults[OPEN_KEYBOARD_IN_APP_MENU]!!
                val solidBackground = preferences[SOLID_BACKGROUND_KEY] ?: preferencesDefaults[SOLID_BACKGROUND]!!
                val appLanguage = preferences[APP_LANGUAGE_KEY] ?: preferencesDefaults[APP_LANGUAGE]!!
                val showSearchOnInternet = preferences[SHOW_SEARCH_ON_INTERNET_KEY] ?: preferencesDefaults[SHOW_SEARCH_ON_INTERNET]!!
                val searchEngine = preferences[SEARCH_ENGINE_KEY] ?: preferencesDefaults[SEARCH_ENGINE]!!
                val theme = preferences[PreferencesKeys.THEME_KEY] ?: preferencesDefaults[THEME]!!
                val showEssentialShortcuts = preferences[PreferencesKeys.SHOW_ESSENTIAL_SHORTCUTS_KEY] ?: preferencesDefaults[SHOW_ESSENTIAL_SHORTCUTS]!!

                // Construye y devuelve el objeto de estado completo
                PreferencesState(
                    isLoading = false,
                    openKeyboardInAppMenu = openKeyboard as Boolean,
                    solidBackground = solidBackground as Boolean,
                    appLanguage = appLanguage.toString(),
                    showSearchOnInternet = showSearchOnInternet as Boolean,
                    searchEngine = searchEngine.toString(),
                    theme = theme.toString(),
                    showEssentialShortcuts = showEssentialShortcuts as Boolean,
                )
            }
    }

    private val dataStore = context.dataStore

    override suspend fun setBooleanPreference(key: String, value: Boolean) {
        val preferenceKey = booleanPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

    override suspend fun setStringPreference(key: String, value: String) {
        val preferenceKey = stringPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }


}