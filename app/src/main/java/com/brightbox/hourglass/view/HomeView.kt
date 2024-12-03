package com.brightbox.hourglass.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.viewmodel.AppsViewModel
import com.brightbox.hourglass.viewmodel.HomeViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(homeViewModel: HomeViewModel, appsViewModel: AppsViewModel) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    var isMenuVisible by remember { mutableStateOf(false) }

    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        Log.d(
            "LaunchedEffect(scaffoldState.bottomSheetState.currentValue)",
            "${scaffoldState.bottomSheetState.currentValue}"
        )
    }

    rememberSystemUiController().setNavigationBarColor(
        color = MaterialTheme.colorScheme.background.copy(alpha = 0.1f),
        darkIcons = !isSystemInDarkTheme()
    )

    // Container
    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .navigationBarsPadding()
            .pointerInput(Unit) {
                detectVerticalDragGestures { change, dragAmount ->
                    if (dragAmount < -10) { // Detect upward swipe
                        isMenuVisible = true
                    }
                }
            }
    )
//     {
//        if (!isMenuVisible) {
//            PinnedApps()
//        }
//    }
//    if (isMenuVisible) {
//        AppMenu(appsViewModel = appsViewModel)
//    }

    {

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetShape = RectangleShape,
            sheetShadowElevation = 0.dp,
            sheetContent = {
                AppMenu(appsViewModel = appsViewModel)
            },
//            sheetPeekHeight = 30.dp,
            sheetDragHandle = {
                if (scaffoldState.bottomSheetState.currentValue != SheetValue.Expanded) {
                    PinnedApps()
                }
            },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.background)
                .navigationBarsPadding()
        ) {

        }
    }


}


@Composable
fun PinnedApps() {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .border(1.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
//                .padding(vertical = 20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Pinned apps...",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}