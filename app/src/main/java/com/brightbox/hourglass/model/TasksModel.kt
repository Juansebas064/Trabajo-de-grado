package com.brightbox.hourglass.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks",
    foreignKeys = [
        ForeignKey(
            entity = CategoriesModel::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"]
        )
    ]
)

data class TasksModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val title: String,
    val description: String,
    val priority: Int,
    val dateCreated: String?,
    val dateDue: String?,
    val dateCompleted: String?,
    val isCompleted: Boolean,
    val wasDelayed: Boolean,
    val categoryId: Int?
)
