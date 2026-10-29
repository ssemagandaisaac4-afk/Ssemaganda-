package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = BrandGreen,
    onPrimary = Color.Black,
    primaryContainer = BrandGreenDark,
    onPrimaryContainer = Color.White,
    secondary = CyberPurple,
    onSecondary = Color.White,
    secondaryContainer = CyberIndigo,
    onSecondaryContainer = Color.White,
    tertiary = AccentAmber,
    onTertiary = Color.Black,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = DarkBorder,
    error = AccentRose,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    // Archery AI Hub is an intentional, immersive dark cyber platform
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
