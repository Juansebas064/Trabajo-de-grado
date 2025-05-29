package com.brightbox.dino.views.applications_limit.components

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.events.LimitsEvent
import com.brightbox.dino.model.ApplicationsModel
import com.brightbox.dino.viewmodel.LimitsViewModel
import com.brightbox.dino.views.common.ApplicationComponent
import com.brightbox.dino.views.common.IconButtonComponent
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun ApplicationLimitComponent(
    modifier: Modifier = Modifier,
    application: ApplicationsModel,
    icon: Drawable,
    selectedApplicationsToLimit: Map<String, Int>,
    isSelectedToLimit: Boolean,
    onLimitsEvent: (LimitsEvent) -> Unit,
    limitsViewModel: LimitsViewModel = hiltViewModel()
) {
    val spacing = LocalSpacing.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!isSelectedToLimit) {
                    onLimitsEvent(
                        LimitsEvent.AddApplicationToLimit(
                            application.packageName
                        )
                    )
                    Log.d(
                        "Limits",
                        "Added: ${limitsViewModel.state.value.limitsList}"
                    )
                } else {
                    onLimitsEvent(
                        LimitsEvent.RemoveApplicationToLimit(
                            application.packageName
                        )
                    )
                    Log.d(
                        "ApplicationListToSetLimitsComponent",
                        "Removed: ${application.packageName}"
                    )
                }
            }
            .clip(RoundedCornerShape(spacing.spaceSmall))
            .background(
                if (!isSelectedToLimit)
                    MaterialTheme.colorScheme.background
                else MaterialTheme.colorScheme.surface
            )
            .padding(spacing.spaceMedium)

            .animateContentSize()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Max)
        ) {
            ApplicationComponent(
                modifier = Modifier
                    .animateContentSize()
                    .weight(1f)
                ,
                application = application,
                showIcon = true,
                icon = icon,
                textStyle = MaterialTheme.typography.bodyMedium,
                textColor =
                    if (!isSelectedToLimit)
                        MaterialTheme.colorScheme.onBackground
                    else MaterialTheme.colorScheme.onSurface
            )

            AnimatedVisibility(
                visible = isSelectedToLimit,
                enter = slideInHorizontally(),
                exit = slideOutHorizontally(tween(50)),
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall),
                    modifier = Modifier
                ) {
                    IconButtonComponent(
                        onClick = {
                            val timeResult =
                                selectedApplicationsToLimit[application.packageName]!! - 5
                            onLimitsEvent(
                                LimitsEvent.EditApplicationToLimit(
                                    application.packageName,
                                    if (timeResult < 0) 0 else timeResult
                                )
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        icon = Icons.Default.Remove,
                        size = 40.dp,
                        contentDescription = "Minus 5 minutes"
                    )
                    BasicTextField(
                        value = selectedApplicationsToLimit[application.packageName].let { value ->
                            if (value == null || value == 0) {
                                ""
                            } else {
                                value.toString()
                            }
                        },
                        onValueChange = {
                            onLimitsEvent(
                                LimitsEvent.EditApplicationToLimit(
                                    application.packageName,
                                    it.let { value ->
                                        if (value.isEmpty()) {
                                            0
                                        } else {
                                            value.toInt()
                                        }
                                    }
                                )
                            )
                        },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            textAlign = TextAlign.Center
                        ),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            keyboardType = KeyboardType.Number
                        ),
                        maxLines = 1,
                        decorationBox = { innerTextField ->
                            Column(
                                modifier = Modifier.fillMaxHeight()
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .weight(1f)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .width(40.dp)
                                    ) {
                                        if (selectedApplicationsToLimit[application.packageName] == 0) {
                                            Text(
                                                text = "0",
                                                color = MaterialTheme.colorScheme.onSurface.copy(
                                                    0.4f
                                                ),
                                                modifier = Modifier.align(Alignment.Center)
                                            )
                                        }
                                        innerTextField()
                                    }
                                    Text(
                                        text = "min",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        textAlign = TextAlign.Center
                                    )

                                }
                                Box(
                                    modifier = Modifier
                                        .height(1.dp)
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primary)
                                )
                            }
                        },
                        modifier = Modifier
//                            .fillMaxHeight()
                            .width(80.dp)
                    )
                    IconButtonComponent(
                        onClick = {
                            onLimitsEvent(
                                LimitsEvent.EditApplicationToLimit(
                                    application.packageName,
                                    selectedApplicationsToLimit[application.packageName]!! + 5
                                )
                            )
                        },
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        icon = Icons.Default.Add,
                        size = 40.dp,
                        contentDescription = "Plus 5 minutes"
                    )
                }
            }
        }
    }
}