package com.brightbox.dino.viewmodel.preferences

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.dino.events.preferences.GeneralPreferencesEvent
import com.brightbox.dino.states.preferences.PreferencesState
import com.brightbox.dino.usecases.PreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val preferencesUseCase: PreferencesUseCase,
    @ApplicationContext val context: Context
) : ViewModel() {

    private val _state = MutableStateFlow(PreferencesState())

    val state = _state.asStateFlow().onStart {
        Log.d("PreferencesViewModel", "Loading preferences")
        loadPreferences()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        PreferencesState()
    )

    private fun loadPreferences() {
        runBlocking {
            preferencesUseCase.getPreferences()
                .onEach { preferencesState ->
                    _state.value = preferencesState
                }
                .launchIn(viewModelScope)
        }
    }

    private fun setOpenKeyboardInAppMenu(value: Boolean) {
        viewModelScope.launch {
            preferencesUseCase.setOpenKeyboardInAppMenu(value)
        }
    }

    private fun setSolidBackground(value: Boolean) {
        viewModelScope.launch {
            preferencesUseCase.setSolidBackground(value)
        }
    }

    private fun setAppLanguage(value: String) {
        viewModelScope.launch {
            preferencesUseCase.setAppLanguage(value)
        }
    }

    private fun setShowSearchOnInternet(value: Boolean) {
        viewModelScope.launch {
            preferencesUseCase.setShowSearchOnInternet(value)
        }
    }

    private fun setSearchEngine(value: String) {
        viewModelScope.launch {
            preferencesUseCase.setSearchEngine(value)
        }
    }

    private fun setTheme(value: String) {
        viewModelScope.launch {
            preferencesUseCase.setTheme(value)
        }
    }

    private fun setShowEssentialShortcuts(value: Boolean) {
        viewModelScope.launch {
            preferencesUseCase.setShowEssentialShortcuts(value)
        }
    }

    fun onEvent(event: GeneralPreferencesEvent) {
        when (event) {
            is GeneralPreferencesEvent.SetOpenKeyboardInAppMenu -> {
                _state.update {
                    it.copy(
                        openKeyboardInAppMenu = event.value
                    )
                }
                setOpenKeyboardInAppMenu(event.value)
            }

            is GeneralPreferencesEvent.SetSolidBackground -> {
                _state.update {
                    it.copy(
                        solidBackground = event.value
                    )
                }
                setSolidBackground(event.value)
            }

            is GeneralPreferencesEvent.SetAppLanguage -> {
                _state.update {
                    it.copy(
                        appLanguage = event.value
                    )
                }
                setAppLanguage(event.value)
            }

            is GeneralPreferencesEvent.SetShowSearchOnInternet -> {
                _state.update {
                    it.copy(
                        showSearchOnInternet = event.value
                    )
                }
                setShowSearchOnInternet(event.value)
            }

            is GeneralPreferencesEvent.SetSearchEngine -> {
                _state.update {
                    it.copy(
                        searchEngine = event.value
                    )
                }
                setSearchEngine(event.value)
            }

            GeneralPreferencesEvent.ChangeTheme -> {
                val currentTheme = when (_state.value.theme) {
                    "system" -> "light"
                    "light" -> "dark"
                    "dark" -> "system"
                    else -> "system"
                }
                _state.update {
                    it.copy(
                        theme = currentTheme
                    )
                }
                Log.d("HourglassProductivityLauncherTheme", "Enter onEvent")
                setTheme(currentTheme)
            }

            is GeneralPreferencesEvent.SetShowEssentialShortcuts -> {
                _state.update {
                    it.copy(
                        showEssentialShortcuts = event.value
                    )
                }
                setShowEssentialShortcuts(event.value)
            }
        }
    }
}