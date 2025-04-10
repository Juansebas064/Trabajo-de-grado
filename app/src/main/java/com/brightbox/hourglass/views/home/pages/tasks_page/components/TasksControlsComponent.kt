package com.brightbox.hourglass.views.home.pages.tasks_page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun TasksControlsComponent(
    onEvent: (TasksEvent) -> Unit,
) {
    val spacing = LocalSpacing.current

    Box(
//        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth()
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
        IconButton(
            onClick = {
                onEvent(TasksEvent.ShowDialog)
            },
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface)
                .align(Alignment.Center)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}