package com.brightbox.hourglass.views.applications_limit

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Timelapse
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.brightbox.hourglass.events.LimitsEvent
import com.brightbox.hourglass.viewmodel.LimitsViewModel
import com.brightbox.hourglass.views.applications_limit.components.PermissionRequesterDialog
import com.brightbox.hourglass.views.common.ApplicationComponent
import com.brightbox.hourglass.views.common.NavigationButton
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun ApplicationsLimitView(
    modifier: Modifier = Modifier,
    onNavigateToSelectApplicationToLimit: () -> Unit,
    onNavigateBack: () -> Unit,
    limitsViewModel: LimitsViewModel = hiltViewModel()
) {
    val isUsageAccessPermissionGranted =
        limitsViewModel.isUsageAccessPermissionGranted.collectAsState()

    val isSystemAlertWindowPermissionGranted =
        limitsViewModel.isSystemAlertWindowPermissionGranted.collectAsState()

    val state = limitsViewModel.state.collectAsState()

    var requestingPermissions by remember {
        mutableStateOf<Boolean>(
            (false)
        )
    }

    var exit by remember {
        mutableStateOf(false)
    }

    val lifecycle = LocalLifecycleOwner.current

    val spacing = LocalSpacing.current

    LaunchedEffect(
        isUsageAccessPermissionGranted.value,
        isSystemAlertWindowPermissionGranted.value,
        lifecycle.lifecycle.currentStateAsState().value,
    ) {
        limitsViewModel.onEvent(LimitsEvent.CheckUsageAccessPermission)
        limitsViewModel.onEvent(LimitsEvent.CheckSystemAlertWindowPermission)

        limitsViewModel.onEvent(LimitsEvent.SyncLimits)

        if (isUsageAccessPermissionGranted.value && isSystemAlertWindowPermissionGranted.value) {
            requestingPermissions = false
        } else if (!exit) {
            requestingPermissions = true
        }
    }

    LaunchedEffect(exit) {
        if (exit) {
            onNavigateBack()
        } else if (!isUsageAccessPermissionGranted.value || !isSystemAlertWindowPermissionGranted.value) {
            requestingPermissions = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .navigationBarsPadding()
            .statusBarsPadding()
    ) {

        if (requestingPermissions) {
            PermissionRequesterDialog(
                onDismissRequest = {
                    requestingPermissions = it
                    exit = !it
                },
                isUsageAccessPermissionGranted = isUsageAccessPermissionGranted.value,
                isSystemAlertWindowPermissionGranted = isSystemAlertWindowPermissionGranted.value,
            )
        }

        if (isUsageAccessPermissionGranted.value && isSystemAlertWindowPermissionGranted.value) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
                modifier = Modifier
                    .padding(spacing.spaceMedium)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing.spaceExtraLarge)
                ) {
                    Text(
                        text = "Applications time limit",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )

                    Spacer(
                        Modifier.height(spacing.spaceSmall)
                    )

                    Text(
                        text = "Today usage",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Center
                    )
                }

                // Limits items
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(items = state.value.limitsList, key = { it.id!! }) { limit ->
                        val application =
                            state.value.appList.find { it.packageName == limit.applicationPackageName }
                        Box(

                        ) {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        1.dp,
                                        MaterialTheme.colorScheme.onBackground.copy(0.5f),
                                        RoundedCornerShape(spacing.spaceSmall)
                                    )
                                    .padding(spacing.spaceMedium)
                            ) {
                                ApplicationComponent(
                                    application = application!!,
                                    textStyle = MaterialTheme.typography.bodyMedium,
                                    textColor = MaterialTheme.colorScheme.onBackground,
                                    modifier = Modifier
                                        .weight(1f)
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
                                    modifier = Modifier
                                ) {
                                    Text(
                                        text = "${limit.usedTime} / ${limit.timeLimit} min",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onBackground
                                    )
                                    Icon(
                                        imageVector = Icons.Default.Timelapse,
                                        tint = MaterialTheme.colorScheme.onBackground,
                                        contentDescription = "Time used"
                                    )
                                }
                            }
                        }
                    }
                }

                // Go to select apps
                Button(
                    onClick = {
                        onNavigateToSelectApplicationToLimit()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    shape = RoundedCornerShape(spacing.spaceSmall),
                    contentPadding = PaddingValues(spacing.spaceMedium),
                    modifier = Modifier

                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "Select applications",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
                            contentDescription = "Set application limit",
                            modifier = Modifier
                                .scale(0.7f)
                        )
                    }
                }

            }
        }

        // Close settings
        NavigationButton(
            modifier = Modifier.align(Alignment.TopStart),
            icon = Icons.Default.Close,
            color = MaterialTheme.colorScheme.onBackground,
            description = "Close",
            onNavigate = {
                onNavigateBack()
            }
        )
    }
}
