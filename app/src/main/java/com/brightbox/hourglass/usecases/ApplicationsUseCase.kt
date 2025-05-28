package com.brightbox.hourglass.usecases

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.events.ApplicationsEvent
import com.brightbox.hourglass.model.ApplicationsModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ApplicationsUseCase @Inject constructor(
    private val db: HourglassDatabase,
    @ApplicationContext private val application: Context
) {

    // Get all apps from the database
    fun getApplicationsFromDatabase(appNameFilter: String? = null): Flow<List<ApplicationsModel>> {
        Log.d(
            "ApplicationsUseCase",
            "getApplicationsFromDatabase: map called, filter = $appNameFilter"
        )
        val appList = db.applicationsDao().getApplications().map { apps ->
            apps.sortedBy { it.name.lowercase() }
        }
        return appList
    }

    // Get Icons from the applications
    suspend fun getApplicationsIcons(appList: List<ApplicationsModel>): Map<String, Drawable> {
        val appIcons = mutableMapOf<String, Drawable>()
        withContext(Dispatchers.IO) {
            appList.forEach { app ->
                appIcons[app.packageName] =
                    application.packageManager.getApplicationIcon(app.packageName)
            }
        }
        return appIcons
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

    suspend fun toggleAppPinnedState(app: ApplicationsModel) {
        withContext(Dispatchers.IO) {
            val pinnedApp = app.copy(isPinned = !app.isPinned)
            db.applicationsDao().upsertApplication(pinnedApp)
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
            Log.d("ApplicationsUseCase", "upsertAppToDatabase: appInfo = $appInfo")
            val app = ApplicationsModel(
                packageName = packageName,
                name = packageManager.getApplicationLabel(appInfo).toString(),
                isPinned = false,
                isRestricted = false
            )
            db.applicationsDao().upsertApplication(app)
//            queryInstalledApplicationsToDatabase()
        }
    }

    private suspend fun deleteAppFromDatabase(packageName: String) {
        withContext(Dispatchers.IO) {
            val app = db.applicationsDao().findByPackageName(packageName)
            if (app != null) {
                db.applicationsDao().deleteApplication(app)
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
                installedAppsIntents.filter { it.activityInfo.packageName != application.packageName }
                    .map { it.activityInfo.packageName }.toSet()
            Log.d("getApps", "App List: $installedPackageNames")

            val dbApps = db.applicationsDao().getApplicationsForQueryInstalledApps()

            dbApps.forEach { app ->
                if (!installedPackageNames.contains(app.packageName)) {
                    db.applicationsDao().deleteApplication(app)
                }
            }

            installedAppsIntents.forEach {
                var app = db.applicationsDao().findByPackageName(it.activityInfo.packageName)
                if (app == null) {
                    app = ApplicationsModel(
                        packageName = it.activityInfo.packageName,
                        name = it.loadLabel(packageManager).toString(),
                        isPinned = false,
                        isRestricted = false
                    )
                    db.applicationsDao().upsertApplication(app)
                }
            }
        }
    }

    fun searchOnInternet(text: String) {
        val query = Uri.encode(text)
        val url = "https://www.google.com/search?q=$query"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // Ensure there's an app to handle the intent
        intent.resolveActivity(application.packageManager)?.let {
            application.applicationContext.startActivity(intent, null)
        }
    }
}