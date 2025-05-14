package com.brightbox.hourglass.views.home.pages.tasks_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.viewmodel.CategoriesViewModel
import com.brightbox.hourglass.viewmodel.TasksViewModel
import com.brightbox.hourglass.views.home.pages.tasks_page.components.TaskComponent
import com.brightbox.hourglass.views.home.pages.tasks_page.components.TasksControlsComponent
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun TasksView(
    modifier: Modifier = Modifier,
    tasksViewModel: TasksViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val tasksState = tasksViewModel.state.collectAsState()
    val categoriesState = categoriesViewModel.state.collectAsState()
    val selectedTasks = tasksViewModel.selectedTasks.collectAsState()
    val isSelectingTasks = remember { mutableStateOf(selectedTasks.value.isNotEmpty()) }

    LaunchedEffect(key1 = selectedTasks.value) {
        isSelectingTasks.value = selectedTasks.value.isNotEmpty()
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(LocalConfiguration.current.screenWidthDp.dp * 0.65f)
    ) {

        if (tasksState.value.tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = spacing.spaceExtraLarge)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Tap + button to create a task",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = modifier
        ) {
            if (tasksState.value.isAddingTask) {
                AddTaskDialog(
                    onTasksEvent = tasksViewModel::onEvent,
                    onCategoriesEvent = categoriesViewModel::onEvent,
                    tasksState = tasksState.value,
                    categoriesState = categoriesState.value
                )
            }

            if (categoriesState.value.isAddingCategory) {
                AddCategoryDialog(
                    onTasksEvent = tasksViewModel::onEvent,
                    onCategoriesEvent = categoriesViewModel::onEvent,
                    categoriesState = categoriesState.value
                )
            }

            if (tasksState.value.isDeletingTasks) {
                AlertDialog(
                    onDismissRequest = {
                        tasksViewModel.onEvent(TasksEvent.HideDeleteTasksDialog)
                    },
                    title = {
                        Text(text = "Delete tasks")
                    },
                    text = {
                        Text(text = "Are you sure you want to delete these tasks?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                tasksViewModel.onEvent(TasksEvent.DeleteTasks)
                                tasksViewModel.onEvent(TasksEvent.HideDeleteTasksDialog)
                            },
                        ) {
                            Text(text = "Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                tasksViewModel.onEvent(TasksEvent.HideDeleteTasksDialog)
                            },
                        ) {
                            Text(text = "Cancel")
                        }
                    },
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                items(tasksState.value.tasks) { task ->
                    TaskComponent(
                        task = task,
                        selectedTasks = selectedTasks.value,
                        isSelectingTasks = isSelectingTasks.value,
                        category = categoriesState.value.categories.find { it.id == task.categoryId },
                        onTasksEvent = tasksViewModel::onEvent
                    )
//                    if (!task.isCompleted && task.dateDue != "")
//                    if (task.dateDue == "" || task.dateDue!! >= tasksViewModel.formatMilisToDate(System.currentTimeMillis()))
//                    {
//                        TaskComponent(
//                            task = task,
//                            selectedTasks = selectedTasks.value,
//                            isSelectingTasks = isSelectingTasks.value,
//                            category = categoriesState.value.categories.find { it.id == task.categoryId },
//                            onTasksEvent = tasksViewModel::onEvent
//                        )
//                    }
                }
            }

            TasksControlsComponent(
                selectedTasks = selectedTasks.value,
                isSelectingTasks = isSelectingTasks.value,
                onEvent = tasksViewModel::onEvent
            )
        }
    }
}
