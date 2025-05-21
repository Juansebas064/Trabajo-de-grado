package com.brightbox.hourglass.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "habits",
    foreignKeys = [
        ForeignKey(
            entity = CategoriesModel::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        )
    ]
)
data class HabitsModel (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val categoryId: Int?,
    val daysOfWeek: String,
    val startDate: String,
    val endDate: String?,
    val deleted: Boolean
)