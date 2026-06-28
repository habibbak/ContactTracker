package com.contacttracker.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = TealPrimary,
    onPrimary = SurfaceLight,
    secondary = CoralAccent,
    background = SurfaceLight,
    surface = SurfaceLight
)

private val DarkColors = darkColorScheme(
    primary = TealPrimaryDark,
    onPrimary = SurfaceDark,
    secondary = CoralAccentDark,
    background = SurfaceDark,
    surface = SurfaceDark
)

@Composable
fun ContactTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        content = content
    )
}
