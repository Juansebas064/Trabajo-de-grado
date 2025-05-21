package com.brightbox.hourglass.views.theme

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
    onSecondary = White,
    tertiary = LightGreen,
    onTertiary = White,
    surfaceVariant = LightYellow,
    onSurfaceVariant = Black,
    inversePrimary = Purple,
    surfaceTint = Orange,
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
    onPrimary = Bone,
    secondary = Ocean,
    onSecondary = Bone,
    tertiary = Green,
    onTertiary = Bone,
    surfaceVariant = Yellow,
    onSurfaceVariant = Black,
    inversePrimary = Purple,
    error = Red,
    onError = Bone,
    surfaceTint = Orange,

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