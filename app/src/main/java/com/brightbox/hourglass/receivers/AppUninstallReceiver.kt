package com.brightbox.hourglass.receivers

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.brightbox.hourglass.services.UninstallWorker
import com.brightbox.hourglass.viewmodel.AppsViewModel

class AppUninstallReceiver : BroadcastReceiver() {

//    private lateinit var appsViewModel: AppsViewModel

    override fun onReceive(context: Context, intent: Intent) {
//        Log.d("AppUninstallReceiver", "onReceive called")
        if (intent.action == Intent.ACTION_PACKAGE_REMOVED) {
            Log.d("AppUninstallReceiver", "Entra :D")
            val packageName = intent.data?.encodedSchemeSpecificPart
            val data = Data.Builder()
                .putString(UninstallWorker.PACKAGE_NAME_KEY, packageName)
                .build()

            // Create a work request
            val workRequest = OneTimeWorkRequest.Builder(UninstallWorker::class.java)
                .setInputData(data)
                .build()

            // Enqueue the work
            WorkManager.getInstance(context).enqueue(workRequest)

//            appsViewModel = AppsViewModel.getInstance(context.applicationContext as Application)
//
//            appsViewModel.setAppShowingOptions("none")
//            appsViewModel.getApps()
        }
    }
}