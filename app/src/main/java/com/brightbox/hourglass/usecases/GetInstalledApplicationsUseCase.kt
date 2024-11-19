package com.brightbox.hourglass.usecases

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager

class GetInstalledApplicationsUseCase(private val context: Context) {

    fun getList(): List<ApplicationInfo> {
        val apps = context.packageManager.getInstalledApplications(PackageManager.GET_META_DATA)
        return apps
    }
}