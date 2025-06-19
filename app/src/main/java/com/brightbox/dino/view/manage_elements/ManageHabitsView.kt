package com.brightbox.dino.view.manage_elements

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
import com.brightbox.dino.model.constants.months
import com.brightbox.dino.model.HabitsModel
import com.brightbox.dino.utils.formatSQLiteDateToMonth
import com.brightbox.dino.viewmodel.CategoriesViewModel
import com.brightbox.dino.viewmodel.HabitsViewModel
import com.brightbox.dino.view.home.pages.tasks_and_habits_page.AddHabitDialog
import com.brightbox.dino.view.home.pages.tasks_and_habits_page.components.DeleteElementsDialog
import com.brightbox.dino.view.home.pages.tasks_and_habits_page.components.HabitComponent
import com.brightbox.dino.view.home.pages.tasks_and_habits_page.components.TasksAndHabitsControlsComponent
import com.brightbox.dino.view.theme.LocalSpacing

@Composable
fun ManageHabitsView(
    modifier: Modifier = Modifier,
    habitsViewModel: HabitsViewModel = hiltViewModel(),
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val state = habitsViewModel.state.collectAsState()
    val habits = state.value.habits
    val selectedHabits = habitsViewModel.selectedHabits.collectAsState()
    val categoriesState = categoriesViewModel.state.collectAsState()
    val isSelectingElements = remember {
        mutableStateOf(selectedHabits.value.isNotEmpty())
    }

    val habitsGroupedByMonth =
        habits.sortedByDescending { it.startDate }
            .groupBy { formatSQLiteDateToMonth(it.startDate) }
            .flatMap { (month, habitsInMonth) ->
                listOf(month) + habitsInMonth
            }

    LaunchedEffect(key1 = selectedHabits.value) {
        isSelectingElements.value =
            selectedHabits.value.isNotEmpty()
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
            if (state.value.isAddingHabit) {
                AddHabitDialog(
                    onHabitsEvent = habitsViewModel::onEvent,
                    habitsState = state.value,
                    categoriesState = categoriesState.value,
                    onCategoriesEvent = categoriesViewModel::onEvent,
                )
            }

            // Show delete dialog
            if (state.value.isDeletingHabits) {
                DeleteElementsDialog()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.spaceMedium)
            ) {
                // Title of view
                Text(
                    text = context.getString(R.string.habits),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

            }

            if (habitsGroupedByMonth.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {

                    items(
                        items = habitsGroupedByMonth,
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

                            // Habits
                        } else {
                            HabitComponent(
                                habit = element as HabitsModel,
                                selectedHabits = selectedHabits.value,
                                isSelectingElements = isSelectingElements.value,
                                onHabitsEvent = habitsViewModel::onEvent,
                                category = categoriesState.value.categories.find { it.id == element.categoryId },
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
                        text = context.getString(R.string.tap_plus_to_create_an_habit),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            TasksAndHabitsControlsComponent(
                selectedTasks = null,
                selectedHabits = selectedHabits.value,
                isSelectingElements = isSelectingElements.value,
                onTasksEvent = {},
                onHabitsEvent = habitsViewModel::onEvent
            )
        }
    }
}