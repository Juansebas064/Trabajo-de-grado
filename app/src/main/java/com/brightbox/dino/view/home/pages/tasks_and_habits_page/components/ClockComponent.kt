package com.brightbox.dino.view.home.pages.tasks_and_habits_page.components

import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

@Composable
fun ClockComponent(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentTime = remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    LaunchedEffect(key1 = currentTime) {
        while (true) {
            delay(1000)
            currentTime.longValue = System.currentTimeMillis()
        }
    }

    Text(
        text = DateUtils.formatDateTime(
            LocalContext.current,
            currentTime.value,
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