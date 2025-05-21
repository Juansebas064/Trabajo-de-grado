package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionOnScreen
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.events.HabitsEvent
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.viewmodel.HabitsViewModel
import com.brightbox.hourglass.viewmodel.TasksViewModel
import kotlin.math.roundToInt

@Composable
fun AddElementsButton(
    modifier: Modifier = Modifier,
    tasksViewModel: TasksViewModel = hiltViewModel(),
    habitsViewModel: HabitsViewModel = hiltViewModel(),
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var refPosition by remember { mutableStateOf(Offset.Zero) }
    val density = LocalDensity.current

    IconButton(
        onClick = {
            expanded = !expanded
        },
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .onGloballyPositioned { coordinates ->
                // posición relativa al root de este Box
                refPosition = coordinates.positionOnScreen()
            }
            .zIndex(10f)
    ) {
        Icon(if (expanded) Icons.Default.Clear else Icons.Default.Add, null)
    }

    AnimatedVisibility(
        visible = expanded,
        enter = fadeIn(),
        exit = fadeOut(animationSpec = tween(100)) + scaleOut()
    ) {

        Popup(
//            alignment = Alignment.BottomEnd,
            onDismissRequest = { expanded = false }
        ) {
            // Aquí pones tu scrim y tu menú:
            Box(
                Modifier
                    .fillMaxSize()
//                    .background(Color.Black.copy(alpha = 0.4f))
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null,
                        onClick = { expanded = false },
                    )
                    .zIndex(1f)
            ) {

                Column(
                    Modifier
                        .fillMaxWidth()
                        .offset {
                            IntOffset(
                                x = 0,
                                y = (refPosition.y - with(density) { 115.dp.toPx() }).roundToInt()
                            )
                        }
                        ,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AnimatedVisibility(
                        visible = expanded,
                        enter = fadeIn(animationSpec = tween(200)) + scaleIn(),
                        exit = fadeOut(animationSpec = tween(100)) + scaleOut()
                    ) {

                        FabMenuItemComponent(
                            label = "Habit",
                            onClick = {
                                expanded = false
                                habitsViewModel.onEvent(HabitsEvent.ShowAddHabitDialog)
                            },
                        )

                    }

                    AnimatedVisibility(
                        visible = expanded,
                        enter = fadeIn() + scaleIn(),
                        exit = fadeOut(animationSpec = tween(100)) + scaleOut()
                    ) {
                        FabMenuItemComponent(
                            label = "Task",
                            onClick = {
                                expanded = false
                                tasksViewModel.onEvent(TasksEvent.ShowAddTaskDialog)
                            },
                        )
                    }
                }
            }
        }
    }
}

