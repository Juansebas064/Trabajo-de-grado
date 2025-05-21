package com.brightbox.hourglass.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brightbox.hourglass.data.ApplicationsDao
import com.brightbox.hourglass.data.CategoriesDao
import com.brightbox.hourglass.data.HabitsDao
import com.brightbox.hourglass.data.HabitsLogsDao
import com.brightbox.hourglass.data.TasksDao
import com.brightbox.hourglass.model.ApplicationsModel
import com.brightbox.hourglass.model.CategoriesModel
import com.brightbox.hourglass.model.HabitsLogsModel
import com.brightbox.hourglass.model.HabitsModel
import com.brightbox.hourglass.model.TasksModel

@Database(
    entities = [
        ApplicationsModel::class,
        TasksModel::class,
        CategoriesModel::class,
        HabitsModel::class,
        HabitsLogsModel::class
    ],
    version = 1
)
abstract class HourglassDatabase : RoomDatabase() {
    abstract fun applicationsDao(): ApplicationsDao
    abstract fun tasksDao(): TasksDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun habitsDao(): HabitsDao
    abstract fun habitsLogsDao(): HabitsLogsDao
}
