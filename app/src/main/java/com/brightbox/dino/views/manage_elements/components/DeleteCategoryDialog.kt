package com.brightbox.dino.views.manage_elements.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.brightbox.dino.R
import com.brightbox.dino.events.CategoriesEvent
import com.brightbox.dino.views.common.BottomModalDialogComponent
import com.brightbox.dino.views.common.RoundedSquareButtonComponent
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun DeleteCategoryDialog(
    modifier: Modifier = Modifier,
    onCategoriesEvent: (CategoriesEvent) -> Unit,
) {

    val spacing = LocalSpacing.current
    val context = LocalContext.current

    BottomModalDialogComponent(
        onDismissRequest = {
            onCategoriesEvent(CategoriesEvent.HideDeleteDialog)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.spaceLarge)

        ) {
            Box {
                Text(
                    text = context.getString(R.string.delete_elements),
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.height(spacing.spaceMedium))

            Box {
                Text(
                    text = context.getString(R.string.are_you_sure_to_delete_elements),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            Spacer(Modifier.height(spacing.spaceLarge))

            RoundedSquareButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                text = context.getString(R.string.delete),
                textStyle = MaterialTheme.typography.bodyMedium,
                contentColor = MaterialTheme.colorScheme.onError,
                containerColor = MaterialTheme.colorScheme.error,
                onClick = {
                    onCategoriesEvent(CategoriesEvent.DeleteCategory)
                },
            )

            Spacer(Modifier.height(spacing.spaceSmall))

            RoundedSquareButtonComponent(
                modifier = Modifier.fillMaxWidth(),
                text = context.getString(R.string.cancel),
                textStyle = MaterialTheme.typography.bodyMedium,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                containerColor = MaterialTheme.colorScheme.primary,
                onClick = {
                    onCategoriesEvent(CategoriesEvent.HideDeleteDialog)
                },
            )
        }
    }
}