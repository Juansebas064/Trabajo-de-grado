package com.brightbox.dino.views.home.pages.tasks_and_habits_page.components

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
import com.brightbox.dino.views.common.IconButtonComponent
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun TasksControlsComponent(
    modifier: Modifier = Modifier,
    selectedTasks: List<Int>,
    selectedHabits: List<Int>,
    isSelectingTasks: Boolean,
    onTasksEvent: (TasksEvent) -> Unit,
    onHabitsEvent: (HabitsEvent) -> Unit
) {
    val spacing = LocalSpacing.current

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
            if (!isSelectingTasks) {
                AddElementsButton()
            } else {
                // Show edit button only if one task is selected
                if (selectedTasks.size + selectedHabits.size == 1) {
                    IconButtonComponent(
                        onClick = {
                            if (selectedTasks.isNotEmpty()) {
                                onTasksEvent(TasksEvent.EditTask(selectedTasks.first()))
                            } else {
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
                        onTasksEvent(TasksEvent.ShowDeleteTasksDialog)
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