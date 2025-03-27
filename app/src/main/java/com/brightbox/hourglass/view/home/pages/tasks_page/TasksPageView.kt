package com.brightbox.hourglass.view.home.pages.tasks_page

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.brightbox.hourglass.view.home.components.ClockComponent
import com.brightbox.hourglass.view.theme.LocalSpacing
import com.brightbox.hourglass.viewmodel.AppsViewModel

@Composable
fun TasksPageView(
    appsViewModel: AppsViewModel,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
        ) {
            DateAndTimeView(appsViewModel)
        }
        EssentialShortcutsBarView(
            appsViewModel = appsViewModel,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
    }
}