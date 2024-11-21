package com.brightbox.hourglass.usecases

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import android.widget.Toast

class AppsMenuUseCase(private val application: Application) {

    fun getList(): List<Map<String, String>> {
        val packageManager = application.packageManager
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }
        val packages: List<ResolveInfo> =
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        Log.d("GetInstalledApplicationsUseCase", packages.toString())
        return packages.map {
            mapOf(
                "appName" to it.loadLabel(packageManager).toString(),
                "packageName" to it.activityInfo.packageName
            )
        }.sortedBy { it["appName"]?.lowercase() }
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