package com.brightbox.dino.views.manage_elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.R
import com.brightbox.dino.constants.months
import com.brightbox.dino.model.TasksModel
import com.brightbox.dino.utils.formatSQLiteDateToMonth
import com.brightbox.dino.viewmodel.CategoriesViewModel
import com.brightbox.dino.viewmodel.TasksViewModel
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.AddTaskDialog
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.DeleteElementsDialog
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.TaskComponent
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.TasksAndHabitsControlsComponent
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun ManageTasksView(
    modifier: Modifier = Modifier,
    tasksViewModel: TasksViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val state = tasksViewModel.state.collectAsState()
    val tasks = state.value.tasks
    val selectedTasks = tasksViewModel.selectedTasks.collectAsState()
    val categoriesState = categoriesViewModel.state.collectAsState()
    val isSelectingElements = remember {
        mutableStateOf(selectedTasks.value.isNotEmpty())
    }

    val tasksGroupedByMonth =
        tasks.sortedByDescending { it.dateCreated }
            .groupBy { formatSQLiteDateToMonth(it.dateCreated!!) }
            .flatMap { (month, tasksInMonth) ->
                listOf(month) + tasksInMonth
            }

    LaunchedEffect(key1 = selectedTasks.value) {
        isSelectingElements.value =
            selectedTasks.value.isNotEmpty()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            // Show task dialog
            if (state.value.isAddingTask) {
                AddTaskDialog(
                    onTasksEvent = tasksViewModel::onEvent,
                    tasksState = state.value,
                    categoriesState = categoriesState.value
                )
            }

            // Show delete dialog
            if (state.value.isDeletingTasks) {
                DeleteElementsDialog()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMedium)
            ) {
                // Title of view
                Text(
                    text = context.getString(R.string.tasks),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            if (tasksGroupedByMonth.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {

                    items(
                        items = tasksGroupedByMonth,
                    ) { element ->
                        // Name of the month
                        if (element::class == String::class) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = spacing.spaceMedium)
                            ) {
                                Text(
                                    text = context.getString(months[element as String]!!)
                                        .replaceFirstChar { it.uppercase() },
                                    color = MaterialTheme.colorScheme.onBackground,
                                    style = MaterialTheme.typography.bodyLarge,
                                    textAlign = TextAlign.Start,
                                )
                            }

                            // Tasks
                        } else {
                            TaskComponent(
                                task = element as TasksModel,
                                selectedTasks = selectedTasks.value,
                                isSelectingElements = isSelectingElements.value,
                                category = categoriesState.value.categories.find { it.id == element.categoryId },
                                onTasksEvent = tasksViewModel::onEvent
                            )
                        }
                    }
                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = spacing.spaceExtraLarge)
                ) {
                    Text(
                        modifier = Modifier,
                        text = context.getString(R.string.tap_plus_to_create_a_task),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            TasksAndHabitsControlsComponent(
                selectedTasks = selectedTasks.value,
                selectedHabits = null,
                isSelectingElements = isSelectingElements.value,
                onTasksEvent = tasksViewModel::onEvent,
                onHabitsEvent = {}
            )
        }
    }
}