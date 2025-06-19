package com.brightbox.dino.model.usecases

import android.R.drawable.sym_def_app_icon
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import com.brightbox.dino.config.DinoDatabase
import com.brightbox.dino.model.constants.SearchEnginesEnum
import com.brightbox.dino.model.ApplicationsModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.util.Locale
import javax.inject.Inject

class ApplicationsUseCase @Inject constructor(
    private val db: DinoDatabase,
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
                try {
                    Log.d("ApplicationsUseCase", "getApplicationsIcons: app = $app")
                    appIcons[app.packageName] =
                        application.packageManager.getApplicationIcon(app.packageName)
                } catch (exception: Exception) {
                    appIcons[app.packageName] =
                        AppCompatResources.getDrawable(application, sym_def_app_icon)!!
                }
            }
        }
        return appIcons
    }


    fun openApp(app: ApplicationsModel) {
        val packageManager = application.packageManager
        val intent = packageManager.getLaunchIntentForPackage(app.packageName)
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
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.data = "package:$packageName".toUri()
        application.applicationContext.startActivity(intent)
    }

    fun uninstallApp(packageName: String) {
        val intent = Intent(Intent.ACTION_DELETE).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        intent.data = "package:$packageName".toUri()
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
                limitTimeReached = false
            )
            db.applicationsDao().upsertApplication(app)
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
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL).filter {
                    it.activityInfo.packageName != application.packageName
                }

            val locale = Locale(Locale.getDefault().language)

            val installedPackageNames =
                installedAppsIntents.map { it.activityInfo.packageName }.toSet()

            Log.d("ApplicationsUseCase", "Our package name: ${application.packageName}")

            val dbApps = db.applicationsDao().getApplicationsForQueryInstalledApps()

            dbApps.forEach { app ->
                if (!installedPackageNames.contains(app.packageName)) {
                    db.applicationsDao().deleteApplication(app)
                }
            }

            installedAppsIntents.forEach { appIntent ->
                val existingApp = dbApps.find { dbApp -> dbApp.packageName == appIntent.activityInfo.packageName }
                val app = ApplicationsModel(
                    packageName = appIntent.activityInfo.packageName,
                    name = appIntent.loadLabel(packageManager).toString(),
                    isPinned = existingApp?.isPinned == true,
                    limitTimeReached = existingApp?.limitTimeReached == true
                )
                db.applicationsDao().upsertApplication(app)
            }
        }
    }

    fun getAppNameInLocale(packageName: String, locale: Locale): String? {
        val packageManager = application.packageManager
        var appName: String? = null

        try {
            val applicationInfo = packageManager.getApplicationInfo(packageName, 0)

            val configuration = Configuration()
            configuration.setLocale(locale)

            val localizedContext: Context = application.createConfigurationContext(configuration)

            val appResources =
                localizedContext.packageManager.getResourcesForApplication(applicationInfo)

            appName = if (applicationInfo.labelRes != 0) {
                appResources.getString(applicationInfo.labelRes)
            } else {
                applicationInfo.nonLocalizedLabel?.toString()
            }

        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return appName
    }

    fun searchOnInternet(
        text: String,
        searchEngine: SearchEnginesEnum = SearchEnginesEnum.GOOGLE
    ) {
        val query = Uri.encode(text)
        val url = searchEngine.baseUrl + query
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        // Ensure there's an app to handle the intent
        application.startActivity(intent)
    }
}