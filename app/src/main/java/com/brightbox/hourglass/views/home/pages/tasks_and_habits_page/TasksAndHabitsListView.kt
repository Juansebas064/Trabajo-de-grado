package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import com.brightbox.hourglass.events.HabitsEvent
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.utils.formatMillisecondsToDay
import com.brightbox.hourglass.utils.formatMillisecondsToSQLiteDate
import com.brightbox.hourglass.viewmodel.CategoriesViewModel
import com.brightbox.hourglass.viewmodel.HabitsViewModel
import com.brightbox.hourglass.viewmodel.TasksViewModel
import com.brightbox.hourglass.viewmodel.TimeViewModel
import com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components.HabitComponent
import com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components.TaskComponent
import com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components.TasksControlsComponent
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun TasksAndHabitsListView(
    modifier: Modifier = Modifier,
    tasksViewModel: TasksViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
    habitsViewModel: HabitsViewModel = hiltViewModel(),
    timeViewModel: TimeViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val tasksState = tasksViewModel.state.collectAsState()
    val selectedTasks = tasksViewModel.selectedTasks.collectAsState()
    val habitsState = habitsViewModel.state.collectAsState()
    val selectedHabits = habitsViewModel.selectedHabits.collectAsState()
    val categoriesState = categoriesViewModel.state.collectAsState()
    val isSelectingElements = remember {
        mutableStateOf(
            selectedTasks.value.isNotEmpty() || selectedHabits.value.isNotEmpty()
        )
    }
    val today = timeViewModel.currentTimeMillis.collectAsState().let { time ->
        mapOf(
            "day" to formatMillisecondsToDay(time.value),
            "date" to formatMillisecondsToSQLiteDate(time.value)
        )
    }

    LaunchedEffect(key1 = selectedTasks.value, key2 = selectedHabits.value) {
        isSelectingElements.value =
            selectedTasks.value.isNotEmpty() || selectedHabits.value.isNotEmpty()
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(LocalConfiguration.current.screenWidthDp.dp * 0.65f)
    ) {

        if (tasksState.value.tasks.isEmpty() && habitsState.value.todayHabits.isEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = spacing.spaceExtraLarge)
            ) {
                Text(
                    modifier = Modifier,
                    text = "Tap + button to create a task or habit",
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
                    tasksState = tasksState.value,
                    categoriesState = categoriesState.value
                )
            }

            if (habitsState.value.isAddingHabit) {
                AddHabitDialog(
                    habitsState = habitsState.value,
                    categoriesState = categoriesState.value,
                    onHabitsEvent = habitsViewModel::onEvent,
                    onCategoriesEvent = categoriesViewModel::onEvent
                )
            }

            if (tasksState.value.isDeletingTasks || habitsState.value.isDeletingHabits) {
                AlertDialog(
                    containerColor = MaterialTheme.colorScheme.surface,
                    onDismissRequest = {
                        tasksViewModel.onEvent(TasksEvent.HideDeleteTasksDialog)
                    },
                    title = {
                        Text(text = "Delete elements")
                    },
                    text = {
                        Text(text = "Are you sure you want to delete these elements?")
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                tasksViewModel.onEvent(TasksEvent.DeleteTasks)
                                habitsViewModel.onEvent(HabitsEvent.DeleteHabits)
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
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
            ) {
                if (habitsState.value.todayHabits.isNotEmpty() && tasksState.value.tasks.isNotEmpty()) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Habits",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
//                            Box(
//                                Modifier
//                                    .fillMaxWidth()
//                                    .background(MaterialTheme.colorScheme.onBackground)
//                                    .height(2.dp)
//                            )
                        }
                    }
                }

                items(habitsState.value.todayHabits) { habit ->
                    if (
                        !habit.deleted &&
                        today["day"].toString() in habit.daysOfWeek
                        && (habit.endDate == null || habit.endDate >= today["date"].toString())
                    ) {
                        HabitComponent(
                            habit = habit,
                            selectedHabits = selectedHabits.value,
                            isSelectingElements = isSelectingElements.value,
                            onHabitsEvent = habitsViewModel::onEvent,
                            category = categoriesState.value.categories.find { it.id == habit.categoryId },
                        )
                    }
                }

                if (habitsState.value.todayHabits.isNotEmpty() && tasksState.value.tasks.isNotEmpty()) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                        ) {
                            Text(
                                text = "Tasks",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onBackground
                            )
//                            Box(
//                                Modifier
//                                    .fillMaxWidth()
//                                    .background(MaterialTheme.colorScheme.onBackground)
//                                    .height(2.dp)
//                            )
                        }
                    }
                }

                items(tasksState.value.tasks) { task ->
                    if (
                        (task.isCompleted && task.dateCompleted == today["date"])
                        || (!task.isCompleted)
                    ) {
                        TaskComponent(
                            task = task,
                            selectedTasks = selectedTasks.value,
                            isSelectingElements = isSelectingElements.value,
                            category = categoriesState.value.categories.find { it.id == task.categoryId },
                            onTasksEvent = tasksViewModel::onEvent
                        )
                    }
                }
            }

            TasksControlsComponent(
                selectedTasks = selectedTasks.value,
                selectedHabits = selectedHabits.value,
                isSelectingTasks = isSelectingElements.value,
                onTasksEvent = tasksViewModel::onEvent,
                onHabitsEvent = habitsViewModel::onEvent
            )
        }
    }
}
