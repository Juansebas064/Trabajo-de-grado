package com.brightbox.hourglass.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.brightbox.hourglass.usecases.GetInstalledApplicationsUseCase

class AppsMenuViewModel(private val application: Application) : AndroidViewModel(application) {

    private val getInstalledApplicationsUseCase = GetInstalledApplicationsUseCase(application)

    private val _appsList = MutableLiveData<List<Map<String, String>>>()
    val appsList: LiveData<List<Map<String, String>>> get() = _appsList

    init {
        getApps()
    }

    private fun getApps() {
        _appsList.value = getInstalledApplicationsUseCase.getList()
    }

    fun openApp(packageName: String) {
        val packageManager = application.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.applicationContext.startActivity(intent)
        } else {
            Toast.makeText( application.applicationContext, "No se puede abrir la aplicación", Toast.LENGTH_SHORT).show()
        }
    }

}