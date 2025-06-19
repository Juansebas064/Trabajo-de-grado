package com.brightbox.dino.config

import androidx.room.Database
import androidx.room.RoomDatabase
import com.brightbox.dino.model.database.ApplicationsDao
import com.brightbox.dino.model.database.CategoriesDao
import com.brightbox.dino.model.database.HabitsDao
import com.brightbox.dino.model.database.HabitsLogsDao
import com.brightbox.dino.model.database.LimitsDao
import com.brightbox.dino.model.database.TasksDao
import com.brightbox.dino.model.ApplicationsModel
import com.brightbox.dino.model.CategoriesModel
import com.brightbox.dino.model.HabitsLogsModel
import com.brightbox.dino.model.HabitsModel
import com.brightbox.dino.model.LimitsModel
import com.brightbox.dino.model.TasksModel

@Database(
    entities = [
        ApplicationsModel::class,
        TasksModel::class,
        CategoriesModel::class,
        HabitsModel::class,
        HabitsLogsModel::class,
        LimitsModel::class
    ],
    version = 1
)
abstract class DinoDatabase : RoomDatabase() {
    abstract fun applicationsDao(): ApplicationsDao
    abstract fun tasksDao(): TasksDao
    abstract fun categoriesDao(): CategoriesDao
    abstract fun habitsDao(): HabitsDao
    abstract fun habitsLogsDao(): HabitsLogsDao
    abstract fun limitsDao(): LimitsDao
}
