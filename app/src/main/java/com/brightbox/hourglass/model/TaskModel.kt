package com.brightbox.hourglass.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.brightbox.hourglass.constants.TaskTypeEnum

@Entity(
    tableName = "tasks",
    foreignKeys = [ForeignKey(
        entity = CategoryModel::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"]
    )]
)
data class TaskModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val title: String,
    val description: String,
    val priority: Int,
    val type: TaskTypeEnum,
    val dateCreated: Long,  // Will be saved in milliseconds
    val dateCompleted: Long?,  // Will be saved in milliseconds
    val isCompleted: Boolean,
    val wasDelayed: Boolean,
    val categoryId: Int?
)
