package com.brightbox.hourglass.usecases

import android.app.Application
import android.content.Intent

class AppUseCase(private val application: Application) {

    fun openApp(packageName: String) {
        val packageManager = application.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.applicationContext.startActivity(intent)
        }
    }
}