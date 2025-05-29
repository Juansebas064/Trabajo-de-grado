package com.brightbox.dino.data.preferences

import com.brightbox.dino.states.preferences.PreferencesState
import kotlinx.coroutines.flow.Flow

interface Preferences {
    suspend fun getGeneralPreferences(): Flow<PreferencesState>

    suspend fun setBooleanPreference(key: String, value: Boolean)

    suspend fun setStringPreference(key: String, value: String)
}