package com.brightbox.hourglass.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brightbox.hourglass.data.ApplicationDao
import com.brightbox.hourglass.data.CategoryDao
import com.brightbox.hourglass.data.TaskDao
import com.brightbox.hourglass.model.ApplicationModel

@Database(
    entities = [ApplicationModel::class],
    version = 1
)
abstract class HourglassDatabase : RoomDatabase() {
    abstract fun applicationDao(): ApplicationDao
    abstract fun taskDao(): TaskDao
    abstract fun categoryDao(): CategoryDao
}
