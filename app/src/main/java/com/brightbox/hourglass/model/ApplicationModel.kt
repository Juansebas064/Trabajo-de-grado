package com.brightbox.hourglass.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class ApplicationModel(
    @PrimaryKey(autoGenerate = false)
    val packageName: String,
    val name: String,
    val isPinned: Boolean,
    val isRestricted: Boolean,
)
