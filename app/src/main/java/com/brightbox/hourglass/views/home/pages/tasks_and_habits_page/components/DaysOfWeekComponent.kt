package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.brightbox.hourglass.views.common.PilledTextButtonComponent
import com.brightbox.hourglass.views.theme.LocalSpacing

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
                    modifier = Modifier
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