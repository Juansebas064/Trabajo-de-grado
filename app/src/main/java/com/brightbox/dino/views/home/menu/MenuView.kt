package com.brightbox.dino.views.home.menu

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.R
import com.brightbox.dino.viewmodel.preferences.PreferencesViewModel
import com.brightbox.dino.views.theme.LocalSpacing

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
    val context = LocalContext.current

    // Open keyboard when menu is opened
    LaunchedEffect(sheetState) {
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
                text = context.getString(R.string.menu),
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleLarge,
            )

            Spacer(Modifier.height(spacing.spaceSmall))

            Text(
                text = context.getString(R.string.press_and_hold_an_app_for_more_options),
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



