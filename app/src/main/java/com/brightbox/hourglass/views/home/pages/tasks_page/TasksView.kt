package com.brightbox.hourglass.views.home.pages.tasks_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.viewmodel.CategoriesViewModel
import com.brightbox.hourglass.viewmodel.TasksViewModel
import com.brightbox.hourglass.views.home.pages.tasks_page.components.TaskComponent
import com.brightbox.hourglass.views.home.pages.tasks_page.components.TasksControlsComponent
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun TasksView(
    tasksViewModel: TasksViewModel,
    categoriesViewModel: CategoriesViewModel,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val tasksState = tasksViewModel.state.collectAsState()
    val categoriesState = categoriesViewModel.state.collectAsState()

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .width(LocalConfiguration.current.screenWidthDp.dp * 0.6f)
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
        }
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            items(tasksState.value.tasks) { task ->
                TaskComponent(
                    task,
                    onTasksEvent = tasksViewModel::onEvent
                )
            }
        }
        TasksControlsComponent(
            onEvent = tasksViewModel::onEvent
        )
    }
}
