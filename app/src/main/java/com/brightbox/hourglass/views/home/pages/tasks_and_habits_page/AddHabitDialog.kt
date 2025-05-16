package com.brightbox.hourglass.views.home.pages.tasks_and_habits_page

import android.util.Log
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
    val areCategoriesExpanded = remember {
        mutableStateOf(false)
    }
    val arePrioritiesExpanded = remember {
        mutableStateOf(false)
    }
    val categoriesDropdownPosition = remember {
        mutableIntStateOf(-1)
    }
    val isTitleValid = remember {
        mutableStateOf(true)
    }
    val categoryInitialValue = categoriesState.categories.find { it.id == habitsState.habitCategory }?.name
        ?: ""


    BasicAlertDialog(
        onDismissRequest = {
            isTitleValid.value = true
            onHabitsEvent(HabitsEvent.HideAddHabitDialog)
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
                    onHabitsEvent(HabitsEvent.SetStartDate(it))
                },
                enabled = true
            )

            // Categories section
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                // Categories Dropdown
                Box(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    OutlinedTextField(
                        value = if (categoriesDropdownPosition.intValue == -1) categoryInitialValue else categoriesState.categories[categoriesDropdownPosition.intValue].name,
                        onValueChange = {
                            onHabitsEvent(HabitsEvent.SetHabitCategory(it.toInt()))
                        },
                        readOnly = true,
                        trailingIcon = {
                            if (areCategoriesExpanded.value) {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropUp,
                                    contentDescription = "Add category"
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
                                    contentDescription = "Add category"
                                )
                            }
                        },
                        label = {
                            Text(text = "Category")
                        },
                        modifier = Modifier
                            .pointerInput(categoriesDropdownPosition) {
                                awaitEachGesture {
                                    // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                                    // in the Initial pass to observe events before the text field consumes them
                                    // in the Main pass.
                                    awaitFirstDown(pass = PointerEventPass.Initial)
                                    val upEvent =
                                        waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                    if (upEvent != null) {
                                        areCategoriesExpanded.value = !areCategoriesExpanded.value
                                    }
                                }
                            }
                    )
                    DropdownMenu(
                        expanded = areCategoriesExpanded.value,
                        onDismissRequest = {
                            areCategoriesExpanded.value = false
                        }
                    ) {
                        categoriesState.categories.forEachIndexed { index, category ->
                            DropdownMenuItem(
                                text = {
                                    Text(text = category.name)
                                },
                                onClick = {
                                    onHabitsEvent(HabitsEvent.SetHabitCategory(category.id))
                                    categoriesDropdownPosition.intValue = index
                                    areCategoriesExpanded.value = !areCategoriesExpanded.value
                                }
                            )
                        }
                    }
                }

                // Add category
                IconButton(
                    onClick = {
                        onHabitsEvent(HabitsEvent.HideAddHabitDialog)
                        onCategoriesEvent(CategoriesEvent.ShowDialog)
                    },
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.onSurface,
                            shape = RoundedCornerShape(8.dp)
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add category",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

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
                        }
                        else {
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