package com.brightbox.dino.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    value: String,
    textStyle: TextStyle,
    backgroundColor: Color,
    indicationColor: Color,
    contentColor: Color,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        value = value,
        enabled = enabled,
        onValueChange = {
            onValueChange(it)
        },
        textStyle = textStyle.copy(
            textAlign = TextAlign.Center,
            color = contentColor
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        ),
        cursorBrush = SolidColor(contentColor),
        maxLines = 1,
        decorationBox = { innerTextField ->
            Column(
                modifier = Modifier
                    .background(backgroundColor)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                ) {
                    Box(
                        modifier = Modifier
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = "0",
                                color = contentColor.copy(0.5f),
                                style = textStyle,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        innerTextField()
                    }
                }

                Box(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxWidth()
                        .background(indicationColor)
                )
            }
        },
        modifier = modifier
    )
}