package com.brightbox.dino.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat.RECEIVER_NOT_EXPORTED
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TimeViewModel @Inject constructor(
    @ApplicationContext context: Context
) : ViewModel() {
    val midnightEventReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            updateTime()
        }

    }

    private val _currentTimeMillis = MutableStateFlow(System.currentTimeMillis())
    val currentTimeMillis: StateFlow<Long> = _currentTimeMillis.asStateFlow()

    init {
        updateTime()
        val intentFilter = IntentFilter("UPDATE_TASKS")
        registerReceiver(
            context,
            midnightEventReceiver,
            intentFilter,
            RECEIVER_NOT_EXPORTED
        )
    }

    fun updateTime() {
        _currentTimeMillis.value = (System.currentTimeMillis())
    }
}