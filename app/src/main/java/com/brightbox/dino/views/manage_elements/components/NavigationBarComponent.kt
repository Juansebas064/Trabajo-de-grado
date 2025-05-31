package com.brightbox.dino.views.manage_elements.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.brightbox.dino.R
import com.brightbox.dino.views.common.RoundedSquareButtonComponent
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun NavigationBarComponent(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    onButtonClick: (Int) -> Unit
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .padding(
                top = spacing.spaceMedium,
                bottom = 0.dp,
                start = spacing.spaceMedium,
                end = spacing.spaceMedium
            )
            .fillMaxWidth(),
    ) {
        RoundedSquareButtonComponent(
            text = context.getString(R.string.tasks),
            textStyle = MaterialTheme.typography.bodyMedium,
            contentColor = if (selectedIndex == 0)
                MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground,
            containerColor = if (selectedIndex == 0)
                MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
            padding = spacing.spaceMedium,
            onClick = {
                onButtonClick(0)
            },
            circleShape = false,
            modifier = Modifier.weight(1f)
        )

        RoundedSquareButtonComponent(
            text = context.getString(R.string.habits),
            textStyle = MaterialTheme.typography.bodyMedium,
            contentColor = if (selectedIndex == 1)
                MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground,
            containerColor = if (selectedIndex == 1)
                MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
            padding = spacing.spaceMedium,
            onClick = {
                onButtonClick(1)
            },
            circleShape = false,
            modifier = Modifier.weight(1f)
        )

        RoundedSquareButtonComponent(
            text = context.getString(R.string.categories),
            textStyle = MaterialTheme.typography.bodyMedium,
            contentColor = if (selectedIndex == 2)
                MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onBackground,
            containerColor = if (selectedIndex == 2)
                MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background,
            padding = spacing.spaceMedium,
            onClick = {
                onButtonClick(2)
            },
            circleShape = false,
            modifier = Modifier.weight(1f)
        )
    }
}