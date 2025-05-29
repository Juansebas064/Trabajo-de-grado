package com.brightbox.dino.views.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.events.preferences.GeneralPreferencesEvent
import com.brightbox.dino.viewmodel.ApplicationsViewModel
import com.brightbox.dino.viewmodel.preferences.PreferencesViewModel
import com.brightbox.dino.views.common.NavigationButton
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.DateAndTimeView
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.EssentialShortcutsBarView
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.TasksAndHabitsListView
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun TasksAndHabitsPageView(
    modifier: Modifier = Modifier,
    onNavigateToPreferences: () -> Unit,
    applicationsViewModel: ApplicationsViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val preferencesViewModel: PreferencesViewModel = hiltViewModel()
    val preferencesState = preferencesViewModel.state.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
//            .border(1.dp, Color.Red)
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
            modifier = modifier
        ) {
            DateAndTimeView(
                openApp = applicationsViewModel::openApp,
            )
            TasksAndHabitsListView(
            )
        }

        if (!preferencesState.value.isLoading && preferencesState.value.showEssentialShortcuts) {
            EssentialShortcutsBarView(
                openApp = applicationsViewModel::openApp,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
            )
        }

        NavigationButton(
            modifier = Modifier.align(Alignment.TopStart),
            icon = Icons.Default.Settings,
            color = MaterialTheme.colorScheme.onBackground,
            description = "Settings",
            onNavigate = {
                onNavigateToPreferences()
            }
        )

        NavigationButton(
            modifier = Modifier.align(Alignment.TopEnd),
            icon = when (preferencesState.value.theme) {
                "system" -> Icons.Default.CloudSync
                "light" -> Icons.Default.LightMode
                "dark" -> Icons.Default.DarkMode
                else -> Icons.Default.CloudSync
            },
            color = MaterialTheme.colorScheme.onBackground,
            description = "Change theme",
            onNavigate = {
                preferencesViewModel.onEvent(
                    GeneralPreferencesEvent.ChangeTheme
                )
            }
        )
    }
}