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
import com.brightbox.hourglass.views.common.IconButtonComponent
import com.brightbox.hourglass.views.common.RoundedSquareButtonComponent
import com.brightbox.hourglass.views.theme.LocalSpacing
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

    val spacing = LocalSpacing.current

    Box(
        modifier = modifier
    ) {
        IconButtonComponent(
            modifier = Modifier.zIndex(1f),
            onClick = {
                expanded = !expanded
            },
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface,
            icon = if (expanded) Icons.Default.Clear else Icons.Default.Add,
            contentDescription = "Add elements"
        )

        Popup(
            alignment = Alignment.Center,
            onDismissRequest = { expanded = false },
            offset = IntOffset(x = 0, y = -220)
        ) {

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn(animationSpec = tween(300)) + scaleIn(),
                exit = fadeOut(animationSpec = tween(300)) + scaleOut()
            ) {

                Column(
                    modifier = Modifier.fillMaxWidth(0.2f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    RoundedSquareButtonComponent(
                        modifier = Modifier.fillMaxWidth(),
                        padding = spacing.spaceSmall,
                        text = "Habit",
                        textStyle = MaterialTheme.typography.bodyMedium,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        onClick = {
                            expanded = false
                            habitsViewModel.onEvent(HabitsEvent.ShowAddHabitDialog)
                        }
                    )

                    RoundedSquareButtonComponent(
                        modifier = Modifier.fillMaxWidth(),
                        padding = spacing.spaceSmall,
                        text = "Task",
                        textStyle = MaterialTheme.typography.bodyMedium,
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
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

