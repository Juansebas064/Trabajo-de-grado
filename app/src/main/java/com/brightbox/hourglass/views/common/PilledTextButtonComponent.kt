package com.brightbox.hourglass.views.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun PilledTextButtonComponent(
    text: String,
    textColor: Color,
    textStyle: TextStyle,
    backgroundColor: Color,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val spacing = LocalSpacing.current
    ElevatedButton(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = spacing.default
        ),
        contentPadding = PaddingValues(12.dp),
        shape = RoundedCornerShape(15.dp),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
        ) {
            Text(
                text = text,
                style = textStyle,
                color = textColor
            )
        }
    }
}