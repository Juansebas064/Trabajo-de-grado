package com.brightbox.hourglass.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class CategoriesModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
//    val color: String
)
