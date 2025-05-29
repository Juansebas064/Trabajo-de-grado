package com.brightbox.dino.views.home.pages.tasks_and_habits_page

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.brightbox.dino.events.CategoriesEvent
import com.brightbox.dino.events.TasksEvent
import com.brightbox.dino.states.CategoriesState
import com.brightbox.dino.views.common.RoundedSquareButtonComponent
import com.brightbox.dino.views.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCategoryDialog(
    categoriesState: CategoriesState,
    onTasksEvent: (TasksEvent) -> Unit,
    onCategoriesEvent: (CategoriesEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current
    val halfScreenDp = (LocalConfiguration.current.screenHeightDp / 5).dp

    BasicAlertDialog(
        onDismissRequest = {
            onCategoriesEvent(CategoriesEvent.HideDialog)
        },
        modifier = modifier
            .padding(top = halfScreenDp)
    ) {
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
                    text = "Add a category",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = categoriesState.categoryName,
                onValueChange = {
                    onCategoriesEvent(CategoriesEvent.SetCategoryName(it))
                },
                label = {
                    Text(
                        text = "Title",
                    )
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Text
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        Log.d("AddCategoryDialog", "Done action fired")
                        onCategoriesEvent(CategoriesEvent.SaveCategory)
                    }
                ),
            )


            Row(
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
                modifier = Modifier
                    .padding(vertical = spacing.spaceExtraSmall)
                    .fillMaxWidth()
            ) {
                RoundedSquareButtonComponent(
                    text = "Cancel",
                    contentColor = MaterialTheme.colorScheme.onError,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    containerColor = MaterialTheme.colorScheme.error,
                    onClick = {
                        onCategoriesEvent(CategoriesEvent.ClearDialogFields)
                        onCategoriesEvent(CategoriesEvent.HideDialog)
                        onTasksEvent(TasksEvent.ShowAddTaskDialog)
                    },
                    modifier = Modifier
                        .padding(spacing.spaceExtraSmall)
                        .weight(1f)
                )

                RoundedSquareButtonComponent(
                    text = "Save",
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    textStyle = MaterialTheme.typography.bodyMedium,
                    containerColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        onCategoriesEvent(CategoriesEvent.SaveCategory)
                        onTasksEvent(TasksEvent.ShowAddTaskDialog)
                    },
                    modifier = Modifier
                        .padding(spacing.spaceExtraSmall)
                        .weight(1f)
                )
            }
        }
    }
}