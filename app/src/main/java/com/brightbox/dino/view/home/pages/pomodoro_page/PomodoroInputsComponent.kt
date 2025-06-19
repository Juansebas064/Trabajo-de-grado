package com.brightbox.dino.view.home.pages.pomodoro_page

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.brightbox.dino.R
import com.brightbox.dino.viewmodel.PomodoroViewModel
import com.brightbox.dino.view.common.TextFieldComponent
import com.brightbox.dino.view.theme.LocalSpacing

@Composable
fun PomodoroInputsComponent(
    modifier: Modifier = Modifier,
    breakTime: String,
    sessionTime: String,
    numberOfSessions: String,
    isTimerRunning: Boolean,
    enabled: Boolean = true,
    pomodoroViewModel: PomodoroViewModel = viewModel(),
) {

    val context = LocalContext.current
    val spacing = LocalSpacing.current

    // Fields container
    Column(
        verticalArrangement = Arrangement.spacedBy(
            spacing.spaceMedium,
        ),
        modifier = modifier
    ) {
        // Session Time
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = context.getString(R.string.session_time),
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
                    enabled = enabled,
                    value = sessionTime,
                    textStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    indicationColor = MaterialTheme.colorScheme.onBackground,
                    onValueChange = {
                        pomodoroViewModel.updateSessionTime(it)
                    },
                )

                Text(
                    text = context.getString(R.string.minutes_short),
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
                text = context.getString(R.string.break_time),
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
                    enabled = enabled,
                    value = breakTime,
                    textStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    indicationColor = MaterialTheme.colorScheme.onBackground,
                    onValueChange = {
                        pomodoroViewModel.updateBreakTime(it)
                    },
                )

                Text(
                    text = context.getString(R.string.minutes_short),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }
        }

        // Number of sessions
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = context.getString(R.string.number_of_sessions),
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
                    enabled = enabled,
                    value = numberOfSessions,
                    textStyle = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                    backgroundColor = Color.Transparent,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    indicationColor = MaterialTheme.colorScheme.onBackground,
                    onValueChange = {
                        pomodoroViewModel.updateNumberOfSessions(it)
                    },
                )

                Text(
                    text = context.getString(R.string.minutes_short),
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                    color = Color.Transparent,
                )
            }
        }
    }
}