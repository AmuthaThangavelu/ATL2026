package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val WonderPlayColorScheme = lightColorScheme(
    primary = VibrantAmber,
    onPrimary = Color(0xFF3E2723),
    primaryContainer = VibrantAmberLight,
    onPrimaryContainer = VibrantAmberShadow,
    secondary = VibrantRose,
    onSecondary = Color.White,
    secondaryContainer = VibrantRoseLight,
    onSecondaryContainer = VibrantRoseShadow,
    tertiary = VibrantLime,
    onTertiary = Color(0xFF1B5E20),
    tertiaryContainer = VibrantLimeLight,
    onTertiaryContainer = VibrantLimeShadow,
    background = VibrantCyan,
    onBackground = VibrantSky900,
    surface = VibrantWhite,
    onSurface = VibrantSky900,
    surfaceVariant = VibrantSkyLight,
    onSurfaceVariant = VibrantSky700
)

val WonderPlayShapes = Shapes(
    extraSmall = RoundedCornerShape(14.dp),
    small = RoundedCornerShape(20.dp),
    medium = RoundedCornerShape(28.dp),
    large = RoundedCornerShape(36.dp),
    extraLarge = RoundedCornerShape(44.dp)
)

@Composable
fun WonderPlayTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = WonderPlayColorScheme,
        typography = Typography,
        shapes = WonderPlayShapes,
        content = content
    )
}

