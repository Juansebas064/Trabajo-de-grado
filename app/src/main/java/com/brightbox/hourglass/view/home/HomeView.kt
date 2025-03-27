package com.brightbox.hourglass.view.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.brightbox.hourglass.view.home.menu.PinnedAppsAndMenuModalView
import com.brightbox.hourglass.view.home.pages.tasks_page.TasksPageView
import com.brightbox.hourglass.viewmodel.AppsViewModel
import com.brightbox.hourglass.viewmodel.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(homeViewModel: HomeViewModel, appsViewModel: AppsViewModel) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    val pagerState = rememberPagerState(
        initialPage = 1,
        pageCount = { 3 }
    )

    val coroutineScope = rememberCoroutineScope()

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

//    LaunchedEffect(pagerState) {
//        Log.d("HomeView", "Current page: ${pagerState.currentPage}")
//    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        HorizontalPager(
            modifier = Modifier
                .weight(1f),
            state = pagerState,
        ) { page ->
            when (page) {
                0 -> {
                    Text(
                        text = "Page ${pagerState.currentPage}",
                        color = Color.Red
                    )
                }

                1 -> {
                    TasksPageView(
                        appsViewModel = appsViewModel,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                2 -> {
                    Text(
                        text = "Page ${pagerState.currentPage}",
                        color = Color.Green
                    )
                }
            }
        }
        PinnedAppsAndMenuModalView(
            sheetState = sheetState,
            appsViewModel = appsViewModel,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
        )
    }

}