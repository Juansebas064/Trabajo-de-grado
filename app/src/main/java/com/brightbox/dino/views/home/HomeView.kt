package com.brightbox.dino.views.home

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.viewmodel.HomeViewModel
import com.brightbox.dino.views.home.menu.PinnedAppsAndMenuModalView
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
                }
            }
            PinnedAppsAndMenuModalView(
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

}