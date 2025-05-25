package com.brightbox.hourglass.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val closeMenuReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // El Worker envió el evento, actualizar la lista de tareas
            closeMenu()
            Log.d("HomeViewModel", "Home press received")
        }
    }

    private val _closeMenu = MutableSharedFlow<Unit>()
    val closeMenu = _closeMenu.asSharedFlow()

    fun closeMenu() {
        viewModelScope.launch {
            _closeMenu.emit(Unit)
        }
    }

    init {
        ContextCompat.registerReceiver(
            context,
            closeMenuReceiver,
            IntentFilter("HOME_BUTTON_PRESSED"),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }
}