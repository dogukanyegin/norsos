package com.example.norsos.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = SosRedLight,
    onPrimary = Color.White,
    primaryContainer = SosRedDark,
    onPrimaryContainer = Color.White,
    secondary = AlertAmber,
    onSecondary = Color.Black,
    surface = DarkSurface,
    onSurface = Color(0xFFE2E2E6),
    background = DarkBackground,
    onBackground = Color(0xFFE2E2E6),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005)
)

private val LightColorScheme = lightColorScheme(
    primary = SosRed,
    onPrimary = Color.White,
    primaryContainer = SosRedContainer,
    onPrimaryContainer = OnSosRedContainer,
    secondary = AlertAmber,
    onSecondary = Color.Black,
    surface = LightSurface,
    onSurface = Color(0xFF1B1B1F),
    background = LightBackground,
    onBackground = Color(0xFF1B1B1F),
    error = SosRed,
    onError = Color.White
)

@Composable
fun NorsosTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
