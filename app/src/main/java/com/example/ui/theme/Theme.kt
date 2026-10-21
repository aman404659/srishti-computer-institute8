package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SrishtiDarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = NavyDark,
    primaryContainer = CyanContainer,
    onPrimaryContainer = NeonCyan,
    secondary = CyanGlow,
    onSecondary = NavyDark,
    secondaryContainer = NavyCardElevated,
    onSecondaryContainer = WhitePure,
    tertiary = GoldAccent,
    onTertiary = NavyDark,
    tertiaryContainer = GoldContainer,
    onTertiaryContainer = GoldAccent,
    background = NavyDark,
    onBackground = TextPrimary,
    surface = NavyMidnight,
    onSurface = TextPrimary,
    surfaceVariant = NavySurface,
    onSurfaceVariant = TextSecondary,
    outline = NavyBorder,
    outlineVariant = NavyLight,
    error = ErrorRed,
    onError = WhitePure,
    errorContainer = ErrorContainer,
    onErrorContainer = WhitePure
)

private val SrishtiLightColorScheme = lightColorScheme(
    primary = NavyMidnight,
    onPrimary = WhitePure,
    primaryContainer = Color(0xFFE0F7FA),
    onPrimaryContainer = NavyDark,
    secondary = NeonCyanDark,
    onSecondary = WhitePure,
    secondaryContainer = Color(0xFFE2E8F0),
    onSecondaryContainer = NavyMidnight,
    tertiary = Color(0xFFB45309),
    onTertiary = WhitePure,
    tertiaryContainer = Color(0xFFFEF3C7),
    onTertiaryContainer = Color(0xFF78350F),
    background = Color(0xFFF8FAFC),
    onBackground = DarkTextPrimary,
    surface = Color(0xFFFFFFFF),
    onSurface = DarkTextPrimary,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    error = ErrorRed,
    onError = WhitePure
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to institute premium navy & neon cyan aesthetic
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) SrishtiDarkColorScheme else SrishtiLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
