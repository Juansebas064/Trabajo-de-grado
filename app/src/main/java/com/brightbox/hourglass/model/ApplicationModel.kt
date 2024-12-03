package com.brightbox.hourglass.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "applications")
data class ApplicationModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val packageName: String,
    val isPinned: Boolean,
    val isRestricted: Boolean,
)
