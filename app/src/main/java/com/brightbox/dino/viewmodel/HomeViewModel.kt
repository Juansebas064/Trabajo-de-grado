package com.brightbox.dino.viewmodel

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.STATUS_BAR_SERVICE
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.registerReceiver
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val timeLimitWorkerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            // El Worker envió el evento, actualizar la lista de tareas
            notifyTimeLimitWork()
        }
    }

    private val _timeLimitWorkDone = MutableSharedFlow<Unit>()
    val timeLimitWorkDone = _timeLimitWorkDone.asSharedFlow()

    fun notifyTimeLimitWork() {
        viewModelScope.launch {
            _timeLimitWorkDone.emit(Unit)
        }
    }

    init {
        registerReceiver(
            context,
            timeLimitWorkerReceiver,
            IntentFilter("TIME_LIMIT_WORKER"),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )
    }

    fun expandNotificationsPanel() {
        try {
            val service = context.getSystemService(STATUS_BAR_SERVICE)
            val statusBarManager = Class.forName("android.app.StatusBarManager")

            val expandMethod: Method = statusBarManager.getMethod("expandNotificationsPanel")

            expandMethod.invoke(service)
        } catch (e: Exception) {
        }
    }
}