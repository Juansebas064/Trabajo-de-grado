package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.constants.PrioritiesEnum
import com.brightbox.hourglass.events.CategoriesEvent
import com.brightbox.hourglass.events.HabitsEvent
import com.brightbox.hourglass.events.TasksEvent
import com.brightbox.hourglass.states.CategoriesState
import com.brightbox.hourglass.states.HabitsState
import com.brightbox.hourglass.views.common.PilledTextButtonComponent
import com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components.CategorySelectorComponent
import com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components.DatePickerComponent
import com.brightbox.hourglass.views.home.pages.tasks_and_habits_page.components.DaysSelectorComponent
import com.brightbox.hourglass.views.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddHabitDialog(
    habitsState: HabitsState,
    categoriesState: CategoriesState,
    onHabitsEvent: (HabitsEvent) -> Unit,
    onCategoriesEvent: (CategoriesEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val halfScreenDp = (LocalConfiguration.current.screenHeightDp / 5).dp
    val isTitleValid = remember {
        mutableStateOf(true)
    }
    Log.d("AddHabitDialog", "habitsState start date: ${habitsState.startDate}")

    BasicAlertDialog(
        onDismissRequest = {
            isTitleValid.value = true
            onHabitsEvent(HabitsEvent.HideAddHabitDialog)
            onCategoriesEvent(CategoriesEvent.HideDialog)
        },
        modifier = modifier
            .padding(top = halfScreenDp)
    ) {
        val focusManager = LocalFocusManager.current

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
            modifier = Modifier
                .clip(RoundedCornerShape(spacing.spaceLarge))
                .background(MaterialTheme.colorScheme.surface)
                .padding(spacing.spaceMedium)
//                .animateContentSize()
        ) {
            Box(
                modifier = Modifier
                    .padding(top = spacing.spaceMedium)
            ) {
                Text(
                    text = "Add an habit",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Title
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = habitsState.habitTitle,
                    onValueChange = {
                        isTitleValid.value = true
                        onHabitsEvent(HabitsEvent.SetHabitTitle(it))
                    },
                    isError = !isTitleValid.value,
                    label = {
                        Text(
                            text = "Title",
                        )
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    ),
                )

                if (!isTitleValid.value) {
                    Text(
                        text = "Title is required",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            // Date picker
            DatePickerComponent(
                modifier = Modifier.fillMaxWidth(),
                label = "Start date",
                date = habitsState.startDate,
                setDate = {
                    onHabitsEvent(HabitsEvent.SetStartDate(it))
                },
                enabled = true
            )

            DatePickerComponent(
                modifier = Modifier.fillMaxWidth(),
                label = "End date",
                date = habitsState.endDate,
                setDate = {
                    onHabitsEvent(HabitsEvent.SetEndDate(it))
                },
                enabled = true
            )

            // Categories section
            CategorySelectorComponent(
                categoryInitialValue = categoriesState.categories.find {
                    it.id == habitsState.habitCategory
                }?.name ?: "",
                onCategoryChange = {
                    onHabitsEvent(HabitsEvent.SetHabitCategory(it))
                }
            )

            // Select which days to do the habit

            Spacer(modifier = Modifier.height(spacing.spaceMedium))
            Text(
                text = "Select which days to do the habit",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            DaysSelectorComponent(
                habitsDaysOfWeek = habitsState.habitDaysOfWeek,
                setHabitsDaysOfWeek = {
                    onHabitsEvent(HabitsEvent.SetHabitDaysOfWeek(it))
                }
            )
            Spacer(modifier = Modifier.height(spacing.spaceMedium))

            // Cancel
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
                modifier = Modifier
                    .padding(vertical = spacing.spaceExtraSmall)
                    .fillMaxWidth()
            ) {
                PilledTextButtonComponent(
                    text = "Cancel",
                    textColor = MaterialTheme.colorScheme.onError,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    backgroundColor = MaterialTheme.colorScheme.error,
                    onClick = {
                        isTitleValid.value = true
                        onHabitsEvent(HabitsEvent.ClearDialogFields)
                        onHabitsEvent(HabitsEvent.HideAddHabitDialog)
                    },
                    modifier = Modifier
//                        .padding(spacing.spaceExtraSmall)
                        .weight(1f)
                )

                // Save task
                PilledTextButtonComponent(
                    text = "Save",
                    textColor = MaterialTheme.colorScheme.onPrimary,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    backgroundColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        if (habitsState.habitTitle.isNotEmpty()) {
                            isTitleValid.value = true
                            onHabitsEvent(HabitsEvent.SaveHabit)
                            onHabitsEvent(HabitsEvent.HideAddHabitDialog)
                        } else {
                            isTitleValid.value = false
                        }
                    },
                    modifier = Modifier
//                        .padding(vertical = spacing.spaceLarge)
                        .weight(1f)
                )
            }
        }
    }
}