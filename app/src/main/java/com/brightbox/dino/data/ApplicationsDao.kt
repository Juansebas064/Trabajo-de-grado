package com.brightbox.dino.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.dino.model.ApplicationsModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ApplicationsDao {
    @Upsert
    suspend fun upsertApplication(application: ApplicationsModel)

    @Delete
    suspend fun deleteApplication(application: ApplicationsModel)

    @Query("SELECT * FROM applications ORDER BY name ASC")
    fun getApplications(): Flow<List<ApplicationsModel>>

    @Query("SELECT * FROM applications ORDER BY name ASC")
    fun getApplicationsForQueryInstalledApps(): List<ApplicationsModel>

    @Query("SELECT * FROM applications WHERE packageName = :packageName")
    suspend fun findByPackageName(packageName: String?): ApplicationsModel?

    @Query("UPDATE applications SET limitTimeReached = :limitTimeReached WHERE packageName = :packageName")
    suspend fun updateLimitTimeReached(packageName: String, limitTimeReached: Boolean)
}