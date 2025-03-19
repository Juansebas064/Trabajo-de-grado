package com.brightbox.hourglass.view

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.viewmodel.AppsViewModel
import com.brightbox.hourglass.viewmodel.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(homeViewModel: HomeViewModel, appsViewModel: AppsViewModel) {

    val scaffoldState = rememberBottomSheetScaffoldState()
    val view = LocalView.current

    rememberSystemUiController().setNavigationBarColor(
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.1f),
        darkIcons = !isSystemInDarkTheme()
    )

    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        Log.d(
            "LaunchedEffect(scaffoldState.bottomSheetState.currentValue)",
            "${scaffoldState.bottomSheetState.currentValue}"
        )
    }

    // Container
//    Column(
//        verticalArrangement = Arrangement.Bottom,
//        modifier = Modifier
//            .fillMaxSize()
//            .background(MaterialTheme.colorScheme.background)
//            .navigationBarsPadding()
//            .statusBarsPadding()
//    )
//    {
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetShape = RectangleShape,
//            sheetShadowElevation = 0.dp,
        sheetContent = {
            AppMenu(
                appsViewModel = appsViewModel,
                scaffoldState = scaffoldState
            )
        },
//            sheetPeekHeight = IntrinsicSize.Min.toD,
        sheetDragHandle = {
            if (scaffoldState.bottomSheetState.hasPartiallyExpandedState) {
                Log.d(
                    "HomeView",
                    "State of BottomSheet is ${scaffoldState.bottomSheetState.currentValue}"
                )
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .border(1.dp, Color.Red)
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowUp,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            } else false

        },
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .animateContentSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Bottom,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
//                .padding(innerPadding)
        )
        {
            PinnedApps(
                appsViewModel = appsViewModel,
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .animateContentSize(animationSpec = tween(durationMillis = 1000))
                    .border(1.dp, Color.Red)
//                    .padding(innerPadding)

//                        .offset(y = if (scaffoldState.bottomSheetState.targetValue == SheetValue.Expanded) (-70).dp else -15.dp)
            )
        }
    }
}
//}

//.pointerInput(Unit) {
//    detectVerticalDragGestures { change, dragAmount ->
//        if (dragAmount < -10) { // Detect upward swipe
//            isMenuVisible = true
//            Log.d("detectVerticalDragGestures", "upward swipe detected")
//        } else if (dragAmount > 10) { // Detect downward swipe
//            isMenuVisible = false
//            Log.d("detectVerticalDragGestures", "downward swipe detected")
//        }
//    }
//}

//            .pointerInput(Unit) {
//                detectVerticalDragGestures { _, dragAmount ->
//                    scope.launch {
//                        if (dragAmount < -10) { // Detect upward swipe
//                            isVisible.value = true
//                        } else if (dragAmount > 10) { // Detect downward swipe
//                            isVisible.value = false
//                        }
//                    }
//                }
//            }