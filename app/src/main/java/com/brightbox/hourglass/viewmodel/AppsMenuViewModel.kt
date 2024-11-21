package com.brightbox.hourglass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brightbox.hourglass.usecases.AppsMenuUseCase

class AppsMenuViewModel(application: Application) : AndroidViewModel(application) {

    private val appsMenuUseCase = AppsMenuUseCase(application)

    private val _appsList = MutableLiveData<List<Map<String, String>>>()
    val appsList: LiveData<List<Map<String, String>>> get() = _appsList

    init {
        getApps()
    }

    private fun getApps() {
        _appsList.value = appsMenuUseCase.getList()
    }

    fun openApp(packageName: String) {
        appsMenuUseCase.openApp(packageName)
    }



}