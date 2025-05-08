package com.brightbox.hourglass.viewmodel.preferences

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.hourglass.events.preferences.GeneralPreferencesEvent
import com.brightbox.hourglass.states.preferences.GeneralPreferencesState
import com.brightbox.hourglass.usecases.GeneralPreferencesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class GeneralPreferencesViewModel @Inject constructor(
    private val generalPreferencesUseCase: GeneralPreferencesUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GeneralPreferencesState())

    val state = _state.asStateFlow().onStart {
        Log.d("GeneralPreferencesViewModel", "Loading preferences")
        loadPreferences()
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        GeneralPreferencesState()
    )

    private fun loadPreferences() {
        runBlocking {
            generalPreferencesUseCase.getGeneralPreferences()
                .onEach { preferencesState ->
                    _state.value = preferencesState
                }
                .launchIn(viewModelScope)
        }
    }

    private fun setOpenKeyboardInAppMenu(value: Boolean) {
        viewModelScope.launch {
            generalPreferencesUseCase.setOpenKeyboardInAppMenu(value)
        }
    }

    fun onEvent(event: GeneralPreferencesEvent) {
        when (event) {
            is GeneralPreferencesEvent.SetOpenKeyboardInAppMenu -> {
                Log.d("GeneralPreferencesViewModel", "Setting openKeyboardInAppMenu to ${event.value}")
                _state.update {
                    it.copy(
                        openKeyboardInAppMenu = event.value
                    )
                }
                setOpenKeyboardInAppMenu(event.value)
            }
        }
    }
}