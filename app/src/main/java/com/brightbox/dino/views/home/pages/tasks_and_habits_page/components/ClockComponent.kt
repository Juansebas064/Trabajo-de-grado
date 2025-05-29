package com.brightbox.dino.views.home.pages.tasks_and_habits_page.components

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

@Composable
fun ClockComponent(
    currentTime: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Text(
        text = DateUtils.formatDateTime(
            LocalContext.current,
            currentTime,
            DateUtils.FORMAT_SHOW_TIME
        ),
        modifier = Modifier
            .clickable {
                onClick()
            },
        color = MaterialTheme.colorScheme.onBackground,
        style = MaterialTheme.typography.displayLarge
    )
}