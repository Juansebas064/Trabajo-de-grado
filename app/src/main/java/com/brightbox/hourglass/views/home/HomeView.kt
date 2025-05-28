package com.brightbox.hourglass.views.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudSync
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.events.preferences.GeneralPreferencesEvent
import com.brightbox.hourglass.viewmodel.HomeViewModel
import com.brightbox.hourglass.viewmodel.preferences.PreferencesViewModel
import com.brightbox.hourglass.views.common.NavigationButton
import com.brightbox.hourglass.views.home.menu.PinnedAppsAndMenuModalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun HomeView(
    homeViewModel: HomeViewModel = hiltViewModel(),
    onNavigateToPreferences: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 2 }
    )
    val context = LocalContext.current

    rememberSystemUiController().apply {
        setNavigationBarColor(
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.1f),
            darkIcons = !isSystemInDarkTheme()
        )
        setStatusBarColor(
            color = MaterialTheme.colorScheme.background.copy(alpha = 0.1f),
            darkIcons = !isSystemInDarkTheme()
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < 10) { // Detect down swipe
                        homeViewModel.expandNotificationsPanel()
                    }
                }
            }
    ) {
        Column(

        ) {
            HorizontalPager(
                modifier = Modifier
                    .weight(1f),
                state = pagerState,
            ) { page ->
                when (page) {
                    0 -> {
                        PomodoroPageView(
                            onNavigateToPreferences = onNavigateToPreferences
                        )
                    }

                    1 -> {
                        TasksAndHabitsPageView(
                            modifier = Modifier.fillMaxSize(),
                            onNavigateToPreferences = onNavigateToPreferences
                        )
                    }

//                2 -> {
//                    Text(
//                        text = "Page 3",
//                        color = Color.Green
//                    )
//                }
                }
            }
            PinnedAppsAndMenuModalView(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

}