package com.brightbox.hourglass.usecases

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.room.Room
import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.model.ApplicationModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AppsMenuUseCase(private val application: Application) {

    // Instantiate the database
    private val db: HourglassDatabase = Room.databaseBuilder(
        application.applicationContext,
        HourglassDatabase::class.java,
        "hourglass_database"
    ).build()

    // Get all apps from the database
    suspend fun getApps(appNameFilter: String? = null): List<ApplicationModel> {
        queryInstalledApps()
        val appList = withContext(Dispatchers.IO) {
            db.applicationDao().getApplications()
        }

        val appNameFilterNormalized = appNameFilter?.trim()?.lowercase()

        return if (appNameFilterNormalized.isNullOrBlank()) {
            appList.sortedBy { it.name.lowercase() }
        } else {
            appList.filter { it.name.lowercase().contains(appNameFilterNormalized) }
                .sortedBy { it.name.lowercase() }
        }
    }

    // Query apps installed on the device and add them to the database
    private fun queryInstalledApps() {
        CoroutineScope(Dispatchers.IO).launch {
            Log.d("queryInstalledApps", "Start queryInstalledApps")
            val packageManager = application.packageManager
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val installedAppsIntents =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)

            val installedPackageNames = installedAppsIntents.map { it.activityInfo.packageName }.toSet()

            val dbApps = db.applicationDao().getApplications()

            dbApps.forEach { app ->
                if (!installedPackageNames.contains(app.packageName)) {
                    db.applicationDao().deleteApplication(app)
                }
            }

            installedAppsIntents.forEach {
                var app = db.applicationDao().findByPackageName(it.activityInfo.packageName)
                if (app == null) {
                    app = ApplicationModel(
                        name = it.loadLabel(packageManager).toString(),
                        packageName = it.activityInfo.packageName,
                        isPinned = false,
                        isRestricted = false
                    )
                    db.applicationDao().upsertApplication(app)
                }
            }
        }
    }
}