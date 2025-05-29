package com.brightbox.dino.views.common

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle

@Composable
fun DropdownComponent(
    modifier: Modifier = Modifier,
    text: String,
    textStyle: TextStyle,
    textFieldLabel: String,
    contentColor: Color,
    value: String,
    expanded: Boolean,
    setExpanded: (Boolean) -> Unit,
    items: List<String>,
    onItemClick: (Int) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Box(modifier = Modifier.weight(1f)) {
            Text(
                modifier = Modifier,
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                color = contentColor
            )
        }

        Box(
            modifier = Modifier
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = {
                },
                textStyle = textStyle.copy(color = contentColor),
                readOnly = true,
                maxLines = 1,
                trailingIcon = {
                    if (expanded) {
                        Icon(
                            imageVector = Icons.Default.ArrowDropUp,
                            contentDescription = "Expand",
                            tint = contentColor
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Collapse",
                            tint = contentColor
                        )
                    }
                },
                label = {
                    Text(
                        text = textFieldLabel,
                        color = contentColor
                    )
                },
                modifier = Modifier
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            // Modifier.clickable doesn't work for text fields, so we use Modifier.pointerInput
                            // in the Initial pass to observe events before the text field consumes them
                            // in the Main pass.
                            awaitFirstDown(pass = PointerEventPass.Initial)
                            val upEvent =
                                waitForUpOrCancellation(pass = PointerEventPass.Initial)
                            if (upEvent != null) {
                                setExpanded(!expanded)
                            }
                        }
                    }
                    .then(modifier)
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = {
                    setExpanded(false)
                },
                containerColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier
            ) {
                items.forEachIndexed { index, item ->
                    DropdownMenuItem(
                        text = {
                            Text(text = item)
                        },
                        onClick = {
                            onItemClick(index)
                            setExpanded(false)
                        },
                    )
                }
            }
        }
    }
}