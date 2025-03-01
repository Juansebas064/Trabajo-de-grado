package com.brightbox.hourglass.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AppChangeReceiver : BroadcastReceiver() {

//    private lateinit var appsViewModel: AppsViewModel

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("BroadcastUninstallReceiver", "onReceive called")
        if (intent.action == Intent.ACTION_PACKAGE_FULLY_REMOVED) {
//            appsViewModel = AppsViewModel.getInstance(context.)
            Log.d("AppUninstallReceiver", "Entra :D")
            val packageName = intent.data?.encodedSchemeSpecificPart
//
//            appsViewModel.setAppShowingOptions("none")
//            appsViewModel.queryInstalledApps()
//            appsViewModel.getApps()
        }
    }
}