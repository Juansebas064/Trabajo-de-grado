package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.model.CategoriesModel
import com.brightbox.hourglass.model.TasksModel
import com.brightbox.hourglass.utils.getDifferenceInDays
import com.brightbox.hourglass.viewmodel.TimeViewModel
import com.brightbox.hourglass.views.theme.LocalSpacing
import kotlin.math.abs

@Composable
fun TaskComponent(
    modifier: Modifier = Modifier,
    task: TasksModel,
    selectedTasks: List<Int>,
    isSelectingElements: Boolean,
    onTasksEvent: (TasksEvent) -> Unit,
    category: CategoriesModel?,
    timeViewModel: TimeViewModel = hiltViewModel()
) {

    val spacing = LocalSpacing.current
    val isSelected = selectedTasks.contains(task.id)
    val daysRemaining: Int = timeViewModel.currentTimeMillis.collectAsState().let { milliseconds ->
        if (!task.dateDue.isNullOrEmpty()) {
            getDifferenceInDays(milliseconds.value, task.dateDue)
        } else {
            0
        }
    }

    val prioritiesColors = mapOf(
        "High" to listOf(
            MaterialTheme.colorScheme.error,
            MaterialTheme.colorScheme.onError
        ),
        "Medium" to listOf(
            MaterialTheme.colorScheme.surfaceVariant,
            MaterialTheme.colorScheme.onSurfaceVariant
        ),
        "Low" to listOf(
            MaterialTheme.colorScheme.tertiary,
            MaterialTheme.colorScheme.onTertiary
        ),
    )

    var canExpand by remember {
        mutableStateOf(false)
    }

    var expanded by remember {
        mutableStateOf(false)
    }

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
                indication = ripple(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ),
                onClick = {
                    when (isSelectingElements) {
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
                }
            )
    ) {
        Box(
            modifier = modifier
                .padding(
                    top = spacing.spaceSmall + spacing.spaceExtraSmall,
                    bottom = if (expanded || task.wasDelayed) spacing.spaceExtraSmall else spacing.spaceSmall,
                    start = spacing.spaceMedium,
                    end = spacing.spaceMedium
                )
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
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .width(IntrinsicSize.Min)
                            .weight(1f)
                    ) {
                        Text(
                            text = task.title,
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.ExtraBold,
                            textAlign = TextAlign.Start,
                            lineHeight = MaterialTheme.typography.bodyMedium.fontSize,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                            maxLines = 2,
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
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }

                // Description
                if (task.description.isNotEmpty()) {

                    Box(
                        modifier = Modifier
                            .padding(top = spacing.spaceSmall)
                            .animateContentSize()
                    ) {
                        Text(
                            text = task.description,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = MaterialTheme.typography.bodyMedium.fontSize,
                            textAlign = TextAlign.Start,
                            overflow = TextOverflow.Ellipsis,
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                            maxLines = if (expanded) Int.MAX_VALUE else 2,
                            onTextLayout = { layoutResult ->
                                // Si no está expandido y hay más de 2 líneas, activamos el botón
                                if (!expanded && layoutResult.hasVisualOverflow) {
                                    canExpand = true
                                } else if (!expanded && !layoutResult.hasVisualOverflow) {
                                    canExpand = false
                                }
                            }
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
                            .padding(top = spacing.spaceSmall)
                    ) {
                        val textToShow = when (daysRemaining) {
                            -1 -> "1 day late"
                            0 -> "Today"
                            1 -> "Tomorrow"
                            else -> if (daysRemaining > 1) {
                                "In $daysRemaining days"
                            } else {
                                "${abs(daysRemaining)} days late"
                            }
                        }

                        val colorToShow = if (task.wasDelayed && !task.isCompleted) {
                            MaterialTheme.colorScheme.error
                        } else if (textToShow == "Today") {
                            MaterialTheme.colorScheme.inversePrimary
                        } else {
                            MaterialTheme.colorScheme.onSurface
                        }

                        Text(
                            text = textToShow,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = colorToShow,
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
                                tint = colorToShow,
                                modifier = Modifier
                                    .scale(0.7f)
                            )

                            Text(
                                text = task.dateDue,
                                style = MaterialTheme.typography.bodyMedium,
                                color = colorToShow,
                                textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
                                fontWeight = if (textToShow == "Today") FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // Container for type of task and category
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
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
                            textDecoration = if (task.isCompleted) TextDecoration.LineThrough else null,
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

        Column(
            Modifier
                .fillMaxWidth()
                .padding(0.dp)
        ) {
            if (task.description.isNotEmpty() && canExpand) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .padding(bottom = spacing.spaceSmall)
                        .clickable {
                            expanded = !expanded
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(Modifier.padding(vertical = spacing.spaceExtraSmall)) {
                        Icon(
                            imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = "Expand",
                            tint = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                }
            }

            if (task.wasDelayed) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant.copy(
                                alpha = if (task.isCompleted) 0.8f else 1f
                            )
                        )
                        .padding(vertical = spacing.spaceExtraSmall),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Delayed",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}