package com.brightbox.hourglass.view.home.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.model.ApplicationModel
import com.brightbox.hourglass.viewmodel.AppsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuAppListComponent(
    appsViewModel: AppsViewModel,
    apps: List<ApplicationModel>,
    appShowingOptions: String,
    focusManager: FocusManager,
    modifier: Modifier,
) {

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(0.dp, Alignment.Bottom),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        items(items = apps) { app ->

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = rememberRipple(
                            color = MaterialTheme.colorScheme.onBackground
                                .copy(alpha = 0f)
                        ),
                        onClick = {
                            if (appShowingOptions == app.packageName) {
                                appsViewModel.setAppShowingOptions("none")
                            } else {
                                if (appShowingOptions != "none") {
                                    appsViewModel.setAppShowingOptions("none")
                                } else {
                                    appsViewModel.openApp(app.packageName)
                                    focusManager.clearFocus()
                                }
                            }
                        },
                        onLongClick = {
                            appsViewModel.setAppShowingOptions(app.packageName)
                        }
                    )
                    .background(
                        if (appShowingOptions == app.packageName)
                            MaterialTheme.colorScheme.surface
                        else
                            MaterialTheme.colorScheme.background
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
                                appsViewModel.toggleAppPinnedState(app)
                                appsViewModel.setAppShowingOptions("none")
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
                                appsViewModel.openAppInfo(app)
                                appsViewModel.setAppShowingOptions("none")
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
                                appsViewModel.uninstallApp(app.packageName)
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
            }
        }
    }
}
