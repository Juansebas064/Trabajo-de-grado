package com.brightbox.dino.views.applications_limit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.R
import com.brightbox.dino.events.LimitsEvent
import com.brightbox.dino.viewmodel.LimitsViewModel
import com.brightbox.dino.views.applications_limit.components.ApplicationListToSetLimitsComponent
import com.brightbox.dino.views.common.NavigationButton
import com.brightbox.dino.views.common.RoundedSquareButtonComponent
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun SelectApplicationsToLimitView(
    modifier: Modifier = Modifier,
    limitsViewModel: LimitsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val spacing = LocalSpacing.current
    val state = limitsViewModel.state.collectAsState()
    val context = LocalContext.current

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
            .imePadding()
            .padding(horizontal = spacing.spaceMedium)
    ) {
        Column {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = spacing.spaceExtraLarge, bottom = spacing.spaceMedium)
            ) {
                Text(
                    text = context.getString(R.string.tap_to_mark_or_unmark_an_app),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            ApplicationListToSetLimitsComponent(
                modifier = Modifier
                    .weight(1f)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = spacing.spaceMedium, horizontal = 0.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                RoundedSquareButtonComponent(
                    text = context.getString(R.string.done),
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