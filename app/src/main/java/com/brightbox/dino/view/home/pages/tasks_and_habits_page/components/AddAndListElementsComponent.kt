package com.brightbox.dino.view.home.pages.tasks_and_habits_page.components

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.window.Popup
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.ManageElementsActivity
import com.brightbox.dino.events.HabitsEvent
import com.brightbox.dino.events.TasksEvent
import com.brightbox.dino.viewmodel.HabitsViewModel
import com.brightbox.dino.viewmodel.TasksViewModel
import com.brightbox.dino.view.common.IconButtonComponent
import com.brightbox.dino.view.common.RoundedSquareButtonComponent
import com.brightbox.dino.view.theme.LocalSpacing
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddAndListElementsComponent(
    modifier: Modifier = Modifier,
    tasksViewModel: TasksViewModel = hiltViewModel(),
    habitsViewModel: HabitsViewModel = hiltViewModel(),
    controlsFor: String,
) {
    var expanded by remember {
        mutableStateOf(false)
    }

    var buttonEnabled by remember { mutableStateOf(true) }

    val spacing = LocalSpacing.current
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(expanded) {
        if (!expanded) {
            scope.launch {
                buttonEnabled = false
                delay(300)
                buttonEnabled = true
            }
        }
    }

    Box(
        modifier = modifier
    ) {
        Row(
            horizontalArrangement = spacedBy(spacing.spaceSmall)
        ) {
            // Manage elements button
            if (controlsFor == "both") {
                IconButtonComponent(
                    onClick = {
                        val manageElementsIntent = Intent(context, ManageElementsActivity::class.java).addFlags(FLAG_ACTIVITY_NEW_TASK)
                        context.startActivity(manageElementsIntent)
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    icon = Icons.AutoMirrored.Filled.List,
                    contentDescription = "Manage tasks"
                )
            }

            // Add elements button
            IconButtonComponent(
                modifier = Modifier.zIndex(1f),
                onClick = {
                    if (buttonEnabled && controlsFor == "both") {
                        expanded = !expanded
                    } else {
                        when (controlsFor) {
                            "tasks" -> tasksViewModel.onEvent(TasksEvent.ShowAddTaskDialog)
                            "habits" -> habitsViewModel.onEvent(HabitsEvent.ShowAddHabitDialog)
                        }
                    }
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                icon = if (expanded) Icons.Default.Clear else Icons.Default.Add,
                contentDescription = "Add elements"
            )
        }

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

