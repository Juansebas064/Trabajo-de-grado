package com.brightbox.hourglass.usecases

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import com.brightbox.hourglass.Application
import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.events.AppChangeEvent
import com.brightbox.hourglass.model.ApplicationModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppUseCase @Inject constructor(
    private val db: HourglassDatabase,
    @ApplicationContext private val application: Context
) {

    companion object {
        val eventBus = MutableSharedFlow<AppChangeEvent>()
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            queryInstalledApplicationsToDatabase()
            eventBus.collectLatest { event ->
                when (event) {
                    is AppChangeEvent.AppUninstalled -> {
                        Log.d("AppUseCase", "App uninstalled: ${event.packageName}")

                        deleteAppFromDatabase(event.packageName)
                    }

                    is AppChangeEvent.AppInstalled -> {
                        Log.d("AppUseCase", "App installed: ${event.packageName}")
                        upsertAppToDatabase(event.packageName)
                    }
                }
            }
        }
    }

    // Get all apps from the database
    fun getApplicationsFromDatabase(appNameFilter: String? = null): Flow<List<ApplicationModel>> {
        Log.d("AppUseCase", "getApplicationsFromDatabase: map called, filter = $appNameFilter")
        val appList = db.applicationDao().getApplications()
        return appList
    }


    fun openApp(packageName: String) {
        val packageManager = application.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.applicationContext.startActivity(intent)
        }
    }

    fun openApp(intent: Intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        application.applicationContext.startActivity(intent)
    }

    suspend fun toggleAppPinnedState(app: ApplicationModel) {
        withContext(Dispatchers.IO) {
            val pinnedApp = app.copy(isPinned = !app.isPinned)
            db.applicationDao().upsertApplication(pinnedApp)
        }
    }

    fun openAppInfo(packageName: String) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.applicationContext.startActivity(intent)
    }

    fun uninstallApp(packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE).apply {
            data = Uri.parse("package:${packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.applicationContext.startActivity(intent)
    }

    private suspend fun upsertAppToDatabase(packageName: String) {
        withContext(Dispatchers.IO) {
            val packageManager = application.packageManager
            val appInfo = packageManager.getApplicationInfo(packageName, 0)
            Log.d("AppUseCase", "upsertAppToDatabase: appInfo = $appInfo")
            val app = ApplicationModel(
                packageName = packageName,
                name = packageManager.getApplicationLabel(appInfo).toString(),
                isPinned = false,
                isRestricted = false
            )
            db.applicationDao().upsertApplication(app)
//            queryInstalledApplicationsToDatabase()
        }
    }

    private suspend fun deleteAppFromDatabase(packageName: String) {
        withContext(Dispatchers.IO) {
            val app = db.applicationDao().findByPackageName(packageName)
            if (app != null) {
                db.applicationDao().deleteApplication(app)
            }
        }
    }

    // Query apps installed on the device and add them to the database
    suspend fun queryInstalledApplicationsToDatabase() {
        withContext(Dispatchers.IO) {
            Log.d("queryInstalledApps", "Start queryInstalledApps")
            val packageManager = application.packageManager
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val installedAppsIntents =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)

            val installedPackageNames =
                installedAppsIntents.map { it.activityInfo.packageName }.toSet()
            Log.d("getApps", "App List: $installedPackageNames")

            val dbApps = db.applicationDao().getApplicationsForQueryInstalledApps()

            dbApps.forEach { app ->
                if (!installedPackageNames.contains(app.packageName)) {
                    db.applicationDao().deleteApplication(app)
                }
            }

            installedAppsIntents.forEach {
                var app = db.applicationDao().findByPackageName(it.activityInfo.packageName)
                if (app == null) {
                    app = ApplicationModel(
                        packageName = it.activityInfo.packageName,
                        name = it.loadLabel(packageManager).toString(),
                        isPinned = false,
                        isRestricted = false
                    )
                    db.applicationDao().upsertApplication(app)
                }
            }
        }
    }
}