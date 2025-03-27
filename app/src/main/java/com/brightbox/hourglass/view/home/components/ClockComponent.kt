package com.brightbox.hourglass.view.home.components

import android.content.Intent
import android.provider.AlarmClock
import android.text.format.DateUtils
import android.widget.TextClock
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.view.theme.LocalSpacing
import com.brightbox.hourglass.viewmodel.AppsViewModel
import kotlinx.coroutines.delay

@Composable
fun ClockComponent(
    currentTimeMillis: Long,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Text(
        text = DateUtils.formatDateTime(
            LocalContext.current,
            currentTimeMillis,
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