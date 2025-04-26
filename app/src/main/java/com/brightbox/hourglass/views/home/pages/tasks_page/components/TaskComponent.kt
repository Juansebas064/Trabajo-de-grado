package com.brightbox.hourglass.views.home.pages.tasks_page.components

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.model.CategoriesModel
import com.brightbox.hourglass.model.TasksModel
import com.brightbox.hourglass.viewmodel.TasksViewModel
import com.brightbox.hourglass.views.theme.LocalSpacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TaskComponent(
    task: TasksModel,
    selectedTasks: List<Int>,
    isSelectingTasks: Boolean,
    onTasksEvent: (TasksEvent) -> Unit,
    category: CategoriesModel?,
    modifier: Modifier = Modifier
) {

    val spacing = LocalSpacing.current
    val context = LocalContext.current
    val isSelected = selectedTasks.contains(task.id)
    val prioritiesColors = mapOf(
        "High" to listOf(
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.onError
        ),
        "Medium" to listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSecondary
        ),
        "Low" to listOf(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.onTertiary
        ),
    )

    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = if (!task.isCompleted)
                MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),

            contentColor = if (!task.isCompleted)
                MaterialTheme.colorScheme.onSurface
            else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
        ),
        shape = RoundedCornerShape(spacing.spaceSmall),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected)
                spacing.spaceMedium
            else 0.dp
        ),
        modifier = Modifier
            .border(
                border = if (selectedTasks.contains(task.id))
                    BorderStroke(4.dp, MaterialTheme.colorScheme.secondary)
                else BorderStroke(0.dp, Color.Transparent),
                shape = RoundedCornerShape(spacing.spaceSmall)
            )
            .combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                onClick = {
                    when (isSelectingTasks) {
                        true -> {
                            if (isSelected) {
                                onTasksEvent(TasksEvent.UnmarkTaskSelected(task.id!!))
                            } else {
                                onTasksEvent(TasksEvent.MarkTaskSelected(task.id!!))
                            }
                        }

                        false -> {
                            if (task.isCompleted) {
                                onTasksEvent(TasksEvent.SetTaskUncompleted(task.id))
                            } else {
                                onTasksEvent(TasksEvent.SetTaskCompleted(task.id))
                            }
                        }
                    }
                },
                onLongClick = {
                    if (isSelected) {
                        onTasksEvent(TasksEvent.UnmarkTaskSelected(task.id!!))
                    } else {
                        onTasksEvent(TasksEvent.MarkTaskSelected(task.id!!))
                    }
//                    val toast = Toast.makeText(context, "Long click detected", Toast.LENGTH_SHORT)
//                    toast.show()
                }
            )
    ) {
        Box(
            modifier = modifier
                .padding(spacing.spaceMedium)
        ) {
            // Primary container
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Title
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing.spaceExtraSmall)
                ) {
                    Box(
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .weight(1f)
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.titleSmall,
                            textAlign = TextAlign.Start,
                            lineHeight = MaterialTheme.typography.titleSmall.fontSize,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    // Priority
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(spacing.spaceSmall))
                            .background(prioritiesColors[task.priority]!![0])
                            .padding(
                                horizontal = spacing.spaceSmall,
                                vertical = spacing.spaceExtraSmall
                            )
                    ) {
                        Text(
                            text = task.priority,
                            style = MaterialTheme.typography.bodySmall,
                            color = prioritiesColors[task.priority]!![1],
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // Description
                if (task.description.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                    ) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.fontSize,
                            textAlign = TextAlign.Start,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                        )
                    }
                }

                // Date
                if (task.dateDue?.isNotEmpty() == true) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = spacing.spaceExtraSmall)
                    ) {
                        Text(
                            text = "Due: ",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                        )

                        // Calendar icon and date
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(spacing.default),
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = "Date",
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier
                                    .scale(0.7f)
                            )

                            Text(
                                text = task.dateDue,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                            )
                        }
                    }
                }

                // Container for type of task and category
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(
                            top = spacing.spaceSmall,
//                            horizontal = spacing.spaceMedium
                        )
                        .fillMaxWidth()
                        .height(IntrinsicSize.Max)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(spacing.spaceExtraSmall),
                        modifier = Modifier
                            .padding(vertical = spacing.spaceExtraSmall)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(3.dp))
                                .background(MaterialTheme.colorScheme.primary)
                                .fillMaxHeight()
                                .padding(
                                    horizontal = spacing.spaceExtraSmall
                                )
                        )
                        Text(
                            text = "Task",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                    // Category
                    if (category != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(spacing.spaceSmall))
                                .background(MaterialTheme.colorScheme.primary)
                                .padding(
                                    horizontal = spacing.spaceSmall,
                                    vertical = spacing.spaceExtraSmall
                                )
                        ) {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }
        }

    }
}