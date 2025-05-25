package com.brightbox.hourglass.views.applications_limit

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.events.LimitsEvent
import com.brightbox.hourglass.viewmodel.LimitsViewModel
import com.brightbox.hourglass.views.applications_limit.components.ApplicationListToSetLimitsComponent
import com.brightbox.hourglass.views.common.NavigationButton
import com.brightbox.hourglass.views.common.RoundedSquareButtonComponent
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun SelectApplicationsToLimitView(
    modifier: Modifier = Modifier,
    limitsViewModel: LimitsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val spacing = LocalSpacing.current
    val state = limitsViewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        limitsViewModel.onEvent(LimitsEvent.ShowApplicationsList)
    }

    LaunchedEffect(state.value.limitsList) {
        if (state.value.showApplicationsList) {
            limitsViewModel.onEvent(LimitsEvent.ShowApplicationsList)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.spaceExtraLarge)
            ) {
                Text(
                    text = "Tap to mark or unmark an application",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            ApplicationListToSetLimitsComponent(
                modifier = Modifier
                    .weight(1f)
            )

        }

        // Done Button
        AnimatedVisibility(
            visible = state.value.selectedApplicationsToLimit.isNotEmpty(),
            enter = slideInVertically(
                // Anima desde la parte inferior de la pantalla (o del contenedor)
                // initialOffsetY = { fullHeight -> fullHeight } // Para deslizar desde completamente fuera de la pantalla
                initialOffsetY = { it } // Para deslizar desde justo debajo de su posición final
            ),
            exit = slideOutVertically(
                // Anima hacia la parte inferior de la pantalla (o del contenedor)
                // targetOffsetY = { fullHeight -> fullHeight } // Para deslizar hacia completamente fuera de la pantalla
                targetOffsetY = { it } // Para deslizar hacia justo debajo de su posición inicial
            ),
            modifier = Modifier
                .align(Alignment.BottomEnd)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceSmall),
                horizontalArrangement = Arrangement.Center
            ) {
                RoundedSquareButtonComponent(
                    text = "Done",
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        limitsViewModel.onEvent(LimitsEvent.SaveApplicationLimits)
                        onNavigateBack()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }
        }

        // Go back
        NavigationButton(
            modifier = Modifier.align(Alignment.TopStart),
            icon = Icons.AutoMirrored.Filled.ArrowBack,
            color = MaterialTheme.colorScheme.onBackground,
            description = "Go back to apps limit overview",
            onNavigate = { onNavigateBack() }
        )
    }

}