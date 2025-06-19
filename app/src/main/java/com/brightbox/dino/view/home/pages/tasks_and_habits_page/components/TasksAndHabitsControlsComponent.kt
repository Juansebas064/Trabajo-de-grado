package com.brightbox.dino.view.home.pages.tasks_and_habits_page.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.brightbox.dino.events.HabitsEvent
import com.brightbox.dino.events.TasksEvent
import com.brightbox.dino.view.common.IconButtonComponent
import com.brightbox.dino.view.theme.LocalSpacing

@Composable
fun TasksAndHabitsControlsComponent(
    modifier: Modifier = Modifier,
    selectedTasks: List<Int>?,
    selectedHabits: List<Int>?,
    isSelectingElements: Boolean,
    onTasksEvent: (TasksEvent) -> Unit,
    onHabitsEvent: (HabitsEvent) -> Unit
) {
    val spacing = LocalSpacing.current
    val controlsFor = if (selectedTasks != null && selectedHabits != null) {
        "both"
    } else if(selectedTasks != null) {
        "tasks"
    } else {
        "habits"
    }

    Box(
//        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = spacing.spaceMedium)
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
            modifier = Modifier
                .align(Alignment.Center)
        ) {
            // Add task
            if (!isSelectingElements) {
                // Add elements
                AddAndListElementsComponent(
                    controlsFor = controlsFor,
                )
            } else {
                // Show edit button only if one task is selected
                if ((selectedTasks?.size ?: 0) + (selectedHabits?.size ?: 0) == 1) {
                    IconButtonComponent(
                        onClick = {
                            if (selectedTasks != null && selectedTasks.isNotEmpty()) {
                                onTasksEvent(TasksEvent.EditTask(selectedTasks.first()))
                            } else if (selectedHabits != null && selectedHabits.isNotEmpty()) {
                                onHabitsEvent(HabitsEvent.EditHabit(selectedHabits.first()))
                            }
                        },
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary,
                        icon = Icons.Default.Edit,
                        contentDescription = "Edit task"
                    )
                }

                // Delete tasks
                IconButtonComponent(
                    onClick = {
                        if (selectedTasks != null && selectedTasks.isNotEmpty()) {
                            onTasksEvent(TasksEvent.ShowDeleteTasksDialog)
                        } else if (selectedHabits != null && selectedHabits.isNotEmpty()) {
                            onHabitsEvent(HabitsEvent.ShowDeleteHabitDialog)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    icon = Icons.Default.Delete,
                    contentDescription = "Delete tasks"
                )
            }
        }
    }
}