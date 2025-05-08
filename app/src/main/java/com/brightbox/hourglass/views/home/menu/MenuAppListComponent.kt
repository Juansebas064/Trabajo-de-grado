package com.brightbox.hourglass.views.home.menu

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRightAlt
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.KeyboardReturn
import androidx.compose.material.icons.filled.ArrowRightAlt
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardReturn
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.model.ApplicationsModel
import com.brightbox.hourglass.viewmodel.ApplicationsViewModel
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun MenuAppListComponent(
    applicationsViewModel: ApplicationsViewModel = hiltViewModel(),
    focusManager: FocusManager,
    modifier: Modifier,
) {
    val spacing = LocalSpacing.current
    val searchText = applicationsViewModel.searchText.collectAsState()
    val apps by applicationsViewModel.filteredAppList.collectAsState()
    val appShowingOptions by applicationsViewModel.appShowingOptions.collectAsState()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        itemsIndexed(items = apps.applications) { index, app ->

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
                            if (appShowingOptions == app.packageName) {
                                applicationsViewModel.setAppShowingOptions("none")
                            } else {
                                if (appShowingOptions != "none") {
                                    applicationsViewModel.setAppShowingOptions("none")
                                } else {
                                    applicationsViewModel.openApp(app.packageName)
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
                        imageVector = Icons.Default.Star,
                        contentDescription = "Pinned",
                        tint = if (appShowingOptions != app.packageName)
                            MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 2.dp)
                    )
                }
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
                        .padding(vertical = 16.dp, horizontal = 8.dp)
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
                    ) {
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
                                imageVector = Icons.Default.Star,
                                contentDescription = "Pin",
                                tint = MaterialTheme.colorScheme.surface,
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
        if (searchText.value.isNotEmpty()) {
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
                    ) {
                        Icon(
                            imageVector = Icons.Default.Language,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier
                                .scale(0.85f)
                        )

                        Text(
                            text = "Search \"${searchText.value}\" on internet",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Start,
                            softWrap = false,
                            maxLines = 1,
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
