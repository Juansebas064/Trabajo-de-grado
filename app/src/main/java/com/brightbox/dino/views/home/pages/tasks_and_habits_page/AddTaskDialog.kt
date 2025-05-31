package com.brightbox.dino.views.home.pages.tasks_and_habits_page

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.brightbox.dino.R
import com.brightbox.dino.constants.PrioritiesEnum
import com.brightbox.dino.events.TasksEvent
import com.brightbox.dino.states.CategoriesState
import com.brightbox.dino.states.TasksState
import com.brightbox.dino.views.common.BottomModalDialogComponent
import com.brightbox.dino.views.common.RoundedSquareButtonComponent
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.CategorySelectorComponent
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.DatePickerComponent
import com.brightbox.dino.views.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskDialog(
    tasksState: TasksState,
    categoriesState: CategoriesState,
    onTasksEvent: (TasksEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val arePrioritiesExpanded = remember {
        mutableStateOf(false)
    }
    val isTitleValid = remember {
        mutableStateOf(true)
    }
    val context = LocalContext.current

    BottomModalDialogComponent (
        onDismissRequest = {
            isTitleValid.value = true
            onTasksEvent(TasksEvent.HideAddTaskDialog)
        },
    ) {
        val focusManager = LocalFocusManager.current

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
            modifier = Modifier
                .clip(RoundedCornerShape(spacing.spaceLarge))
                .background(MaterialTheme.colorScheme.surface)
                .padding(spacing.spaceLarge)
        ) {
            Box(
                modifier = Modifier
                    .padding(top = spacing.spaceMedium)
            ) {
                Text(
                    text = if (tasksState.isEditingTask)
                        context.getString(R.string.edit_task) else context.getString(R.string.add_task),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            // Title
            Column {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = tasksState.taskTitle,
                    onValueChange = {
                        isTitleValid.value = true
                        onTasksEvent(TasksEvent.SetTaskTitle(it))
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
                        text = context.getString(R.string.title_is_required),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            // Description
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = tasksState.taskDescription,
                onValueChange = {
                    onTasksEvent(TasksEvent.SetTaskDescription(it))
                },
                label = {
                    Text(text = context.getString(R.string.description))
                },
                minLines = 2,
                maxLines = 2,
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

            // Date picker
            DatePickerComponent(
                modifier = Modifier
                    .fillMaxWidth(),
                label = context.getString(R.string.due_date),
                date = tasksState.taskDueDate,
                setDate = {
                    onTasksEvent(TasksEvent.SetTaskDueDate(it))
                },
                enabled = !tasksState.wasTaskDelayed
            )

            // Categories section
            CategorySelectorComponent(
                categoryInitialValue = categoriesState.categories.find { it.id == tasksState.taskCategory }?.name
                    ?: "",
                onCategoryChange = {
                    onTasksEvent(TasksEvent.SetTaskCategory(it))
                }
            )

            // Priorities dropdown
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = context.getString(tasksState.taskPriority.stringResource),
                    onValueChange = {
                        onTasksEvent(TasksEvent.SetTaskPriority(it))
                    },
                    readOnly = true,
                    trailingIcon = {
                        if (arePrioritiesExpanded.value) {
                            Icon(
                                imageVector = Icons.Default.ArrowDropUp,
                                contentDescription = "Add category"
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Select priority"
                            )
                        }
                    },
                    label = {
                        Text(text = context.getString(R.string.priority))
                    },
                    modifier = Modifier
                        .pointerInput(tasksState.taskPriority) {
                            awaitEachGesture {
                                // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                                // in the Initial pass to observe events before the text field consumes them
                                // in the Main pass.
                                awaitFirstDown(pass = PointerEventPass.Initial)
                                val upEvent =
                                    waitForUpOrCancellation(pass = PointerEventPass.Initial)
                                if (upEvent != null) {
                                    arePrioritiesExpanded.value = !arePrioritiesExpanded.value
                                }
                            }
                        }
                        .fillMaxWidth()
                )

                DropdownMenu(
                    expanded = arePrioritiesExpanded.value,
                    onDismissRequest = {
                        arePrioritiesExpanded.value = false
                    },
                    containerColor = MaterialTheme.colorScheme.surface,
                ) {
                    PrioritiesEnum.entries.forEach {
                        DropdownMenuItem(
                            text = {
                                Text(text = context.getString(it.stringResource))
                            },
                            onClick = {
                                onTasksEvent(TasksEvent.SetTaskPriority(it.priority))
                                arePrioritiesExpanded.value = !arePrioritiesExpanded.value
                            }
                        )
                    }
                }
            }

            Spacer(
                Modifier.height(spacing.spaceSmall)
            )

            // Save task
            RoundedSquareButtonComponent(
                text = context.getString(R.string.save),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                textStyle = MaterialTheme.typography.bodyMedium,
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    if (tasksState.taskTitle.isNotEmpty()) {
                        isTitleValid.value = true
                        onTasksEvent(TasksEvent.SaveTask)
                        onTasksEvent(TasksEvent.HideAddTaskDialog)
                    }
                    else {
                        isTitleValid.value = false
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
//                        .padding(vertical = spacing.spaceLarge)
//                    .weight(1f)
            )

            // Cancel
            RoundedSquareButtonComponent(
                text = context.getString(R.string.cancel),
                contentColor = MaterialTheme.colorScheme.onError,
                textStyle = MaterialTheme.typography.bodyMedium,
                containerColor = MaterialTheme.colorScheme.error,
                onClick = {
                    isTitleValid.value = true
                    onTasksEvent(TasksEvent.ClearDialogFields)
                    onTasksEvent(TasksEvent.HideAddTaskDialog)
                },
                modifier = Modifier
                    .fillMaxWidth()
//                        .padding(spacing.spaceExtraSmall)
//                    .weight(1f)
            )


//            Row(
//                horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
//                modifier = Modifier
//                    .padding(vertical = spacing.spaceExtraSmall)
//                    .fillMaxWidth()
//            ) {
//
//            }
        }
    }
}