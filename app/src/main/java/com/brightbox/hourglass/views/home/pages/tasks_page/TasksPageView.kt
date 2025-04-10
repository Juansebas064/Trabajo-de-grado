package com.brightbox.hourglass.views.home.pages.tasks_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.brightbox.hourglass.views.theme.LocalSpacing
import com.brightbox.hourglass.viewmodel.ApplicationsViewModel
import com.brightbox.hourglass.viewmodel.TasksViewModel

@Composable
fun TasksPageView(
    applicationsViewModel: ApplicationsViewModel,
    tasksViewModel: TasksViewModel,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
            modifier = modifier
        ) {
            DateAndTimeView(applicationsViewModel)
            TasksView(
                tasksViewModel,
                modifier = Modifier.padding(horizontal = spacing.tasksPadding)
            )
        }
        EssentialShortcutsBarView(
            applicationsViewModel,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
    }
}