package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.events.HabitsEvent
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun TasksControlsComponent(
    selectedTasks: List<Int>,
    selectedHabits: List<Int>,
    isSelectingTasks: Boolean,
    onTasksEvent: (TasksEvent) -> Unit,
    onHabitsEvent: (HabitsEvent) -> Unit
) {
    val spacing = LocalSpacing.current

    Box(
//        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = spacing.spaceMedium)
    ) {
//        Text(
//            text = "Today",
//            color = MaterialTheme.colorScheme.onBackground,
//            style = MaterialTheme.typography.bodyLarge.plus(
//                TextStyle(textDecoration = TextDecoration.Underline)
//            ),
//            modifier = Modifier
//
//        )
//
        // Add task
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
                    IconButton(
                        onClick = {
                            if (selectedTasks.isNotEmpty()) {
                                onTasksEvent(TasksEvent.EditTask(selectedTasks.first()))
                            } else {
                                onHabitsEvent(HabitsEvent.EditHabit(selectedHabits.first()))
                            }
                        },
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(MaterialTheme.colorScheme.secondary)
//                            .align(Alignment.Center)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Add task",
                            tint = MaterialTheme.colorScheme.onSecondary
                        )
                    }
                }

                // Delete tasks
                IconButton(
                    onClick = {
                        onTasksEvent(TasksEvent.ShowDeleteTasksDialog)
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.error)
//                        .align(Alignment.Center)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Add task",
                        tint = MaterialTheme.colorScheme.onError
                    )
                }
            }
        }
    }
}