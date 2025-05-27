package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.constants.daysOfWeek
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun DaysSelectorComponent(
    modifier: Modifier = Modifier,
    habitsDaysOfWeek: List<String>,
    setHabitsDaysOfWeek: (List<String>) -> Unit
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(
            spacing.spaceExtraSmall,
            Alignment.CenterHorizontally
        )
    ) {
        daysOfWeek.forEach { day ->
            Button(
                shape = RoundedCornerShape(spacing.spaceSmall),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (day in habitsDaysOfWeek) {
                        MaterialTheme.colorScheme.secondary
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    contentColor = if (day in habitsDaysOfWeek) {
                        MaterialTheme.colorScheme.onSecondary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                ),
                contentPadding = PaddingValues(0.dp),
                onClick = {
                    if (day in habitsDaysOfWeek) {
                        setHabitsDaysOfWeek(habitsDaysOfWeek.filter { it != day })
                    } else {
                        setHabitsDaysOfWeek(habitsDaysOfWeek + day)
                    }
                },
                modifier = Modifier.size(37.dp)
            ) {
                Text(
                    text = day[0].uppercase(),
                    style = MaterialTheme.typography.bodySmall
                )
            }

        }
    }
}