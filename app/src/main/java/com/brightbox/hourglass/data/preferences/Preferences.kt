package com.brightbox.hourglass.data.preferences

import com.brightbox.hourglass.states.preferences.PreferencesState
import kotlinx.coroutines.flow.Flow

interface Preferences {
    suspend fun getGeneralPreferences(): Flow<PreferencesState>

    suspend fun setBooleanPreference(key: String, value: Boolean)
}