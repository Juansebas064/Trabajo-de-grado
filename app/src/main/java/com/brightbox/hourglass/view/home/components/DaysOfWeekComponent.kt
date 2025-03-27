package com.brightbox.hourglass.view.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.view.theme.LocalSpacing

@Composable
fun DaysOfWeekComponent(
    currentDay: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val daysOfWeek = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
        modifier = modifier
            .clickable {
                onClick()
            }
    ) {
        daysOfWeek.forEach { day ->
            if (currentDay.startsWith(day)) {
                PilledTextButtonComponent(
                    text = currentDay,
                    textColor = MaterialTheme.colorScheme.onSurface,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    onClick = { onClick() },
                )
            }
            else {
                Text(
                    text = "${day[0]}",
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}