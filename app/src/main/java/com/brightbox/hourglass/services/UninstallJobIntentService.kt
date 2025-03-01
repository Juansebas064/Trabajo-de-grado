package com.brightbox.hourglass.services

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.work.Worker
import androidx.work.WorkerParameters

class UninstallWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {

    companion object {
        const val TAG = "UninstallWorker"
        const val PACKAGE_NAME_KEY = "package_name"
    }

    override fun doWork(): Result {
        val packageName = inputData.getString(PACKAGE_NAME_KEY)
        Log.d(TAG, "doWork: Uninstall worker started for package: $packageName")

        if (packageName == applicationContext.packageName) {
            Log.d(TAG, "doWork: Our app is being uninstalled")
//            handleUninstall()
        } else {
            Log.d(TAG, "doWork: Another app is being uninstalled")
        }

        return Result.success()
    }
}