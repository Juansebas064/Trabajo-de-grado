package com.brightbox.hourglass.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brightbox.hourglass.data.ApplicationsDao
import com.brightbox.hourglass.data.CategoriesDao
import com.brightbox.hourglass.data.TasksDao
import com.brightbox.hourglass.model.ApplicationsModel
import com.brightbox.hourglass.model.CategoriesModel
import com.brightbox.hourglass.model.TasksModel

@Database(
    entities = [
        ApplicationsModel::class,
        TasksModel::class,
        CategoriesModel::class
    ],
    version = 1
)
abstract class HourglassDatabase : RoomDatabase() {
    abstract fun applicationsDao(): ApplicationsDao
    abstract fun tasksDao(): TasksDao
    abstract fun categoriesDao(): CategoriesDao
}
