package com.brightbox.hourglass.usecases

import android.app.AppOpsManager
import android.app.usage.UsageStats
import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.Process
import android.util.Log
import com.brightbox.hourglass.config.HourglassDatabase
import com.brightbox.hourglass.model.HabitsModel
import com.brightbox.hourglass.model.LimitsModel
import com.brightbox.hourglass.utils.formatMillisecondsToMinutes
import com.brightbox.hourglass.utils.formatMillisecondsToSQLiteDate
import com.brightbox.hourglass.utils.formatSQLiteDateToMilliseconds
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LimitsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val db: HourglassDatabase
) {
    fun isUsageAccessPermissionGranted(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode =
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                Process.myUid(),
                context.packageName
            )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun isSystemAlertPermissionGranted(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode =
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW,
                Process.myUid(),
                context.packageName
            )
        return mode == AppOpsManager.MODE_ALLOWED
    }

    fun getUsageStats(): MutableList<UsageStats>? {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            formatSQLiteDateToMilliseconds("2025-05-20"),
            formatSQLiteDateToMilliseconds("2025-05-23")
        )
        usageStats.forEach { stat ->
            System.currentTimeMillis()
            if (stat.totalTimeInForeground > 0) {
//                Log.d(
//                    "LimitsUseCase",
//                    "date: ${formatMillisecondsToSQLiteDate(stat.lastTimeStamp)}: ${stat.packageName}, ${
//                        formatMillisecondsToMinutes(
//                            stat.totalTimeInForeground
//                        )
//                    }\n"
//                )
            }
        }
        return usageStats
    }

    fun getLimits(): Flow<List<LimitsModel>> = db.limitsDao().getLimits()

    suspend fun upsertLimit(limit: LimitsModel) {
        withContext(Dispatchers.IO) {
            db.limitsDao().upsertLimit(limit)
        }
    }

    suspend fun deleteLimit(id: Int) {
        withContext(Dispatchers.IO) {
            db.limitsDao().deleteLimit(id)
        }
    }

}