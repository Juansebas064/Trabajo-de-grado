package com.brightbox.hourglass.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.brightbox.hourglass.constants.OPEN_KEYBOARD_IN_APP_MENU
import com.brightbox.hourglass.constants.SOLID_BACKGROUND
import com.brightbox.hourglass.constants.preferencesDefaults
import com.brightbox.hourglass.data.preferences.PreferencesImpl.PreferencesKeys.OPEN_KEYBOARD_IN_APP_MENU_KEY
import com.brightbox.hourglass.data.preferences.PreferencesImpl.PreferencesKeys.SOLID_BACKGROUND_KEY
import com.brightbox.hourglass.states.preferences.PreferencesState
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
    }

    override suspend fun getGeneralPreferences(): Flow<PreferencesState> {
        return dataStore.data
            .map { preferences ->
                val openKeyboard = preferences[OPEN_KEYBOARD_IN_APP_MENU_KEY] ?: preferencesDefaults[OPEN_KEYBOARD_IN_APP_MENU]!!
                val solidBackground = preferences[SOLID_BACKGROUND_KEY] ?: preferencesDefaults[SOLID_BACKGROUND]!!

                // Construye y devuelve el objeto de estado completo
                PreferencesState(
                    openKeyboardInAppMenu = openKeyboard,
                    solidBackground = solidBackground
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
}