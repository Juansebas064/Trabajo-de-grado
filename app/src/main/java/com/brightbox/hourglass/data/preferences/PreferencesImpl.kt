package com.brightbox.hourglass.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import com.brightbox.hourglass.constants.OPEN_KEYBOARD_IN_APP_MENU
import com.brightbox.hourglass.states.preferences.GeneralPreferencesState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore("preferences")

class PreferencesImpl @Inject constructor(
    @ApplicationContext private val context: Context
): Preferences {

    private object PreferencesKeys {
        val OPEN_KEYBOARD_IN_APP_MENU_KEY = booleanPreferencesKey(OPEN_KEYBOARD_IN_APP_MENU)
    }

    override suspend fun getGeneralPreferences(): Flow<GeneralPreferencesState> {
        return dataStore.data
            .map { preferences ->
                val openKeyboard = preferences[PreferencesKeys.OPEN_KEYBOARD_IN_APP_MENU_KEY] ?: true // Valor por defecto

                // Construye y devuelve el objeto de estado completo
                GeneralPreferencesState(
                    openKeyboardInAppMenu = openKeyboard,
                )
            }
    }

    private val dataStore = context.dataStore

    override suspend fun setOpenKeyboardInAppMenu(key: String, value: Boolean) {
        val preferenceKey = booleanPreferencesKey(key)
        dataStore.edit { preferences ->
            preferences[preferenceKey] = value
        }
    }

    override suspend fun getOpenKeyboardInAppMenu(key: String): Flow<Boolean> {
        val preferenceKey = booleanPreferencesKey(key)
        return dataStore.data.map { preferences ->
            preferences[preferenceKey] ?: false
        }
    }
}