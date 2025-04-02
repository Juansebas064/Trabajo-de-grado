package com.brightbox.hourglass.view.home.components

import android.content.Intent
import android.provider.AlarmClock
import android.text.format.DateUtils
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.brightbox.hourglass.viewmodel.AppsViewModel
import javax.inject.Inject

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