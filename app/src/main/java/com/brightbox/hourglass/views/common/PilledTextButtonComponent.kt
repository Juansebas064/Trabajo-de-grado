package com.brightbox.hourglass.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun PilledTextButtonComponent(
    text: String,
    textColor: Color,
    textStyle: TextStyle,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = {
            onClick()
        },
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .background(backgroundColor)
    ) {
        Text(
            text = text,
            style = textStyle,
            color = textColor
        )
    }
}