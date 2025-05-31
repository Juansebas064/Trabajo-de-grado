package com.brightbox.dino.views.home.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.currentStateAsState
import com.brightbox.dino.R
import com.brightbox.dino.viewmodel.ApplicationsViewModel
import com.brightbox.dino.viewmodel.preferences.PreferencesViewModel
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun MenuAppListComponent(
    applicationsViewModel: ApplicationsViewModel = hiltViewModel(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    focusManager: FocusManager,
    modifier: Modifier,
) {
    val spacing = LocalSpacing.current
    val searchText = applicationsViewModel.searchText.collectAsState()
    val apps by applicationsViewModel.filteredAppList.collectAsState()
    val appShowingOptions by applicationsViewModel.appShowingOptions.collectAsState()
    val context = LocalContext.current
    val lifeCycle = LocalLifecycleOwner.current.lifecycle.currentStateAsState()
    val preferencesState = preferencesViewModel.state.collectAsState()

    LaunchedEffect(lifeCycle.value) {
        applicationsViewModel.queryInstalledApplicationsToDatabase()
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        itemsIndexed(
            items = apps.applications,
            key = { _, app -> app.packageName }
        ) { index, app ->

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(spacing.spaceSmall))
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        onClick = {
                            if (appShowingOptions == app.packageName) {
                                applicationsViewModel.setAppShowingOptions("none")
                            } else {
                                if (appShowingOptions != "none") {
                                    applicationsViewModel.setAppShowingOptions("none")
                                } else {
                                    applicationsViewModel.openApp(app)
                                    focusManager.clearFocus()
                                }
                            }
                        },
                        onLongClick = {
                            applicationsViewModel.setAppShowingOptions(app.packageName)
                        }
                    )
                    .background(
                        if (appShowingOptions == app.packageName)
                            MaterialTheme.colorScheme.surface
                        else
                            Color.Transparent
                    )
                    .fillMaxWidth()
                    .padding(
                        vertical = spacing.spaceExtraSmall,
                        horizontal = spacing.spaceSmall
                    )
                    .animateItem()
                    .animateContentSize()
                    .height(IntrinsicSize.Min)
            ) {
                AnimatedVisibility(
                    visible = app.isPinned,
                    enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                ) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "Pinned",
                        tint = if (appShowingOptions != app.packageName)
                            MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface,
                    )
                }

                Spacer(Modifier.width(spacing.spaceSmall))

                Text(
                    text = app.name,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Start,
                    softWrap = false,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = if (appShowingOptions != app.packageName)
                        MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .weight(3f)
                )

                AnimatedVisibility(
                    visible = appShowingOptions == app.packageName,
                    enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .width(150.dp)
                            .weight(1.5f)
                            .padding(3.dp)
                            .fillMaxHeight()
                    ) {
                        // Separator
//                        Box(
//                            modifier = Modifier
//                                .width(spacing.default)
//                                .fillMaxHeight(0.6f)
//                                .clip(RoundedCornerShape(spacing.spaceLarge))
//                                .background(MaterialTheme.colorScheme.onSurface.copy(0.5f))
//
//                        )
                        // Pin
                        IconButton(
                            onClick = {
                                applicationsViewModel.toggleAppPinnedState(app)
                                applicationsViewModel.setAppShowingOptions("none")
                            },
                            modifier = Modifier
                                .height(45.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .weight(1f)
                                .padding(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PushPin,
                                contentDescription = "Pin",
                                tint = if (app.isPinned) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.surface,
                            )
                        }
                        // App info
                        IconButton(
                            onClick = {
                                applicationsViewModel.openAppInfo(app)
                                applicationsViewModel.setAppShowingOptions("none")
                            },
                            modifier = Modifier
                                .height(45.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.secondary)
                                .weight(1f)
                                .padding(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Info",
                                tint = MaterialTheme.colorScheme.surface,
                            )
                        }
                        // Delete
                        IconButton(
                            onClick = {
                                applicationsViewModel.uninstallApp(app.packageName)
                            },
                            modifier = Modifier
                                .height(45.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.error)
                                .weight(1f)
                                .padding(0.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = MaterialTheme.colorScheme.surface,
                            )
                        }
                    }
                }

                if (appShowingOptions != app.packageName && index == 0) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowRightAlt,
                        contentDescription = "Return",
                        tint = MaterialTheme.colorScheme.onBackground,
                    )
                }
            }
        }

        if (searchText.value.isNotEmpty() && preferencesState.value.showSearchOnInternet) {
            item {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .combinedClickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(
                                color = MaterialTheme.colorScheme.onBackground
                                    .copy(alpha = 0f)
                            ),
                            onClick = {
                                applicationsViewModel.searchOnInternet()
                            },
                        )
                        .background(Color.Transparent)
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min)
                        .padding(vertical = spacing.spaceMedium)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .weight(1f)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = context.getString(R.string.search),
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .scale(0.85f)
                        )

                        Text(
                            text = "${context.getString(R.string.search)} \"${searchText.value}\"",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Start,
                            softWrap = false,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = context.getString(R.string.on_the_internet),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Start,
                            softWrap = false,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    if (apps.applications.isEmpty()) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowRightAlt,
                            contentDescription = "Return",
                            tint = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }
        }
    }
}
