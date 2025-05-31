package com.brightbox.dino.viewmodel

import android.content.Context
import android.content.Context.STATUS_BAR_SERVICE
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.lang.reflect.Method
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context
) : ViewModel() {

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