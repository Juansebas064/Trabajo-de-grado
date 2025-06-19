package com.brightbox.dino.view.manage_elements

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.R
import com.brightbox.dino.events.CategoriesEvent
import com.brightbox.dino.model.CategoriesModel
import com.brightbox.dino.viewmodel.CategoriesViewModel
import com.brightbox.dino.view.common.IconButtonComponent
import com.brightbox.dino.view.manage_elements.components.AddCategoryDialog
import com.brightbox.dino.view.manage_elements.components.DeleteCategoryDialog
import com.brightbox.dino.view.theme.LocalSpacing

@Composable
fun ManageCategoriesView(
    modifier: Modifier = Modifier,
    categoriesViewModel: CategoriesViewModel = hiltViewModel(),
) {
    val spacing = LocalSpacing.current
    val context = LocalContext.current

    val state = categoriesViewModel.state.collectAsState()
    val categories: List<CategoriesModel> = state.value.categories

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {

            // Add category
            if (state.value.isAddingCategory) {
                AddCategoryDialog(
                    categoriesState = state.value,
                    onCategoriesEvent = categoriesViewModel::onEvent,
                )
            }

            // Detele category
            if (state.value.isDeletingCategory) {
                DeleteCategoryDialog(
                    onCategoriesEvent = categoriesViewModel::onEvent
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = spacing.spaceMedium,
                        bottom = spacing.spaceExtraLarge
                    )
            ) {
                // Title of view
                Text(
                    text = context.getString(R.string.categories),
                    color = MaterialTheme.colorScheme.onBackground,
                    style = MaterialTheme.typography.titleLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (categories.isNotEmpty()) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    items(
                        items = categories,
                        key = { it.id.toString() + it.name }
                    ) { category ->
                        ElevatedCard(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface,
                                contentColor = MaterialTheme.colorScheme.onSurface,
                            ),
                            shape = RoundedCornerShape(spacing.spaceSmall),
                            elevation = CardDefaults.cardElevation(0.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(
                                        horizontal = spacing.spaceMedium,
                                        vertical = spacing.spaceSmall
                                    )
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    Text(
                                        text = category.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                    )

                                    IconButtonComponent(
                                        size = 40.dp,
                                        onClick = {
                                            categoriesViewModel.onEvent(
                                                CategoriesEvent.SetCategoryId(category.id!!)
                                            )
                                            categoriesViewModel.onEvent(
                                                CategoriesEvent.ShowDeleteDialog
                                            )
                                        },
                                        containerColor = MaterialTheme.colorScheme.error,
                                        contentColor = MaterialTheme.colorScheme.onError,
                                        icon = Icons.Default.Delete,
                                        contentDescription = "Delete category"
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = spacing.spaceExtraLarge)
                ) {
                    Text(
                        modifier = Modifier,
                        text = context.getString(R.string.tap_plus_to_create_a_category),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            Row {
                IconButtonComponent(
                    onClick = {
                        categoriesViewModel.onEvent(CategoriesEvent.ShowDialog)
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    icon = Icons.Default.Add,
                    contentDescription = "Add category"
                )
            }
        }
    }
}