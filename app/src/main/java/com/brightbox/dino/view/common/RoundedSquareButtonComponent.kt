package com.brightbox.dino.view.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.brightbox.dino.view.theme.LocalSpacing

@Composable
fun RoundedSquareButtonComponent(
    text: String,
    textStyle: TextStyle,
    contentColor: Color,
    containerColor: Color,
    padding: Dp? = null,
    onClick: () -> Unit,
    circleShape: Boolean = false,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    ElevatedButton(
        shape = if (!circleShape) RoundedCornerShape(spacing.spaceSmall) else CircleShape,
        contentPadding = PaddingValues(padding ?: spacing.spaceMedium),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        onClick = {
            onClick()
        },
        modifier = modifier
    ) {
        Text(
            text = text,
            style = textStyle,
            textAlign = TextAlign.Center,
        )
    }
}