package com.brightbox.hourglass.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.hourglass.model.ApplicationModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationDao {
    @Upsert
    suspend fun upsertApplication(application: ApplicationModel)

    @Delete
    suspend fun deleteApplication(application: ApplicationModel)

    @Query("SELECT * FROM applications ORDER BY name ASC")
    fun getApplications(): Flow<List<ApplicationModel>>

    @Query("SELECT * FROM applications ORDER BY name ASC")
    fun getApplicationsForQueryInstalledApps(): List<ApplicationModel>

    @Query("SELECT * FROM applications WHERE packageName = :packageName")
    suspend fun findByPackageName(packageName: String?): ApplicationModel?
}