package com.brightbox.hourglass.views.home.pages.tasks_page

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.states.TasksState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    state: TasksState,
    onEvent: (TasksEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    BasicAlertDialog(
        onDismissRequest = {
            onEvent(TasksEvent.HideDialog)
        },
        modifier = modifier
    ) {
        Column {
            TextField(
                value = state.taskTitle,
                onValueChange = {
                    onEvent(TasksEvent.SetTaskTitle(it))
                },
                placeholder = {
                    Text(text = "Task title")
                }
            )

            TextField(
                value = state.taskDescription,
                onValueChange = {
                    onEvent(TasksEvent.SetTaskDescription(it))
                },
                placeholder = {
                    Text(text = "Description")
                }
            )

            TextField(
                value = state.taskCategory.toString(),
                onValueChange = {
                    onEvent(TasksEvent.SetTaskCategory(it.toInt()))
                },
                placeholder = {
                    Text(text = "Category")
                }
            )

            TextField(
                value = state.taskDueDate,
                onValueChange = {
                    onEvent(TasksEvent.SetTaskDueDate(it))
                },
                placeholder = {
                    Text(text = "Due date")
                }
            )

            TextField(
                value = state.taskPriority.toString(),
                onValueChange = {
                    onEvent(TasksEvent.SetTaskPriority(it.toInt()))
                },
                placeholder = {
                    Text(text = "Priority")
                }
            )

            Button(
                onClick = {
                    onEvent(TasksEvent.SaveTask)
                }
            ) {
                Text(text = "Save")
            }
        }
    }
}