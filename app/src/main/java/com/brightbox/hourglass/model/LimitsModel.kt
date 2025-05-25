package com.brightbox.hourglass.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "limits",
    foreignKeys = [
        ForeignKey(
            entity = ApplicationsModel::class,
            parentColumns = ["packageName"],
            childColumns = ["applicationPackageName"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class LimitsModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val applicationPackageName: String,
    val dateCreated: String,
    val timeLimit: Int,  // Time in minutes
    val usedTime: Int  // Time in minutes
)