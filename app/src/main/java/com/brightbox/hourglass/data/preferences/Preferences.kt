package com.brightbox.hourglass.data.preferences

import com.brightbox.hourglass.states.preferences.GeneralPreferencesState
import kotlinx.coroutines.flow.Flow

interface Preferences {
    suspend fun getGeneralPreferences(): Flow<GeneralPreferencesState>

    suspend fun setOpenKeyboardInAppMenu(key: String, value: Boolean)
    suspend fun getOpenKeyboardInAppMenu(key: String): Flow<Boolean>
}