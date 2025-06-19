package com.brightbox.dino.view.applications_limit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.viewmodel.LimitsViewModel
import com.brightbox.dino.view.theme.LocalSpacing

@Composable
fun ApplicationListToSetLimitsComponent(
    modifier: Modifier = Modifier,
    limitsViewModel: LimitsViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val state = limitsViewModel.state.collectAsState()
    val icons = state.value.appIcons
    val selectedApplicationsToLimit = state.value.selectedApplicationsToLimit

    if (!state.value.appList.isEmpty() && icons.isNotEmpty()) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
            modifier = modifier
//                .padding(spacing.spaceSmall)
        ) {
            items(
                items = state.value.appList,
                key = { it.packageName }
            ) { application ->
                val isSelectedToLimit =
                    selectedApplicationsToLimit.containsKey(application.packageName)
                val icon = icons[application.packageName]

                ApplicationLimitComponent(
                    application = application,
                    icon = icon!!,
                    selectedApplicationsToLimit = selectedApplicationsToLimit,
                    isSelectedToLimit = isSelectedToLimit,
                    onLimitsEvent = limitsViewModel::onEvent
                )
            }

        }
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
        ) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
    }

}