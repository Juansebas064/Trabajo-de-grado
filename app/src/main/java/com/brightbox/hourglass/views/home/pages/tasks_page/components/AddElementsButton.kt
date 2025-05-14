package com.brightbox.hourglass.views.home.pages.tasks_page.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.viewmodel.CategoriesViewModel
import com.brightbox.hourglass.viewmodel.TasksViewModel
import kotlin.math.roundToInt

@Composable
fun AddElementsButton(
    modifier: Modifier = Modifier,
    tasksViewModel: TasksViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var refPosition by remember { mutableStateOf(Offset.Zero) }
    val density = LocalDensity.current

//    val (addButton, subButtons) =

    // FAB “normal” en tu Scaffold, Activity, etc.
    FloatingActionButton(
        onClick = { expanded = true },
        modifier = Modifier
            .onGloballyPositioned { coordinates ->
                // posición relativa al root de este Box
                refPosition = coordinates.positionInRoot()
            }
    ) {
        Icon(if (expanded) Icons.Default.Clear else Icons.Default.Add, null)
    }

    if (expanded) {
        Popup(
//            alignment = Alignment.BottomEnd,
            onDismissRequest = { expanded = false }
        ) {
            // Aquí pones tu scrim y tu menú:
            Box(
                Modifier
                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.4f))
                    .clickable { expanded = false }
            ) {
                AnimatedVisibility(
                    visible = expanded,
                    enter = fadeIn(animationSpec = tween(200)) + scaleIn(),
                    exit = fadeOut(animationSpec = tween(200)) + scaleOut()
                ) {
                    Column(
                        Modifier
//                        .padding(16.dp)
                            .offset {
                                IntOffset(
                                    x = refPosition.x.roundToInt(),
                                    y = (refPosition.y - with(density) { 80.dp.toPx() }).roundToInt()
                                )
                            },
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FabMenuItem(
                            icon = Icons.Default.Add,
                            label = "Add task",
                            onClick = {
                                tasksViewModel.onEvent(TasksEvent.ShowAddTaskDialog)
                                expanded = false
                            },
                        )

                    }
                }
            }
        }
    }

//    IconButton(
//        onClick = {
//            tasksViewModel.onEvent(TasksEvent.ShowAddTaskDialog)
//        },
//        modifier = Modifier
//            .clip(RoundedCornerShape(16.dp))
//            .background(MaterialTheme.colorScheme.surface)
////                        .align(Alignment.Center)
//    ) {
//
//    }
}

@Composable
private fun FabMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    ElevatedCard(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.width(12.dp))
            Icon(imageVector = icon, contentDescription = label)
        }
    }
}