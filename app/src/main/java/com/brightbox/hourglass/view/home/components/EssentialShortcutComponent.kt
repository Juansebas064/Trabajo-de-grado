package com.brightbox.hourglass.view.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.model.EssentialShortcutsEnum
import com.brightbox.hourglass.view.theme.LocalSpacing

@Composable
fun EssentialShortcutComponent(
    essentialShortcutEnum: EssentialShortcutsEnum,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    IconButton(
        onClick = onClick,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(spacing.spaceExtraSmall)
    ) {
        Icon(
            imageVector = essentialShortcutEnum.icon,
            contentDescription = essentialShortcutEnum.contentDescription,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}