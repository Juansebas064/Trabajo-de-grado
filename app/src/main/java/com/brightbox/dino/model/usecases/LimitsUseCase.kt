package com.brightbox.dino.model.usecases

import android.app.AppOpsManager
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.os.Process
import android.util.Log
import com.brightbox.dino.config.DinoDatabase
import com.brightbox.dino.model.LimitsModel
import com.brightbox.dino.services.TimeLimitService
import com.brightbox.dino.utils.formatMillisecondsToMinutes
import com.brightbox.dino.utils.getStartOfTodayMillisInUTC
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LimitsUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val db: DinoDatabase
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

    fun registerTimeLimitService() {
        val serviceIntent = Intent(context, TimeLimitService::class.java)
        context.startService(serviceIntent)
    }

    fun getTodayUsageStats(): Map<String, Int> {
        val usageStatsManager =
            context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        val usageStats = usageStatsManager.queryUsageStats(
            UsageStatsManager.INTERVAL_DAILY,
            getStartOfTodayMillisInUTC(),
            System.currentTimeMillis()
        )

//        Log.d("LimitsUseCase", "Usage stats: $usageStats")

        val flatUsageStats = mutableMapOf<String, Int>()

        usageStats.forEach { stat ->
            flatUsageStats[stat.packageName] =
                formatMillisecondsToMinutes(stat.totalTimeInForeground).toInt()
//            if (stat.totalTimeInForeground > 0) {
//                Log.d(
//                    "LimitsUseCase",
//                    "date: ${formatMillisecondsToSQLiteDate(stat.lastTimeStamp)}: ${stat.packageName}, ${
//                        formatMillisecondsToMinutes(stat.totalTimeInForeground)
//                    }\n"
//                )
//            }
        }

        return flatUsageStats
    }

    fun getLimits(): Flow<List<LimitsModel>> = db.limitsDao().getLimits().map { limitsList ->
        limitsList.sortedByDescending { it.usedTime }
    }

    suspend fun upsertLimit(limit: LimitsModel) {
        withContext(Dispatchers.IO) {
            val updatedLimit = syncLimit(limit)
            db.limitsDao().upsertLimit(updatedLimit)
        }
    }

    suspend fun updateLimit(limit: LimitsModel) {
        withContext(Dispatchers.IO) {
            Log.d("LimitsUseCase", "Updating limit: ${limit}")
            db.limitsDao().upsertLimit(limit)
        }
    }

    suspend fun syncLimit(limit: LimitsModel): LimitsModel {
        val stats = getTodayUsageStats()
        Log.d(
            "LimitsUseCase",
            "Updating limit: ${limit},\nstats: ${stats[limit.applicationPackageName]}"
        )
        val currentTimeInForeground = stats[limit.applicationPackageName]!!
        val previousTimeInForeground = when (limit.usedTime) {
            0 -> currentTimeInForeground
            else -> limit.usedTime
        }

        if (currentTimeInForeground >= limit.timeLimit) {
            withContext(Dispatchers.IO) {
                db.applicationsDao().updateLimitTimeReached(limit.applicationPackageName, true)
            }
        }

        return limit.copy(
            usedTime = currentTimeInForeground,
            previousUsedTime = previousTimeInForeground
        )

    }

    suspend fun resetLimitsAtMidnight(limitList: List<LimitsModel>) {
        withContext(Dispatchers.IO) {
            limitList.forEach { limit ->
                val limitReset = limit.copy(
                    usedTime = 0,
                    previousUsedTime = 0
                )
                upsertLimit(limitReset)
                db.applicationsDao().updateLimitTimeReached(limit.applicationPackageName, false)
            }
        }
    }

    suspend fun deleteLimit(id: Int) {
        withContext(Dispatchers.IO) {
            val limit = db.limitsDao().getLimitById(id)
            val app = db.applicationsDao().findByPackageName(limit.applicationPackageName)
            db.limitsDao().deleteLimit(id)
            db.applicationsDao().upsertApplication(app!!.copy(limitTimeReached = false))
        }
    }

}