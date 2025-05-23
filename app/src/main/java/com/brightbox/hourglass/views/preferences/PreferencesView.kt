package com.brightbox.hourglass.views.preferences

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.events.preferences.GeneralPreferencesEvent
import com.brightbox.hourglass.viewmodel.preferences.PreferencesViewModel
import com.brightbox.hourglass.views.preferences.components.ToggleComponent
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun PreferencesView(
    modifier: Modifier = Modifier,
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    onNavigateUp: () -> Unit,
) {
    val spacing = LocalSpacing.current
    val state = preferencesViewModel.state.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(spacing.spaceLarge)
        ) {
            Box(
                modifier = Modifier
                    .padding(vertical = spacing.spaceExtraLarge)
            ) {
                Text(
                    text = "Settings",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .weight(1f)
            ) {
                ToggleComponent(
                    text = "Open keyboard automatically in app menu",
                    checked = state.value.openKeyboardInAppMenu,
                    onCheckedChange = {
                        preferencesViewModel.onEvent(
                            GeneralPreferencesEvent.SetOpenKeyboardInAppMenu(it)
                        )
                    }
                )

                ToggleComponent(
                    text = "Show solid background",
                    checked = state.value.solidBackground,
                    onCheckedChange = {
                        preferencesViewModel.onEvent(
                            GeneralPreferencesEvent.SetSolidBackground(it)
                        )
                    }
                )
            }
        }

        // Go back
        IconButton(
            onClick = {
                onNavigateUp()
            },
            modifier = Modifier
                .align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Settings",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}