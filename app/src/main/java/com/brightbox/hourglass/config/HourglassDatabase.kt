package com.brightbox.hourglass.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brightbox.hourglass.adapters.ApplicationDao
import com.brightbox.hourglass.model.ApplicationModel

@Database(entities = [ApplicationModel::class], version = 1)
abstract class HourglassDatabase : RoomDatabase() {
    abstract fun applicationDao(): ApplicationDao
}
