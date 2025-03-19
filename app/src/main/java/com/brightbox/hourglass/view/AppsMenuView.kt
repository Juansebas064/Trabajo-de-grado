package com.brightbox.hourglass.view

import android.util.Log
import android.view.KeyEvent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.nativeKeyCode
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.viewmodel.AppsViewModel
import com.brightbox.hourglass.view.components.AppColumnListComponent
import com.brightbox.hourglass.view.components.SearchBarComponent
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMenu(
    appsViewModel: AppsViewModel,
    sheetState: SheetState,
) {

    // States
    val apps by appsViewModel.filteredAppList.collectAsState()
    val searchText by appsViewModel.searchText.collectAsState()
    val isKeyboardOpen by appsViewModel.isKeyboardOpened.collectAsState()
    val appShowingOptions by appsViewModel.appShowingOptions.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()

    // Open keyboard when menu is opened
    LaunchedEffect(sheetState) {
        if (sheetState.hasExpandedState) {
            focusRequester.requestFocus()
        }
    }

    BackHandler(enabled = sheetState.isVisible) {
        scope.launch { sheetState.hide() }
    }

    // Parent container
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        // Menu text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(
                    fill = true,
                    weight = 1f
                )
        ) {
            Text(
                text = "Menu",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        // Filters area
        FiltersBox(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.2f)
                .padding(vertical = 16.dp)
        )
        // App list
        AppColumnListComponent(
            appsViewModel = appsViewModel,
            apps = apps.applications,
            appShowingOptions = appShowingOptions,
            focusManager = focusManager,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.3f)
        )
        // Search bar
        SearchBarComponent(
            appsViewModel = appsViewModel,
            searchText = searchText,
            focusRequester = focusRequester,
            focusManager = focusManager,
            isKeyboardOpen = isKeyboardOpen,
            modifier = Modifier
                .fillMaxWidth()
                .imePadding()
                .padding(vertical = 16.dp)
        )
    }
}

@Composable
fun FiltersBox(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {
        Text(
            text = "Filters",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}


