package com.brightbox.hourglass.view.home.pages.tasks_page

import android.content.Intent
import android.provider.AlarmClock
import android.text.format.DateUtils
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.view.home.components.ClockComponent
import com.brightbox.hourglass.view.home.components.DateComponent
import com.brightbox.hourglass.view.home.components.DaysOfWeekComponent
import com.brightbox.hourglass.view.theme.LocalSpacing
import com.brightbox.hourglass.viewmodel.AppsViewModel
import kotlinx.coroutines.delay

@Composable
fun DateAndTimeView(
    appsViewModel: AppsViewModel,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current

    val currentTimeMillis = remember {
        mutableLongStateOf(System.currentTimeMillis())
    }

    LaunchedEffect(key1 = currentTimeMillis) {
        while (true) {
            delay(250)
            currentTimeMillis.longValue = System.currentTimeMillis()
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .padding(vertical = spacing.spaceExtraLarge)
    ) {
        ClockComponent(
            currentTime = currentTimeMillis.longValue,
            onClick = { appsViewModel.openApp(Intent(AlarmClock.ACTION_SHOW_ALARMS)) }
        )

        DaysOfWeekComponent(
            currentDay = DateUtils.formatDateTime(
                LocalContext.current,
                currentTimeMillis.longValue,
                DateUtils.FORMAT_SHOW_WEEKDAY
            ),
            onClick = {
                appsViewModel.openApp(
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_APP_CALENDAR)
                )
            }
        )

        DateComponent(
            currentTimeMillis = currentTimeMillis.longValue,
            onClick = {
                appsViewModel.openApp(
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_APP_CALENDAR)
                )
            }
        )

    }

}