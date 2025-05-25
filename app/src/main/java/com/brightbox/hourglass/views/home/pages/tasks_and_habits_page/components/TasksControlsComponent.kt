package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.brightbox.hourglass.views.common.IconButtonComponent
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