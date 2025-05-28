package com.brightbox.hourglass.views.common

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun RoundedSquareButtonComponent(
    text: String,
    textStyle: TextStyle,
    contentColor: Color,
    containerColor: Color,
    padding: Dp? = null,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val spacing = LocalSpacing.current

    Button(
        shape = RoundedCornerShape(spacing.spaceSmall),
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