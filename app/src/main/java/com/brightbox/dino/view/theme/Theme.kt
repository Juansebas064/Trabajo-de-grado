package com.brightbox.dino.view.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import com.brightbox.dino.viewmodel.preferences.PreferencesViewModel

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
fun DinoLauncherTheme(
//    darkTheme: Boolean = isSystemInDarkTheme(),
    preferencesViewModel: PreferencesViewModel = hiltViewModel(),
    content: @Composable () -> Unit
) {
    val preferencesState = preferencesViewModel.state.collectAsState()
    val theme = preferencesState.value.theme

//    val themeChangerFlow = preferencesViewModel.themeChangerFlow.collectAsState()

    if (!preferencesState.value.isLoading) {
        val colorScheme = when(theme) {
            "system" -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
            "light" -> LightColorScheme
            "dark" -> DarkColorScheme
            else -> if (isSystemInDarkTheme()) DarkColorScheme else LightColorScheme
        }

        CompositionLocalProvider(LocalSpacing provides Dimensions()) {
            MaterialTheme(
                colorScheme = colorScheme.animateAllColors(),
                typography = Typography,
                content = content
            )
        }
    }
}


// Duración de la animación (puedes ajustarla)
private const val COLOR_ANIMATION_DURATION_MS = 400 // 700 milisegundos

@Stable
@Composable
fun ColorScheme.animateAllColors(
    animationSpec: androidx.compose.animation.core.AnimationSpec<Color> = tween(durationMillis = COLOR_ANIMATION_DURATION_MS)
): ColorScheme {
    // Anima cada color que uses en tu aplicación.
    // Es importante que los nombres coincidan con los de ColorScheme.
    val primary = animateColorAsState(this.primary, animationSpec).value
    val onPrimary = animateColorAsState(this.onPrimary, animationSpec).value
    val primaryContainer = animateColorAsState(this.primaryContainer, animationSpec).value
    val onPrimaryContainer = animateColorAsState(this.onPrimaryContainer, animationSpec).value
    val inversePrimary = animateColorAsState(this.inversePrimary, animationSpec).value
    val secondary = animateColorAsState(this.secondary, animationSpec).value
    val onSecondary = animateColorAsState(this.onSecondary, animationSpec).value
    val secondaryContainer = animateColorAsState(this.secondaryContainer, animationSpec).value
    val onSecondaryContainer = animateColorAsState(this.onSecondaryContainer, animationSpec).value
    val tertiary = animateColorAsState(this.tertiary, animationSpec).value
    val onTertiary = animateColorAsState(this.onTertiary, animationSpec).value
    val tertiaryContainer = animateColorAsState(this.tertiaryContainer, animationSpec).value
    val onTertiaryContainer = animateColorAsState(this.onTertiaryContainer, animationSpec).value
    val background = animateColorAsState(this.background, animationSpec).value
    val onBackground = animateColorAsState(this.onBackground, animationSpec).value
    val surface = animateColorAsState(this.surface, animationSpec).value
    val onSurface = animateColorAsState(this.onSurface, animationSpec).value
    val surfaceVariant = animateColorAsState(this.surfaceVariant, animationSpec).value
    val onSurfaceVariant = animateColorAsState(this.onSurfaceVariant, animationSpec).value
    val surfaceTint = animateColorAsState(this.surfaceTint, animationSpec).value
    val inverseSurface = animateColorAsState(this.inverseSurface, animationSpec).value
    val inverseOnSurface = animateColorAsState(this.inverseOnSurface, animationSpec).value
    val error = animateColorAsState(this.error, animationSpec).value
    val onError = animateColorAsState(this.onError, animationSpec).value
    val errorContainer = animateColorAsState(this.errorContainer, animationSpec).value
    val onErrorContainer = animateColorAsState(this.onErrorContainer, animationSpec).value
    val outline = animateColorAsState(this.outline, animationSpec).value
    val outlineVariant = animateColorAsState(this.outlineVariant, animationSpec).value
    val scrim = animateColorAsState(this.scrim, animationSpec).value
    val surfaceBright = animateColorAsState(this.surfaceBright, animationSpec).value
    val surfaceContainer = animateColorAsState(this.surfaceContainer, animationSpec).value
    val surfaceContainerHigh = animateColorAsState(this.surfaceContainerHigh, animationSpec).value
    val surfaceContainerHighest =
        animateColorAsState(this.surfaceContainerHighest, animationSpec).value
    val surfaceContainerLow = animateColorAsState(this.surfaceContainerLow, animationSpec).value
    val surfaceContainerLowest =
        animateColorAsState(this.surfaceContainerLowest, animationSpec).value
    val surfaceDim = animateColorAsState(this.surfaceDim, animationSpec).value


    // Devuelve un nuevo ColorScheme con los colores animados.
    // Es importante copiar TODOS los campos, incluso si algunos no los animas explícitamente,
    // para mantener la integridad del ColorScheme.
    return this.copy(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        surfaceBright = surfaceBright,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainerLowest = surfaceContainerLowest,
        surfaceDim = surfaceDim
        // Asegúrate de incluir todos los campos si tu versión de Material3 tiene más
    )
}

// Ya tienes esta función, puedes ajustar `tween` si es necesario
// o usar el animationSpec de la función de extensión.
// @Composable
// private fun animateColor(targetValue: Color) =
//    animateColorAsState(
//        targetValue = targetValue,
//        animationSpec = tween(durationMillis = 2000) // Duración bastante larga
//    ).value