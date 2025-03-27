package com.brightbox.hourglass.view.home.pages.tasks_page

import android.content.Intent
import android.provider.AlarmClock
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.view.home.components.ClockComponent
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

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .padding(vertical = spacing.spaceExtraLarge, horizontal = spacing.spaceLarge)

    ) {
        ClockComponent(
            currentTimeMillis = currentTimeMillis.longValue,
            onClick = {
                appsViewModel.openApp(Intent(AlarmClock.ACTION_SHOW_ALARMS))
            }
        )



    }

}