package com.example.model

import androidx.compose.ui.graphics.Color

enum class GameType(
    val title: String,
    val subtitle: String,
    val emoji: String,
    val primaryColor: Color,
    val secondaryColor: Color
) {
    BALLOON_POP(
        title = "Balloon Pop",
        subtitle = "Tap, Pop & Catch Stars!",
        emoji = "🎈",
        primaryColor = Color(0xFFF43F5E), // Vibrant Rose
        secondaryColor = Color(0xFFFBBF24) // Vibrant Amber
    ),
    ANIMAL_PIANO(
        title = "Magic Xylophone",
        subtitle = "Play Joyful Tunes & Songs",
        emoji = "🎹",
        primaryColor = Color(0xFF38BDF8), // Vibrant Sky
        secondaryColor = Color(0xFFA3E635) // Vibrant Lime
    ),
    SHAPE_SORTER(
        title = "Shape Sorter",
        subtitle = "Match Cute Shapes & Colors",
        emoji = "⭐",
        primaryColor = Color(0xFFFBBF24), // Vibrant Amber
        secondaryColor = Color(0xFFF43F5E) // Vibrant Rose
    ),
    MAGIC_PAINT(
        title = "Sparkle Paint",
        subtitle = "Rainbow & Glow Finger Art",
        emoji = "🎨",
        primaryColor = Color(0xFFA855F7), // Vibrant Purple
        secondaryColor = Color(0xFF38BDF8) // Vibrant Sky
    ),
    FEED_ANIMALS(
        title = "Feed Animals",
        subtitle = "Yummy Treats for Happy Pets",
        emoji = "🐼",
        primaryColor = Color(0xFFA3E635), // Vibrant Lime
        secondaryColor = Color(0xFFFBBF24) // Vibrant Amber
    ),
    STICKER_ALBUM(
        title = "Sticker Stars",
        subtitle = "Collect Shiny Badges",
        emoji = "🏆",
        primaryColor = Color(0xFFFB923C), // Vibrant Coral
        secondaryColor = Color(0xFFFBBF24) // Vibrant Amber
    )
}

// Balloon Pop Models
data class BalloonItem(
    val id: Long,
    val xFraction: Float, // 0.05 to 0.95
    val yFraction: Float, // 1.1 down to -0.2
    val sizeDp: Float,
    val color: Color,
    val emoji: String,
    val isSpecial: Boolean = false,
    val isPopped: Boolean = false,
    val speed: Float = 0.003f
)

data class PoppedParticle(
    val id: Long,
    val x: Float,
    val y: Float,
    val color: Color,
    val vx: Float,
    val vy: Float,
    val alpha: Float = 1f,
    val size: Float = 16f,
    val text: String? = null
)

// Xylophone & Animal Music Models
data class MusicalKey(
    val id: Int,
    val noteName: String,
    val solfege: String,
    val frequency: Float,
    val color: Color,
    val animalEmoji: String,
    val animalName: String,
    val soundAction: String
)

data class NurserySong(
    val title: String,
    val emoji: String,
    val notes: List<Int>, // List of key IDs (0..7)
    val noteNames: List<String>
)

// Shape Sorter Models
enum class KidShape(
    val displayName: String,
    val emoji: String,
    val color: Color
) {
    STAR("Star", "⭐", Color(0xFFFBBF24)), // Vibrant Amber
    HEART("Heart", "❤️", Color(0xFFF43F5E)), // Vibrant Rose
    CIRCLE("Circle", "🟡", Color(0xFF38BDF8)), // Vibrant Sky
    SQUARE("Square", "🟩", Color(0xFFA3E635)), // Vibrant Lime
    TRIANGLE("Triangle", "🔺", Color(0xFFFB923C)), // Vibrant Coral
    DIAMOND("Diamond", "💎", Color(0xFFA855F7)) // Vibrant Purple
}

data class ShapeCard(
    val id: Long,
    val shape: KidShape,
    val isMatched: Boolean = false
)

// Feed Animal Models
data class AnimalFriend(
    val id: String,
    val name: String,
    val emoji: String,
    val avatarColor: Color,
    val favoriteFoods: List<FoodItem>,
    val soundResponse: String = "Nom Nom! Yummy!"
)

data class FoodItem(
    val id: String,
    val name: String,
    val emoji: String,
    val points: Int = 10
)

// Magic Paint Models
enum class BrushType(val label: String, val emoji: String) {
    RAINBOW("Rainbow", "🌈"),
    NEON_GLOW("Neon", "✨"),
    BUBBLE("Bubbles", "🫧"),
    STAR_TRAIL("Stars", "⭐"),
    HEART_STAMP("Hearts", "💖")
}

data class PaintStroke(
    val points: List<androidx.compose.ui.geometry.Offset>,
    val color: Color,
    val strokeWidth: Float,
    val brushType: BrushType
)

data class StampItem(
    val offset: androidx.compose.ui.geometry.Offset,
    val emoji: String,
    val size: Float
)

// Sticker Album & Reward Models
data class StickerReward(
    val id: String,
    val title: String,
    val emoji: String,
    val description: String,
    val starsRequired: Int,
    val isUnlocked: Boolean = false
)
