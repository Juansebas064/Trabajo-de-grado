package com.brightbox.dino.views.common

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.brightbox.dino.views.theme.LocalSpacing

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