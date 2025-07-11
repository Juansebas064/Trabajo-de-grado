package com.brightbox.dino.view.home.pages.tasks_and_habits_page.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.brightbox.dino.model.constants.EssentialShortcutsEnum
import com.brightbox.dino.view.theme.LocalSpacing

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
//            .background(MaterialTheme.colorScheme.surface)
//            .padding(spacing.default)
    ) {
        Icon(
            imageVector = essentialShortcutEnum.icon,
            contentDescription = essentialShortcutEnum.contentDescription,
            tint = MaterialTheme.colorScheme.onBackground
        )
    }
}