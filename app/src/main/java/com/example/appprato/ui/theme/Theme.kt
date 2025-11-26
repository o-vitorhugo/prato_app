package com.example.appprato.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = Green_light_primary,
    onPrimary = Green_light_onPrimary,
    primaryContainer = Green_light_primaryContainer,
    onPrimaryContainer = Green_light_onPrimaryContainer,
    secondary = Green_light_secondary,
    onSecondary = Green_light_onSecondary,
    secondaryContainer = Green_light_secondaryContainer,
    onSecondaryContainer = Green_light_onSecondaryContainer,
    tertiary = Green_light_tertiary,
    onTertiary = Green_light_onTertiary,
    tertiaryContainer = Green_light_tertiaryContainer,
    onTertiaryContainer = Green_light_onTertiaryContainer,
    error = Green_light_error,
    errorContainer = Green_light_errorContainer,
    onError = Green_light_onError,
    onErrorContainer = Green_light_onErrorContainer,
    background = Green_light_background,
    onBackground = Green_light_onBackground,
    surface = Green_light_surface,
    onSurface = Green_light_onSurface,
    surfaceVariant = Green_light_surfaceVariant,
    onSurfaceVariant = Green_light_onSurfaceVariant,
    outline = Green_light_outline,
    inverseOnSurface = Green_light_inverseOnSurface,
    inverseSurface = Green_light_inverseSurface,
    inversePrimary = Green_light_inversePrimary,
    surfaceTint = Green_light_surfaceTint,
)

private val DarkColorScheme = darkColorScheme(
    primary = Green_dark_primary,
    onPrimary = Green_dark_onPrimary,
    primaryContainer = Green_dark_primaryContainer,
    onPrimaryContainer = Green_dark_onPrimaryContainer,
    secondary = Green_dark_secondary,
    onSecondary = Green_dark_onSecondary,
    secondaryContainer = Green_dark_secondaryContainer,
    onSecondaryContainer = Green_dark_onSecondaryContainer,
    tertiary = Green_dark_tertiary,
    onTertiary = Green_dark_onTertiary,
    tertiaryContainer = Green_dark_tertiaryContainer,
    onTertiaryContainer = Green_dark_onTertiaryContainer,
    error = Green_dark_error,
    errorContainer = Green_dark_errorContainer,
    onError = Green_dark_onError,
    onErrorContainer = Green_dark_onErrorContainer,
    background = Green_dark_background,
    onBackground = Green_dark_onBackground,
    surface = Green_dark_surface,
    onSurface = Green_dark_onSurface,
    surfaceVariant = Green_dark_surfaceVariant,
    onSurfaceVariant = Green_dark_onSurfaceVariant,
    outline = Green_dark_outline,
    inverseOnSurface = Green_dark_inverseOnSurface,
    inverseSurface = Green_dark_inverseSurface,
    inversePrimary = Green_dark_inversePrimary,
    surfaceTint = Green_dark_surfaceTint,
)

@Composable
fun AppPratoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
