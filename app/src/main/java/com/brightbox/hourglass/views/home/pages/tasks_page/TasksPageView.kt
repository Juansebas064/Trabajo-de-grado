package com.brightbox.hourglass.views.home.pages.tasks_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.views.theme.LocalSpacing
import com.brightbox.hourglass.viewmodel.ApplicationsViewModel
import com.brightbox.hourglass.viewmodel.CategoriesViewModel
import com.brightbox.hourglass.viewmodel.TasksViewModel

@Composable
fun TasksPageView(
    modifier: Modifier = Modifier,
    onNavigateToSettings: () -> Unit,
    applicationsViewModel: ApplicationsViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {

        IconButton(
            onClick = {
                onNavigateToSettings()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
            modifier = modifier
        ) {
            DateAndTimeView(
                openApp = applicationsViewModel::openApp,
            )
            TasksView(
//                modifier = Modifier.padding(horizontal = spacing.tasksPadding)
            )
        }
        EssentialShortcutsBarView(
            openApp = applicationsViewModel::openApp,
            modifier = Modifier
                .align(Alignment.BottomEnd)
        )
    }
}