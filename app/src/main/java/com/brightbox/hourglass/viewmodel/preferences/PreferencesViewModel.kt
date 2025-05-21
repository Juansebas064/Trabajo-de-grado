package com.brightbox.hourglass.viewmodel.preferences

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.hourglass.events.preferences.GeneralPreferencesEvent
import com.brightbox.hourglass.states.preferences.PreferencesState
import com.brightbox.hourglass.usecases.PreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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
    private val preferencesUseCase: PreferencesUseCase
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
        }
    }
}