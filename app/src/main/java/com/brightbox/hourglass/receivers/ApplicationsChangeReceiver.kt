package com.brightbox.hourglass.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.brightbox.hourglass.events.AppChangeEvent
import com.brightbox.hourglass.usecases.ApplicationsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ApplicationsChangeReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ApplicationsChangeReceiver", "onReceive called")

        CoroutineScope(Dispatchers.IO).launch {
            when (intent.action) {
                Intent.ACTION_PACKAGE_REMOVED -> notifyAppUninstall(intent)
                Intent.ACTION_PACKAGE_ADDED -> notifyAppInstall(intent)
            }
        }
    }

    private suspend fun notifyAppUninstall(intent: Intent) {
        withContext(Dispatchers.IO) {
            val packageName = intent.data?.encodedSchemeSpecificPart
            Log.d("ApplicationsChangeReceiver", "Package deleted name: $packageName")
            if (packageName != null) {
                ApplicationsUseCase.eventBus.emit(AppChangeEvent.AppUninstalled(packageName))
            }
        }
    }

    private suspend fun notifyAppInstall(intent: Intent) {
        withContext(Dispatchers.IO) {
            val packageName = intent.data?.encodedSchemeSpecificPart
            Log.d("ApplicationsChangeReceiver", "Package added name: $packageName")
            if (packageName != null) {
                ApplicationsUseCase.eventBus.emit(AppChangeEvent.AppInstalled(packageName))
            }
        }
    }
}