package com.brightbox.dino.view.common

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.zIndex
import com.brightbox.dino.view.theme.LocalSpacing

@Composable
fun BottomModalDialogComponent(
    modifier: Modifier = Modifier,
    onDismissRequest: (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    val spacing = LocalSpacing.current

    Dialog(
        onDismissRequest = {
            onDismissRequest(false)
        },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        content = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(
                            color = MaterialTheme.colorScheme.onBackground
                                .copy(alpha = 0f),
                            bounded = false,
                            radius = 0.dp
                        ),
                        onClick = {
                            onDismissRequest(false)
                        }
                    )
                    .padding(spacing.spaceLarge)
                    .animateContentSize()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(spacing.spaceSmall))
                        .align(Alignment.BottomEnd)
                        .zIndex(2f)
                        .clickable(
                            enabled = false,
                            onClick = {}
                        )
                ) {
                    content()
                }
            }
        }
    )
}