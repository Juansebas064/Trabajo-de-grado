package com.brightbox.hourglass.views.home

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brightbox.hourglass.utils.formatMillisecondsToMinutes
import com.brightbox.hourglass.viewmodel.PomodoroViewModel
import com.brightbox.hourglass.views.common.IconButtonComponent
import com.brightbox.hourglass.views.common.TextFieldComponent
import com.brightbox.hourglass.views.theme.LocalSpacing

@Composable
fun PomodoroPageView(
    modifier: Modifier = Modifier,
) {
    val pomodoroViewModel: PomodoroViewModel = viewModel()

    val sessionTime = pomodoroViewModel.sessionTime.collectAsState()
    val breakTime = pomodoroViewModel.breakTime.collectAsState()

    val elapsedTime = pomodoroViewModel.elapsedTime.collectAsState().let { millis ->
        formatMillisecondsToMinutes(millis.value, showSeconds = true)
    }
    var currentProgress = pomodoroViewModel.progressIndicator.collectAsState()

    val isTimerRunning = pomodoroViewModel.isTimerRunning.collectAsState()
    val isSessionTimeRunning = pomodoroViewModel.isSessionTimeRunning.collectAsState()
    val isBreakTimeRunning = pomodoroViewModel.isBreakTimeRunning.collectAsState()

    val spacing = LocalSpacing.current
    val clockSize = 300.dp

    // Container
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        // Column for content
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Title
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(spacing.spaceMedium)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Pomodoro",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(Modifier.height(spacing.spaceMedium))

            // Clock
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface)
                    .size(clockSize)
            ) {
                // Clock content
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = elapsedTime,
                        style = MaterialTheme.typography.displayMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = if (isBreakTimeRunning.value) "Break time" else "Session time",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )

                }

                // Progress indicator
                CircularProgressIndicator(
                    progress = {
                        currentProgress.value
                    },
                    modifier = Modifier.size(clockSize),
                    color = if (isSessionTimeRunning.value) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.inversePrimary,
                    strokeWidth = spacing.spaceSmall + spacing.default,
                    trackColor = Color.Transparent,
                )
            }


            Spacer(
                modifier = Modifier
                    .height(spacing.spaceLarge)
            )

            // Fields container
            Column(
                verticalArrangement = Arrangement.spacedBy(
                    spacing.spaceMedium,
                ),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .weight(1f)
            ) {
                // Session Time
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Session time",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
                    ) {
                        TextFieldComponent(
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                                .width(80.dp),
                            enabled = !isTimerRunning.value,
                            value = sessionTime.value,
                            textStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                            backgroundColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            indicationColor = MaterialTheme.colorScheme.onBackground,
                            onValueChange = {
                                pomodoroViewModel.updateSessionTime(it)
                            },
                        )

                        Text(
                            text = "min",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }

                // Break Time
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "Break time",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onBackground,
                        textAlign = TextAlign.Start
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(spacing.spaceSmall)
                    ) {
                        TextFieldComponent(
                            modifier = Modifier
                                .height(IntrinsicSize.Min)
                                .width(80.dp),
                            enabled = !isTimerRunning.value,
                            value = breakTime.value,
                            textStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                            backgroundColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                            indicationColor = MaterialTheme.colorScheme.onBackground,
                            onValueChange = {
                                pomodoroViewModel.updateBreakTime(it)
                            },
                        )

                        Text(
                            text = "min",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
            }

            Row(
                verticalAlignment = Alignment.Bottom,
            ) {
                IconButtonComponent(
                    modifier = Modifier,
//                        .padding(horizontal = spacing.spaceLarge)
                    onClick = {
                        if (!isTimerRunning.value) {
                            pomodoroViewModel.startTimer()
                        } else {
                            pomodoroViewModel.pauseTimer()
                        }
                    },
                    containerColor = if (isSessionTimeRunning.value) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.inversePrimary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    icon = if (!isTimerRunning.value) {
                        Icons.Default.PlayArrow
                    } else {
                        Icons.Default.Pause
                    },
                    contentDescription = "Pomodoro timer control",
                )
            }

        }
    }
}