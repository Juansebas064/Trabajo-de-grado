package com.brightbox.dino.views.home.pages.pomodoro_page

import android.media.MediaPlayer
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.brightbox.dino.R
import com.brightbox.dino.views.common.RoundedSquareButtonComponent
import com.brightbox.dino.views.theme.LocalSpacing

@Composable
fun TimerComponent(
    modifier: Modifier = Modifier,
    elapsedTime: String,
    elapsedNumberOfSessions: String,
    currentProgress: Float,
    isSessionTimeRunning: Boolean,
    isBreakTimeRunning: Boolean,
    timerSize: Dp
) {
    val context = LocalContext.current
    val spacing = LocalSpacing.current

    val startSessionPlayer = remember { MediaPlayer.create(context, R.raw.start_timer) }
    val finishSessionPlayer = remember { MediaPlayer.create(context, R.raw.finish_timer) }

    val animatedProgress by animateFloatAsState(
        targetValue = currentProgress,
        animationSpec = tween(
            durationMillis = 1000, // Ajusta esto según el intervalo de tu ViewModel
            easing = LinearEasing   // Para un progreso constante, o puedes probar otros easings
        ),
        label = "progressAnimation" // Etiqueta para herramientas de inspección
    )

    LaunchedEffect(isBreakTimeRunning, isSessionTimeRunning) {
        if (isSessionTimeRunning) {
            startSessionPlayer.start()
        }

        if (isBreakTimeRunning) {
            finishSessionPlayer.start()
        }
    }

    // Timer
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.surface)
            .size(timerSize)
    ) {
        // Timer content
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
                text = if (isBreakTimeRunning)
                    context.getString(R.string.break_time) else context.getString(R.string.session_time),
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Normal),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

        }

        // Progress indicator
        CircularProgressIndicator(
            progress = {
                animatedProgress
            },
            modifier = Modifier.size(timerSize),
            color = if (!isBreakTimeRunning) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.inversePrimary,
            strokeWidth = spacing.spaceSmall + spacing.default,
            trackColor = Color.Transparent,
        )

        if (isBreakTimeRunning || isSessionTimeRunning) {
            RoundedSquareButtonComponent(
                text = elapsedNumberOfSessions,
                textStyle = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                containerColor = if (!isBreakTimeRunning) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.inversePrimary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                onClick = {},
                circleShape = true,
                padding = 0.dp,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(40.dp)
                    .offset(y = (-20).dp)
            )
        }
    }
}