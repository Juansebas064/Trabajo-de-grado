package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.hourglass.constants.daysOfWeek
import com.brightbox.hourglass.events.HabitsEvent
import com.brightbox.hourglass.model.CategoriesModel
import com.brightbox.hourglass.model.HabitsLogsModel
import com.brightbox.hourglass.model.HabitsModel
import com.brightbox.hourglass.utils.formatMillisecondsToDay
import com.brightbox.hourglass.utils.formatMillisecondsToSQLiteDate
import com.brightbox.hourglass.viewmodel.HabitsViewModel
import com.brightbox.hourglass.viewmodel.TimeViewModel
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun HabitComponent(
    modifier: Modifier = Modifier,
    habit: HabitsModel,
    selectedHabits: List<Int>,
    isSelectingElements: Boolean,
    onHabitsEvent: (HabitsEvent) -> Unit,
    category: CategoriesModel?,
    habitsViewModel: HabitsViewModel = hiltViewModel(),
    timeViewModel: TimeViewModel = hiltViewModel()
) {

    val spacing = LocalSpacing.current
    val isSelected = selectedHabits.contains(habit.id)
    val today = timeViewModel.currentTimeMillis.collectAsState().let { time ->
        mapOf(
            "day" to formatMillisecondsToDay(time.value),
            "date" to formatMillisecondsToSQLiteDate(time.value)
        )
    }
    val habitsLogs = habitsViewModel.habitsLogs.collectAsState()

    var habitLog by remember {
        mutableStateOf<HabitsLogsModel?>(null)
    }
    val isCompletedForToday = habitLog?.completed ?: false

    LaunchedEffect(habitsLogs.value) {
        habitLog = habitsLogs.value.find { it.habitId == habit.id && it.date == today["date"] }
    }

    ElevatedCard(
        colors = CardDefaults.cardColors(
            containerColor = if (!isCompletedForToday)
                MaterialTheme.colorScheme.surface
            else MaterialTheme.colorScheme.surface.copy(alpha = 0.7f),

            contentColor = if (!isCompletedForToday)
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
                border = if (isSelected)
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
                                onHabitsEvent(HabitsEvent.UnmarkHabitSelected(habit.id!!))
                            } else {
                                onHabitsEvent(HabitsEvent.MarkHabitSelected(habit.id!!))
                            }
                        }

                        false -> {
                            if (habitLog == null) {
                                habitsViewModel.upsertHabitLog(habit.id!!, today["date"]!!)
                            } else {
                                if (isCompletedForToday) {
                                    habitsViewModel.setHabitLogUncompleted(habitLog!!.id!!)
                                } else {
                                    habitsViewModel.setHabitLogCompleted(habitLog!!.id!!)
                                }
                            }
                        }
                    }
                },
                onLongClick = {
                    if (isSelected) {
                        onHabitsEvent(HabitsEvent.UnmarkHabitSelected(habit.id!!))
                    } else {
                        onHabitsEvent(HabitsEvent.MarkHabitSelected(habit.id!!))
                    }
//                    val toast = Toast.makeText(context, "Long click detected", Toast.LENGTH_SHORT)
//                    toast.show()
                }
            )
    ) {
        Box(
            modifier = modifier
                .padding(vertical = spacing.spaceSmall, horizontal = spacing.spaceMedium)
        ) {
            // Primary container
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Title
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing.spaceExtraSmall)
                ) {
                    Text(
                        text = habit.title,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Start,
                        lineHeight = MaterialTheme.typography.titleSmall.fontSize,
                        textDecoration = if (isCompletedForToday) TextDecoration.LineThrough else null,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(
                    modifier.height(spacing.spaceSmall)
                )

                // Days of week the habit will be shown

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    daysOfWeek.forEach { day ->
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(RoundedCornerShape(spacing.spaceSmall))
                                .background(if (day in habit.daysOfWeek) (if (day == today["day"]) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.secondary) else Color.Transparent)
                        ) {
                            Text(
                                text = day[0].uppercase(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (day in habit.daysOfWeek)
                                    MaterialTheme.colorScheme.onSecondary
                                else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.align(Alignment.Center),
                                textDecoration = if (isCompletedForToday && day == today["day"]) TextDecoration.LineThrough else null,
                            )
                        }
                    }
                }


                // Container for type of element and category
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
                                .background(MaterialTheme.colorScheme.inversePrimary)
                                .fillMaxHeight()
                                .padding(
                                    horizontal = spacing.spaceExtraSmall
                                )
                        )
                        Text(
                            text = "Habit",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            textDecoration = if (isCompletedForToday) TextDecoration.LineThrough else null,
                        )
                    }
                    // Category
                    if (category != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(spacing.spaceSmall))
                                .background(MaterialTheme.colorScheme.inversePrimary)
                                .padding(
                                    horizontal = spacing.spaceSmall,
                                    vertical = spacing.spaceExtraSmall
                                )
                        ) {
                            Text(
                                text = category.name,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }
        }

    }
}