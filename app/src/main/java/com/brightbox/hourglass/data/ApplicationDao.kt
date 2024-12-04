package com.brightbox.hourglass.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.brightbox.hourglass.model.ApplicationModel

@Dao
interface ApplicationDao {
    @Upsert
    suspend fun upsertApplication(application: ApplicationModel)

    @Delete
    suspend fun deleteApplication(application: ApplicationModel)

    @Query("SELECT * FROM applications ORDER BY name ASC")
    suspend fun getApplications(): List<ApplicationModel>

    @Query("SELECT * FROM applications WHERE packageName LIKE :packageName ORDER BY name ASC")
    suspend fun findByPackageName(packageName: String?): ApplicationModel
}