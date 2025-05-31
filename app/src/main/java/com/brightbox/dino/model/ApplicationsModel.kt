package com.brightbox.dino.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class ApplicationsModel(
    @PrimaryKey(autoGenerate = false)
    val packageName: String,
    val name: String,
    val isPinned: Boolean,
    val limitTimeReached: Boolean,
)
