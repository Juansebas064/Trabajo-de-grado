package com.brightbox.hourglass.views.home.menu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.viewmodel.preferences.PreferencesViewModel
import com.brightbox.hourglass.views.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuView(
    sheetState: SheetState,
    preferencesViewModel: PreferencesViewModel = hiltViewModel()
) {
    // States
    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current
    val generalPreferencesState by preferencesViewModel.state.collectAsState()
    val spacing = LocalSpacing.current

    // Open keyboard when menu is opened
    LaunchedEffect(sheetState) {
        Log.d("MenuView", "Opening keyboard value set to ${generalPreferencesState.openKeyboardInAppMenu}")
        if (sheetState.hasExpandedState && generalPreferencesState.openKeyboardInAppMenu) {
            focusRequester.requestFocus()
        }
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
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
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
            )

            Spacer(Modifier.height(spacing.spaceSmall))

            Text(
                text = "Hold an app for more options",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // App list
        MenuAppListComponent(
            focusManager = focusManager,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.3f)
        )
        // Search bar
        SearchBarView(
            focusRequester = focusRequester,
            focusManager = focusManager,
            modifier = Modifier
//                .fillMaxWidth()
        )
    }
}



