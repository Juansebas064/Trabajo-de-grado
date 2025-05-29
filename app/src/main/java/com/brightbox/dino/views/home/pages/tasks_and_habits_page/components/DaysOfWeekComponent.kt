package com.brightbox.dino.views.home.pages.tasks_and_habits_page.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.brightbox.dino.R
import com.brightbox.dino.constants.daysOfWeek
import com.brightbox.dino.views.common.RoundedSquareButtonComponent
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun DaysOfWeekComponent(
    today: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val daysOfWeek = context.resources.getStringArray(R.array.days_of_week).toList()
    Log.d("DaysOfWeekComponent", "today: $daysOfWeek")
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
        modifier = modifier
            .clickable {
                onClick()
            }
    ) {
        daysOfWeek.forEach { day ->
            if (today == day) {
                RoundedSquareButtonComponent(
                    text = today.replaceFirstChar { char ->
                        char.uppercase()
                    },
                    padding = spacing.spaceSmall + spacing.spaceExtraSmall,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    textStyle = MaterialTheme.typography.bodyLarge,
                    containerColor = MaterialTheme.colorScheme.surface,
                    onClick = { onClick() },
                    modifier = Modifier
                )
            }
            else {
                Text(
                    text = day[0].uppercase(),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}