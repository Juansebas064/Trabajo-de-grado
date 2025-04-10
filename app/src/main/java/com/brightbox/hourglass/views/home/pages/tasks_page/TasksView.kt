package com.brightbox.hourglass.views.home.pages.tasks_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.brightbox.hourglass.viewmodel.TasksViewModel
import com.brightbox.hourglass.views.home.pages.tasks_page.components.TasksControlsComponent

@Composable
fun TasksView(
    tasksViewModel: TasksViewModel,
    modifier: Modifier = Modifier
) {

    val state = tasksViewModel.state.collectAsState()

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = modifier
            .fillMaxSize()
    ) {
        if(state.value.isAddingTask) {
            AddTaskDialog(
                onEvent = tasksViewModel::onEvent,
                state = state.value
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
        ) {
            items(state.value.tasks) { task ->
                Text(
                    text = task.title
                )
            }
        }
        TasksControlsComponent(
            onEvent = tasksViewModel::onEvent
        )
    }
}