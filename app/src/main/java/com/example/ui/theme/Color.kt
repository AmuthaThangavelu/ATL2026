package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// Vibrant Palette - Theme Color Tokens (Extracted from Design HTML)
val VibrantCyan = Color(0xFFA5F3FC)          // Base background #A5F3FC
val VibrantCyanDark = Color(0xFF06B6D4)      // Cyan-500
val VibrantSkyLight = Color(0xFFBAE6FD)      // Sky-200
val VibrantSky300 = Color(0xFF7DD3FC)        // Sky-300
val VibrantSky400 = Color(0xFF38BDF8)        // Sky-400
val VibrantSky900 = Color(0xFF0C4A6E)        // Sky-900 text
val VibrantSky700 = Color(0xFF0369A1)        // Sky-700 labels / subtitles
val VibrantSky600 = Color(0xFF0284C7)        // Sky-600

// Chunky 3D Gummy Accent Palette
val VibrantAmber = Color(0xFFFBBF24)         // Amber-400 #FBBF24
val VibrantAmberShadow = Color(0xFFB45309)   // Amber-700 #B45309 3D Bevel
val VibrantAmberLight = Color(0xFFFEF08A)    // Amber-200 #FEF08A Top Highlight

val VibrantRose = Color(0xFFF43F5E)          // Rose-500 #F43F5E
val VibrantRoseShadow = Color(0xFF9F1239)    // Rose-800 #9F1239 3D Bevel
val VibrantRoseLight = Color(0xFFFDA4AF)     // Rose-300 #FDA4AF Top Highlight

val VibrantLime = Color(0xFFA3E635)          // Lime-400 #A3E635
val VibrantLimeShadow = Color(0xFF4D7C0F)    // Lime-700 3D Bevel
val VibrantLimeLight = Color(0xFFBEF264)     // Lime-300 #BEF264 Top Highlight
val VibrantEmerald = Color(0xFF10B981)       // Emerald-500

val VibrantPurple = Color(0xFFA855F7)        // Purple-500 #A855F7
val VibrantPurpleShadow = Color(0xFF6D28D9)  // Purple-700 3D Bevel
val VibrantPurpleLight = Color(0xFFE9D5FF)   // Purple-200

val VibrantPink = Color(0xFFF472B6)          // Pink-400 #F472B6
val VibrantPinkShadow = Color(0xFFBE185D)    // Pink-700 3D Bevel
val VibrantPinkLight = Color(0xFFFBCFE8)     // Pink-200

val VibrantCoral = Color(0xFFFB923C)         // Orange-400
val VibrantCoralShadow = Color(0xFFC2410C)   // Orange-700

// Surface & Card Neutrals
val VibrantWhite = Color(0xFFFFFFFF)
val VibrantBevelShadow = Color(0xFFE2E8F0)   // #E2E8F0 3D drop shadow
val VibrantBevelDark = Color(0xFFCBD5E1)     // Slate-300

// Legacy alias mappings for backward compatibility
val SunnyYellow = VibrantAmber
val SunshineOrange = VibrantCoral
val CoralRed = VibrantRose
val BubblePink = VibrantPink
val CandyPurple = VibrantPurple
val SkyBlue = VibrantSky400
val OceanBlue = VibrantCyanDark
val MintGreen = VibrantLime
val LimeGreen = VibrantLime
val LavenderSoft = VibrantPurpleLight
val SoftCream = Color(0xFFFFFDE7)
val CardBackground = VibrantWhite
val TextDark = VibrantSky900
val TextSubtitle = VibrantSky700

val CardGlowYellow = Color(0xFFFEF9C3)
val CardGlowPink = Color(0xFFFCE7F3)
val CardGlowBlue = Color(0xFFE0F2FE)
val CardGlowGreen = Color(0xFFECFCCB)
val CardGlowPurple = Color(0xFFF3E8FF)

// Vibrant Palette Gradients
val RainbowBrush = Brush.horizontalGradient(
    colors = listOf(
        VibrantRose,
        VibrantCoral,
        VibrantAmber,
        VibrantLime,
        VibrantSky400,
        VibrantPurple
    )
)

val SkyGradientBrush = Brush.verticalGradient(
    colors = listOf(
        VibrantCyan,
        VibrantSkyLight,
        Color(0xFFE0F2FE)
    )
)

val VibrantMainSkyGradient = Brush.verticalGradient(
    colors = listOf(
        VibrantSky300,
        VibrantSky400
    )
)


