package com.brightbox.dino.views.home.pages.tasks_and_habits_page

import android.content.Intent
import android.provider.AlarmClock
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.utils.formatMillisecondsToDay
import com.brightbox.dino.viewmodel.TimeViewModel
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.ClockComponent
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.DateComponent
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.DaysOfWeekComponent
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun DateAndTimeView(
    modifier: Modifier = Modifier,
    openApp: (Intent) -> Unit,
    timeViewModel: TimeViewModel = hiltViewModel()
) {

    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val today = timeViewModel.currentTimeMillis.collectAsState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .padding(vertical = spacing.spaceExtraSmall)
    ) {
        ClockComponent(
            onClick = { openApp(Intent(AlarmClock.ACTION_SHOW_ALARMS)) }
        )
        val dayOfToday = formatMillisecondsToDay(today.value)
        DaysOfWeekComponent(
            today = dayOfToday,
            onClick = {
                openApp(
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_APP_CALENDAR)
                )
            }
        )

        DateComponent(
            currentTimeMillis = today.value,
            onClick = {
                openApp(
                    Intent(Intent.ACTION_MAIN)
                        .addCategory(Intent.CATEGORY_APP_CALENDAR)
                )
            }
        )

    }

}