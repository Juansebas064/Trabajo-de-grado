package com.brightbox.dino.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoriesModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val deleted: Boolean = false
)
