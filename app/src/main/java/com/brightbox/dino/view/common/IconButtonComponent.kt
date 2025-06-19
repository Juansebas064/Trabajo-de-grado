package com.brightbox.dino.view.common

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.brightbox.dino.view.theme.LocalSpacing

@Composable
fun IconButtonComponent(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    containerColor: Color,
    contentColor: Color,
    icon: ImageVector,
    size: Dp = 50.dp,
    contentDescription: String
) {
    val spacing = LocalSpacing.current
    Button(
        onClick = {
            onClick()
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(0.dp),
        shape = RoundedCornerShape(spacing.spaceSmall),
        modifier = Modifier
            .size(size)
            .then(modifier)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
        )

    }
}