package com.brightbox.hourglass.usecases

import android.app.Application
import android.content.Intent
import android.net.Uri
import androidx.room.Room
import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.model.ApplicationModel

class AppUseCase(private val application: Application) {

    private val db: HourglassDatabase = Room.databaseBuilder(
        application.applicationContext,
        HourglassDatabase::class.java,
        "hourglass_database"
    ).build()

    fun openApp(packageName: String) {
        val packageManager = application.packageManager
        val intent = packageManager.getLaunchIntentForPackage(packageName)
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            application.applicationContext.startActivity(intent)
        }
    }

    suspend fun toggleAppPinnedState(app: ApplicationModel) {
        val pinnedApp = app.copy(isPinned = !app.isPinned)
        db.applicationDao().upsertApplication(pinnedApp)
    }

    fun openAppInfo(packageName: String) {
        val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.applicationContext.startActivity(intent)
    }

    fun uninstallApp(app: ApplicationModel) {
        val intent = Intent(Intent.ACTION_DELETE).apply {
            data = Uri.parse("package:${app.packageName}")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        application.applicationContext.startActivity(intent)
    }
}