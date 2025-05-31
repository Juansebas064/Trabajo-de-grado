package com.brightbox.dino.views.home.pages.tasks_and_habits_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.R
import com.brightbox.dino.utils.formatMillisecondsToDay
import com.brightbox.dino.utils.formatMillisecondsToSQLiteDate
import com.brightbox.dino.viewmodel.CategoriesViewModel
import com.brightbox.dino.viewmodel.HabitsViewModel
import com.brightbox.dino.viewmodel.TasksViewModel
import com.brightbox.dino.viewmodel.TimeViewModel
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.DeleteElementsDialog
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.HabitComponent
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.TaskComponent
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.TasksAndHabitsControlsComponent
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun TasksAndHabitsListView(
    modifier: Modifier = Modifier,
    tasksViewModel: TasksViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
    habitsViewModel: HabitsViewModel = hiltViewModel(),
    timeViewModel: TimeViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current
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
            .fillMaxWidth(0.65f)
    ) {

        if (tasksState.value.tasks.isEmpty() && habitsState.value.habits.isEmpty()) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(horizontal = spacing.spaceExtraLarge)
            ) {
                Text(
                    modifier = Modifier,
                    text = context.getString(R.string.tap_plus_to_create_an_element),
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
            // Show task dialog
            if (tasksState.value.isAddingTask) {
                AddTaskDialog(
                    onTasksEvent = tasksViewModel::onEvent,
                    tasksState = tasksState.value,
                    categoriesState = categoriesState.value
                )
            }

            // Show habit dialog
            if (habitsState.value.isAddingHabit) {
                AddHabitDialog(
                    habitsState = habitsState.value,
                    categoriesState = categoriesState.value,
                    onHabitsEvent = habitsViewModel::onEvent,
                    onCategoriesEvent = categoriesViewModel::onEvent
                )
            }

            // Show delete dialog
            if (tasksState.value.isDeletingTasks || habitsState.value.isDeletingHabits) {
                DeleteElementsDialog()
            }


            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                ) {
                    if (habitsState.value.habits.isNotEmpty() && tasksState.value.tasks.isNotEmpty()) {
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = context.getString(R.string.habits),
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }

                    items(
                        items = habitsState.value.habits,
                        key = { it.id.toString() + it.startDate + it.title }
                    ) { habit ->
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

                    if (habitsState.value.habits.isNotEmpty() && tasksState.value.tasks.isNotEmpty()) {
                        item {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = context.getString(R.string.tasks),
                                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }

                    items(
                        items = tasksState.value.tasks,
                        key = { it.id.toString() + it.title + it.dateCreated }
                    ) { task ->
                        if (
                            (task.isCompleted && task.dateCompleted == today["date"] && task.visible)
                            || (!task.isCompleted && task.visible)
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
            }

            TasksAndHabitsControlsComponent(
                selectedTasks = selectedTasks.value,
                selectedHabits = selectedHabits.value,
                isSelectingElements = isSelectingElements.value,
                onTasksEvent = tasksViewModel::onEvent,
                onHabitsEvent = habitsViewModel::onEvent
            )
        }
    }
}
