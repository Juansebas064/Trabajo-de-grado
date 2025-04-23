package com.brightbox.hourglass.views.home.pages.tasks_page.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.model.CategoriesModel
import com.brightbox.hourglass.model.TasksModel
import com.brightbox.hourglass.viewmodel.TasksViewModel
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun TaskComponent(
    task: TasksModel,
    onTasksEvent: (TasksEvent) -> Unit,
//    category: CategoriesModel?,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current
    ElevatedCard(
        onClick = {
            if (task.isCompleted) {
                onTasksEvent(TasksEvent.SetTaskUncompleted(task.id))
            }
            else {
                onTasksEvent(TasksEvent.SetTaskCompleted(task.id))
            }
        },
        colors = CardDefaults.cardColors(
            containerColor = if (!task.isCompleted)
                MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),

            contentColor = if (!task.isCompleted)
                MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        ),
        shape = RoundedCornerShape(spacing.spaceSmall),
//        elevation = CardDefaults.cardElevation(
//            defaultElevation = spacing.spaceMedium
//        ),

        ) {
        Box(
            modifier = modifier
                .padding(spacing.spaceMedium)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .padding(bottom = spacing.spaceExtraSmall)
                ) {
                    Text(
                        text = task.title,
                        style = MaterialTheme.typography.titleSmall,
                        textAlign = TextAlign.Center,
                        lineHeight = MaterialTheme.typography.titleSmall.fontSize
                    )
                }

                if (task.description.isNotEmpty()) {
                    Text(
                        text = task.description,
                        style = MaterialTheme.typography.bodyMedium,
                        lineHeight = MaterialTheme.typography.bodyMedium.fontSize,
                        textAlign = TextAlign.Center
                    )
                }

                if(task.dateDue?.isNotEmpty() == true) {
                    Row(
                        modifier = Modifier
                            .padding(top = spacing.spaceExtraSmall)
                    ) {
                        Text(
                            text = "Due: ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = task.dateDue,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = spacing.spaceSmall)
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.primary)
                            .padding(spacing.spaceSmall)
                    ) {
                        Text(
                            text = "Task",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(15.dp))
                            .background(MaterialTheme.colorScheme.error)
                            .padding(spacing.spaceSmall)
                    ) {
                        Text(
                            text = task.priority,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onError
                        )
                    }


                }
            }
        }

    }
}