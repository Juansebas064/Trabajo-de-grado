package com.brightbox.dino.views.home.pages.tasks_and_habits_page.components

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.events.CategoriesEvent
import com.brightbox.dino.viewmodel.CategoriesViewModel
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun CategorySelectorComponent(
    modifier: Modifier = Modifier,
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
    categoryInitialValue: String = "",
    onCategoryChange: (Int) -> Unit,
) {
    val categoriesState = categoriesViewModel.state.collectAsState()

    val spacing = LocalSpacing.current
    val areCategoriesExpanded = remember {
        mutableStateOf(false)
    }
    val categoriesDropdownPosition = remember {
        mutableIntStateOf(-1)
    }

//    LaunchedEffect(key1 = categoriesState.value.categories) {
//        if (categoriesState.value.categories.isEmpty()) {
//            categoriesDropdownPosition.value = -1
//        }
//    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        // Categories Dropdown
        Box(
            modifier = Modifier
                .weight(1f)
                .width(IntrinsicSize.Min)
        ) {
            OutlinedTextField(
                value = if (categoriesDropdownPosition.intValue == -1 || categoriesState.value.categories.isEmpty()) categoryInitialValue else categoriesState.value.categories[categoriesDropdownPosition.intValue].name,
                onValueChange = {
                    onCategoryChange(it.toInt())
//                    onHabitsEvent(HabitsEvent.SetHabitCategory(it.toInt()))
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
                },
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
                    .fillMaxWidth(0.55f)
            ) {
                categoriesState.value.categories.forEachIndexed { index, category ->
                    DropdownMenuItem(
                        text = {
                            Text(text = category.name)
                        },
                        onClick = {
                            onCategoryChange(category.id!!)
                            categoriesDropdownPosition.intValue = index
                            areCategoriesExpanded.value = !areCategoriesExpanded.value
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete category",
                                modifier = Modifier
                                    .scale(0.8f)
                                    .clickable {
                                        categoriesViewModel.onEvent(
                                            CategoriesEvent.DeleteCategory(category.id!!)
                                        )
                                    }
                            )
                        }
                    )
                }
            }
        }

        // Add category
        IconButton(
            onClick = {
                if (!categoriesState.value.isAddingCategory)
                    categoriesViewModel.onEvent(CategoriesEvent.ShowDialog)
                else categoriesViewModel.onEvent(CategoriesEvent.HideDialog)
            },
            modifier = Modifier
                .offset(y = 5.dp)
                .clip(shape = RoundedCornerShape(spacing.spaceSmall))
                .background(MaterialTheme.colorScheme.secondary)
        ) {
            Icon(
                imageVector = if (!categoriesState.value.isAddingCategory) Icons.Default.Add else Icons.Default.Close,
                contentDescription = "Add category",
                tint = MaterialTheme.colorScheme.onSecondary
            )
        }
    }

    AnimatedVisibility(
        visible = categoriesState.value.isAddingCategory,
        enter = fadeIn() + slideInVertically(),
        exit = fadeOut() + slideOutVertically(),
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(8.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.secondary)
                    .offset(y = 28.dp)
            )
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = categoriesState.value.categoryName,
                onValueChange = {
                    categoriesViewModel.onEvent(CategoriesEvent.SetCategoryName(it))
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
                        Log.d("CategorySelectorComponent", "Done action fired")
                        categoriesViewModel.onEvent(CategoriesEvent.SaveCategory)
                    }
                ),
            )

            // Confirm add category
            IconButton(
                onClick = {
                    categoriesViewModel.onEvent(CategoriesEvent.SaveCategory)
                    categoriesViewModel.onEvent(CategoriesEvent.HideDialog)
                    categoriesViewModel.onEvent(CategoriesEvent.SetCategoryName(""))
                },
                modifier = Modifier
                    .offset(y = 5.dp)
                    .clip(shape = RoundedCornerShape(spacing.spaceSmall))
                    .background(MaterialTheme.colorScheme.tertiary)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirm add category",
                    tint = MaterialTheme.colorScheme.onTertiary
                )
            }
        }
    }
}