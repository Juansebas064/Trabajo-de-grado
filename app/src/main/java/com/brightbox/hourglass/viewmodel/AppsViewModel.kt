package com.brightbox.hourglass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brightbox.hourglass.model.ApplicationModel
import com.brightbox.hourglass.usecases.AppUseCase
import com.brightbox.hourglass.usecases.AppsMenuUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppsViewModel(application: Application) : AndroidViewModel(application) {

    private val appsMenuUseCase = AppsMenuUseCase(application)
    private val appUseCase = AppUseCase(application)

    private val _appsList = MutableStateFlow(emptyList<ApplicationModel>())
    val appsList = _appsList.asStateFlow()

    init {
        appsMenuUseCase.queryInstalledApps()
        getApps()
    }


    fun getApps(appNameFilter: String? = null) {
        viewModelScope.launch {
            _appsList.value = appsMenuUseCase.getApps(appNameFilter)
        }
    }

    fun openApp(packageName: String) {
        appUseCase.openApp(packageName)
    }
}