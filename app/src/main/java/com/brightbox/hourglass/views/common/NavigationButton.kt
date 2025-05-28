package com.brightbox.hourglass.views.common

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun NavigationButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    color: Color,
    description: String,
    onNavigate: () -> Unit
) {
    val spacing = LocalSpacing.current
    // Close settings
    IconButton(
        onClick = {
            onNavigate()
        },
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = color
        ),
        modifier = modifier
    ) {
        Icon(
            imageVector = icon,
            contentDescription = description,
        )
    }
}