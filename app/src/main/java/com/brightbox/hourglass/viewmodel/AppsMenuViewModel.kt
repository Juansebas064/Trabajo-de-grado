package com.brightbox.hourglass.viewmodel

import android.app.Application
import android.content.Context
import android.content.pm.ApplicationInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.brightbox.hourglass.usecases.GetInstalledApplicationsUseCase

class AppsMenuViewModel(private val getInstalledApplicationsUseCase: GetInstalledApplicationsUseCase) : ViewModel() {

    private val _appsList = MutableLiveData<List<ApplicationInfo>>()
    val appsList: LiveData<List<ApplicationInfo>> get() = _appsList

    fun getApps() {
        _appsList.value = getInstalledApplicationsUseCase.getList()
    }

}