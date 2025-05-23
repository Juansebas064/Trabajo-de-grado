package com.brightbox.hourglass.views.home

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import com.brightbox.hourglass.views.home.menu.PinnedAppsAndMenuModalView
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@Composable
fun HomeView(
    onNavigateToPreferences: () -> Unit,
) {
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 3 }
    )
    val coroutineScope = rememberCoroutineScope()
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < 10) { // Detect upward swipe
                        val toast = Toast.makeText(context, "Downward swipe detected", Toast.LENGTH_SHORT)
                        toast.show()
//                        AccessibilityService.instance?.openShade()
//                            ?: run { /* servicio no habilitado → pide al usuario */ }
//                        coroutineScope.launch {
//
//                        }
                        Log.d("HomeView", "Downward swipe detected")
                    }
                }
            }
    ) {
        HorizontalPager(
            modifier = Modifier
                .weight(1f),
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> {
                    Text(
                        text = "Page 1",
                        color = Color.Red
                    )
                }

                1 -> {
                    TasksAndHabitsPageView(
                        onNavigateToSettings = onNavigateToPreferences,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                2 -> {
                    Text(
                        text = "Page 3",
                        color = Color.Green
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