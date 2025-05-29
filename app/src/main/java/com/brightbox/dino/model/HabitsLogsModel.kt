package com.brightbox.dino.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "habits_logs",
    foreignKeys = [
        ForeignKey(
            entity = HabitsModel::class,
            parentColumns = ["id"],
            childColumns = ["habitId"]
        )
    ]
)
data class HabitsLogsModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val habitId: Int,
    val date: String,
    val completed: Boolean
)