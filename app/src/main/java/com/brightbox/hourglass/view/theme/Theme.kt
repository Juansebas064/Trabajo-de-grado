package com.brightbox.hourglass.view.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

private val DarkColorScheme = darkColorScheme(
    background = Black,
    onBackground = White,
    surface = LightBlue,
    onSurface = Black,
    inverseSurface = BlackSemiTransparent,
    primary = BlueDarker,
    onPrimary = White,
    secondary = OceanDarker,
    onSecondary = Black,
    tertiary = LightGreen,
    onTertiary = Black,
    surfaceVariant = LightYellow,
    onSurfaceVariant = Black,
    error = LightRed,
    onError = White,
)


private val LightColorScheme = lightColorScheme(
    background = BoneDarker,
    onBackground = Gray,
    surface = Bone,
    onSurface = Gray,
    inverseSurface = LightGray,
    primary = Blue,
    onPrimary = White,
    secondary = Ocean,
    onSecondary = White,
    tertiary = Green,
    onTertiary = White,
    surfaceVariant = Yellow,
    onSurfaceVariant = White,
    error = Red,
    onError = White,

    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun HourglassProductivityLauncherTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    CompositionLocalProvider(LocalSpacing provides Dimensions()) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }


}