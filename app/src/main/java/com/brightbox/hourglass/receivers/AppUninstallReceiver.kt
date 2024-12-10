package com.brightbox.hourglass.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.brightbox.hourglass.viewmodel.AppsViewModel

class AppUninstallReceiver : BroadcastReceiver() {

    private lateinit var appsViewModel: AppsViewModel

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_PACKAGE_REMOVED) {
            val packageName = intent.data?.encodedSchemeSpecificPart
            appsViewModel.getApps()
        }
    }
}