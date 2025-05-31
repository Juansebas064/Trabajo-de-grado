package com.brightbox.dino.views.home.pages.tasks_and_habits_page

import androidx.compose.foundation.background
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
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.brightbox.dino.R
import com.brightbox.dino.events.CategoriesEvent
import com.brightbox.dino.events.HabitsEvent
import com.brightbox.dino.states.CategoriesState
import com.brightbox.dino.states.HabitsState
import com.brightbox.dino.views.common.BottomModalDialogComponent
import com.brightbox.dino.views.common.RoundedSquareButtonComponent
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.CategorySelectorComponent
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.DatePickerComponent
import com.brightbox.dino.views.home.pages.tasks_and_habits_page.components.DaysSelectorComponent
import com.brightbox.dino.views.theme.LocalSpacing

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
    val isTitleValid = remember {
        mutableStateOf(true)
    }
    val context = LocalContext.current

    BottomModalDialogComponent(
        onDismissRequest = {
            isTitleValid.value = true
            onHabitsEvent(HabitsEvent.HideAddHabitDialog)
            onCategoriesEvent(CategoriesEvent.HideDialog)
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
//                .animateContentSize()
        ) {
            Box(
                modifier = Modifier
                    .padding(top = spacing.spaceMedium)
            ) {
                Text(
                    text = if (habitsState.isEditingHabit)
                        context.getString(R.string.edit_habit) else context.getString(R.string.add_habit),
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
                            text = context.getString(R.string.title),
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

            // Date picker
            DatePickerComponent(
                modifier = Modifier.fillMaxWidth(),
                label = context.getString(R.string.start_date),
                date = habitsState.startDate,
                setDate = {
                    onHabitsEvent(HabitsEvent.SetStartDate(it))
                },
                enabled = true
            )

            DatePickerComponent(
                modifier = Modifier.fillMaxWidth(),
                label = context.getString(R.string.end_date),
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
                text = context.getString(R.string.select_days_of_the_habit),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
            DaysSelectorComponent(
                habitsDaysOfWeek = habitsState.habitDaysOfWeek,
                setHabitsDaysOfWeek = {
                    onHabitsEvent(HabitsEvent.SetHabitDaysOfWeek(it))
                }
            )
            Spacer(modifier = Modifier.height(spacing.spaceSmall))

            // Save task
            RoundedSquareButtonComponent(
                text = context.getString(R.string.save),
                contentColor = MaterialTheme.colorScheme.onPrimary,
                textStyle = MaterialTheme.typography.bodyMedium,
                containerColor = MaterialTheme.colorScheme.primary,
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
                    .fillMaxWidth()
            )

            // Cancel
            RoundedSquareButtonComponent(
                text = context.getString(R.string.cancel),
                contentColor = MaterialTheme.colorScheme.onError,
                textStyle = MaterialTheme.typography.bodyMedium,
                containerColor = MaterialTheme.colorScheme.error,
                onClick = {
                    isTitleValid.value = true
                    onHabitsEvent(HabitsEvent.ClearDialogFields)
                    onHabitsEvent(HabitsEvent.HideAddHabitDialog)
                },
                modifier = Modifier
                    .fillMaxWidth()
            )

        }
    }
}