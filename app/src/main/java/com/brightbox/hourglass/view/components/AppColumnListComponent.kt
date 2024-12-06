package com.brightbox.hourglass.view.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.model.ApplicationModel
import com.brightbox.hourglass.view.theme.Yellow
import com.brightbox.hourglass.viewmodel.AppsViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AppColumnListComponent(
    appsViewModel: AppsViewModel,
    apps: List<ApplicationModel>,
    focusManager: FocusManager,
    modifier: Modifier,
) {
    val appShowingOptions = remember { mutableStateOf("none") }
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
                        indication = null,
//                        indication = rememberRipple(
//                            color = MaterialTheme.colorScheme.onBackground
//                                .copy(alpha = 0f)
//                        ),
                        onClick = {
                            if (appShowingOptions.value == app.packageName) {
                                appShowingOptions.value = "none"
                            } else {
                                appsViewModel.openApp(app.packageName)
                                focusManager.clearFocus()
                                appShowingOptions.value = "none"
                            }
                        },
                        onLongClick = {
                            appShowingOptions.value = app.packageName
                        }
                    )
                    .background(
                        if (appShowingOptions.value == app.packageName)
                            MaterialTheme.colorScheme.surface
                        else
                            MaterialTheme.colorScheme.background
                    )
                    .fillMaxWidth()
                    .animateItem()
                    .animateContentSize()
                    .height(IntrinsicSize.Min)
            ) {
                Text(
                    text = app.name,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    softWrap = false,
                    color = (
                            if (appShowingOptions.value == app.packageName)
                                MaterialTheme.colorScheme.onSurface
                            else
                                MaterialTheme.colorScheme.onBackground
                            ),
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                )

                AnimatedVisibility(
                    visible = appShowingOptions.value == app.packageName,
                    enter = slideInHorizontally(initialOffsetX = { it }) + fadeIn(),
                    exit = slideOutHorizontally(targetOffsetX = { it }) + fadeOut()
                ) {
                    Row(
                    ) {
                        // Pin
                        IconButton(
                            onClick = { Unit },
                            modifier = Modifier
                                .fillParentMaxHeight()
                                .width(20.dp)
                                .background(MaterialTheme.colorScheme.tertiary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = "Pin",
                                tint = MaterialTheme.colorScheme.surface,
                            )
                        }
                        // App info
                        IconButton(
                            onClick = { Unit },
                            modifier = Modifier
                                .fillParentMaxHeight()
                                .width(20.dp)
                                .background(MaterialTheme.colorScheme.secondary)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Info",
                                tint = MaterialTheme.colorScheme.surface,
                            )
                        }
                        // Delete
                        IconButton(
                            onClick = { Unit },
                            modifier = Modifier
                                .fillParentMaxHeight()
                                .width(20.dp)
                                .background(MaterialTheme.colorScheme.error)
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


//Row(
//horizontalArrangement = Arrangement.SpaceBetween,
//modifier = Modifier
//.clip(RoundedCornerShape(10.dp))
//.clickable(
//interactionSource = remember { MutableInteractionSource() },
//indication = rememberRipple(
//color = MaterialTheme.colorScheme.onBackground.copy(
//alpha = 1f
//)
//),
//onLongClick = {
//    // Change style and show options
//    // Implement the logic to change the style and show options
//}
//) {
//    appsViewModel.openApp(app.packageName)
//    focusManager.clearFocus()
//}
//.fillMaxWidth()
//.padding(vertical = 16.dp, horizontal = 8.dp)
//) {
//    Text(
//        text = app.name,
//        style = MaterialTheme.typography.bodyLarge,
//        color = MaterialTheme.colorScheme.onBackground,
//        modifier = Modifier.align(Alignment.CenterVertically)
//    )
//    // Options that slide in from the right
//    AnimatedVisibility(
//        visible = /* condition to show options */,
//        enter = slideInHorizontally(initialOffsetX = { it }),
//        exit = slideOutHorizontally(targetOffsetX = { it })
//    ) {
//        Row(
//            horizontalArrangement = Arrangement.End,
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            // Add your options here
//            Text(
//                text = "Option 1",
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier.padding(horizontal = 8.dp)
//            )
//            Text(
//                text = "Option 2",
//                style = MaterialTheme.typography.bodyLarge,
//                color = MaterialTheme.colorScheme.onBackground,
//                modifier = Modifier.padding(horizontal = 8.dp)
//            )
//        }
//    }
//}