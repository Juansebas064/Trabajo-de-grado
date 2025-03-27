package com.brightbox.hourglass.model

import android.content.Intent
import android.provider.MediaStore
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Folder
import androidx.compose.ui.graphics.vector.ImageVector

enum class EssentialShortcutsEnum(
    val icon: ImageVector,
    val contentDescription: String,
    val intent: Intent
) {
    EMAIL(
        Icons.Default.Email,
        "Email",
        Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_APP_EMAIL)
    ),
    CAMERA(
        Icons.Default.CameraAlt,
        "Camera",
        Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA)
    ),
    FILE_EXPLORER(
        Icons.Default.Folder,
        "File explorer",
        Intent(Intent.ACTION_GET_CONTENT).setType("file/*")
    ),
    PHONE(
        Icons.Default.Call,
        "Phone",
        Intent(Intent.ACTION_DIAL)
    )
}