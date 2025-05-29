package com.brightbox.dino.views.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brightbox.dino.utils.formatMillisecondsToMinutes
import com.brightbox.dino.viewmodel.PomodoroViewModel
import com.brightbox.dino.views.common.IconButtonComponent
import com.brightbox.dino.views.common.NavigationButton
import com.brightbox.dino.views.theme.LocalSpacing
import com.brightbox.dino.R
import com.brightbox.dino.views.home.pages.pomodoro_page.PomodoroInputsComponent
import com.brightbox.dino.views.home.pages.pomodoro_page.TimerComponent

@Composable
fun PomodoroPageView(
    modifier: Modifier = Modifier,
) {
    val pomodoroViewModel: PomodoroViewModel = viewModel()

    val sessionTime = pomodoroViewModel.sessionTime.collectAsState()
    val breakTime = pomodoroViewModel.breakTime.collectAsState()
    val numberOfSessions = pomodoroViewModel.numberOfSessions.collectAsState()

    val elapsedTime = pomodoroViewModel.elapsedTime.collectAsState().let { millis ->
        formatMillisecondsToMinutes(millis.value, showSeconds = true)
    }
    val elapsedNumberOfSessions = pomodoroViewModel.elapsedNumberOfSessions.collectAsState().let {
        it.value.toString()
    }
    var currentProgress = pomodoroViewModel.progressIndicator.collectAsState()

    val isTimerRunning = pomodoroViewModel.isTimerRunning.collectAsState()
    val isSessionTimeRunning = pomodoroViewModel.isSessionTimeRunning.collectAsState()
    val isBreakTimeRunning = pomodoroViewModel.isBreakTimeRunning.collectAsState()

    val spacing = LocalSpacing.current
    val timerSize = 300.dp

    val context = LocalContext.current

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
                    text = context.getString(R.string.pomodoro),
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    textAlign = TextAlign.Center
                )
            }

            // Timer component
            TimerComponent(
                elapsedTime = elapsedTime,
                elapsedNumberOfSessions = elapsedNumberOfSessions,
                currentProgress = currentProgress.value,
                isSessionTimeRunning = isSessionTimeRunning.value,
                isBreakTimeRunning = isBreakTimeRunning.value,
                timerSize = timerSize
            )

            Spacer(Modifier.height(spacing.spaceMedium))



            Spacer(
                modifier = Modifier
                    .height(spacing.spaceLarge)
            )

            // Fields component
            PomodoroInputsComponent(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .weight(1f),
                breakTime = breakTime.value,
                sessionTime = sessionTime.value,
                isTimerRunning = isTimerRunning.value,
                numberOfSessions = numberOfSessions.value,
                enabled = !isTimerRunning.value && !isBreakTimeRunning.value && !isSessionTimeRunning.value
            )


            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium)
            ) {

                if (isBreakTimeRunning.value || isSessionTimeRunning.value) {
                    IconButtonComponent(
                        modifier = Modifier,
                        onClick = {
                            pomodoroViewModel.cancelTimer()
                        },
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError,
                        icon = Icons.Default.Stop,
                        contentDescription = "Cancel Pomodoro timer",
                    )
                }

                IconButtonComponent(
                    modifier = Modifier,
                    onClick = {
                        if (!isTimerRunning.value) {
                            pomodoroViewModel.startTimer()
                        } else {
                            pomodoroViewModel.pauseTimer()
                        }
                    },
                    containerColor = if (!isBreakTimeRunning.value)
                        MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary,
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

//        NavigationButton(
//            modifier = Modifier.align(Alignment.TopEnd),
//            icon = when (preferencesState.value.theme) {
//                "system" -> Icons.Default.CloudSync
//                "light" -> Icons.Default.LightMode
//                "dark" -> Icons.Default.DarkMode
//                else -> Icons.Default.CloudSync
//            },
//            color = MaterialTheme.colorScheme.onBackground,
//            description = "Change theme",
//            onNavigate = {
//                preferencesViewModel.onEvent(
//                    GeneralPreferencesEvent.ChangeTheme
//                )
//            }
//        )
    }
}