package com.brightbox.hourglass.model

import android.content.Intent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Call
import androidx.compose.ui.graphics.vector.ImageVector

enum class EssentialShortcutsEnum(
    val icon: ImageVector,
    val contentDescription: String,
    val intent: Intent
) {
    PHONE(
        Icons.Default.Call,
        "Phone",
        Intent(Intent.ACTION_DIAL)
    ),
}