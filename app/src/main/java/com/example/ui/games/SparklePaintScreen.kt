package com.example.ui.games

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt
import com.example.audio.SoundEngine
import com.example.model.BrushType
import com.example.model.PaintStroke
import com.example.model.StampItem
import com.example.ui.components.BouncyButton
import com.example.ui.components.ConfettiCelebration
import com.example.ui.components.KidTopBar
import com.example.ui.theme.VibrantAmber
import com.example.ui.theme.VibrantAmberShadow
import com.example.ui.theme.VibrantBevelDark
import com.example.ui.theme.VibrantBevelShadow
import com.example.ui.theme.VibrantCoral
import com.example.ui.theme.VibrantCyan
import com.example.ui.theme.VibrantLime
import com.example.ui.theme.VibrantPink
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantRose
import com.example.ui.theme.VibrantSky400
import com.example.ui.theme.VibrantSky700
import com.example.ui.theme.VibrantSky900
import com.example.ui.theme.VibrantWhite
import kotlin.random.Random

@Composable
fun SparklePaintScreen(
    soundEngine: SoundEngine,
    stars: Int,
    onAddStars: (Int) -> Unit,
    onBack: () -> Unit,
    isSoundOn: Boolean,
    isMusicOn: Boolean,
    onToggleSound: () -> Unit,
    onToggleMusic: () -> Unit
) {
    val strokes = remember { mutableStateListOf<PaintStroke>() }
    val currentPoints = remember { mutableStateListOf<Offset>() }
    val stamps = remember { mutableStateListOf<StampItem>() }

    var selectedBrush by remember { mutableStateOf(BrushType.RAINBOW) }
    var selectedColor by remember { mutableStateOf(VibrantRose) }
    var strokeWidth by remember { mutableFloatStateOf(28f) }
    var selectedStampEmoji by remember { mutableStateOf("⭐") }
    var showMagicCelebration by remember { mutableStateOf(false) }

    val kidColors = listOf(
        VibrantRose,
        VibrantCoral,
        VibrantAmber,
        VibrantLime,
        VibrantCyan,
        VibrantSky400,
        VibrantPurple,
        VibrantPink,
        VibrantWhite
    )

    val stampOptions = listOf("⭐", "❤️", "🐾", "🌸", "🦄", "🧁", "🦖", "🌟", "🎈")

    fun triggerDrawingSound(offset: Offset, height: Float) {
        val ratio = (1f - (offset.y / height).coerceIn(0f, 1f))
        soundEngine.playDrawingChime(ratio)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0F172A)) // Vibrant dark slate night canvas
            .testTag("sparkle_paint_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            KidTopBar(
                title = "Sparkle Paint",
                emoji = "🎨",
                stars = stars,
                soundEngine = soundEngine,
                onBack = onBack,
                isSoundOn = isSoundOn,
                isMusicOn = isMusicOn,
                onToggleSound = onToggleSound,
                onToggleMusic = onToggleMusic,
                subtitle = "Magical Canvas"
            )

            // Brush & Stamp Modes Toolbar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(BrushType.values()) { brush ->
                        val isSelected = selectedBrush == brush
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) VibrantAmberShadow else VibrantBevelDark)
                                .padding(bottom = 3.dp)
                                .clip(RoundedCornerShape(17.dp))
                                .background(if (isSelected) VibrantAmber else Color(0xFF1E293B))
                                .border(
                                    2.dp,
                                    if (isSelected) Color.White else Color(0xFF334155),
                                    RoundedCornerShape(17.dp)
                                )
                                .clickable {
                                    soundEngine.playBoing()
                                    selectedBrush = brush
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = brush.emoji, fontSize = 17.sp)
                                Text(
                                    text = brush.label,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 12.sp,
                                    color = if (isSelected) Color(0xFF3E2723) else Color.White
                                )
                            }
                        }
                    }
                }

                // Magic Clear Wand
                CircleToolButton(
                    icon = "🪄",
                    label = "Magic Wand",
                    onClick = {
                        if (strokes.isNotEmpty() || stamps.isNotEmpty()) {
                            soundEngine.playSparkle()
                            soundEngine.playVictory()
                            showMagicCelebration = true
                            onAddStars(5)
                            strokes.clear()
                            stamps.clear()
                        }
                    },
                    backgroundColor = VibrantPurple
                )

                // Undo Button
                CircleToolButton(
                    icon = "↩️",
                    label = "Undo",
                    onClick = {
                        soundEngine.playBoing()
                        if (strokes.isNotEmpty()) {
                            strokes.removeLast()
                        } else if (stamps.isNotEmpty()) {
                            stamps.removeLast()
                        }
                    },
                    backgroundColor = VibrantSky400
                )
            }

            // Colors or Stamps Bar based on mode
            if (selectedBrush == BrushType.HEART_STAMP) {
                // Stamp selection row
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(stampOptions) { stamp ->
                        val isSelected = selectedStampEmoji == stamp
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) VibrantAmber else Color(0xFF1E293B))
                                .border(
                                    2.dp,
                                    if (isSelected) Color.White else Color(0xFF334155),
                                    CircleShape
                                )
                                .clickable {
                                    soundEngine.playBoing()
                                    selectedStampEmoji = stamp
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = stamp, fontSize = 20.sp)
                        }
                    }
                }
            } else {
                // Color selection palette
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(kidColors) { color ->
                        val isSelected = selectedColor == color
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.5.dp else 1.5.dp,
                                    color = if (isSelected) Color.White else Color.White.copy(alpha = 0.4f),
                                    shape = CircleShape
                                )
                                .clickable {
                                    soundEngine.playBoing()
                                    selectedColor = color
                                }
                        )
                    }
                }
            }

            // Magic Drawing Canvas
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF020617))
                    .border(3.dp, Color(0xFF1E293B), RoundedCornerShape(28.dp))
                    .pointerInput(selectedBrush, selectedColor, selectedStampEmoji) {
                        if (selectedBrush == BrushType.HEART_STAMP) {
                            detectTapGestures { offset ->
                                soundEngine.playSparkle()
                                stamps.add(
                                    StampItem(
                                        offset = offset,
                                        emoji = selectedStampEmoji,
                                        size = 46f
                                    )
                                )
                                onAddStars(1)
                            }
                        } else {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPoints.clear()
                                    currentPoints.add(offset)
                                    triggerDrawingSound(offset, size.height.toFloat())
                                },
                                onDrag = { change, _ ->
                                    val pos = change.position
                                    currentPoints.add(pos)
                                    triggerDrawingSound(pos, size.height.toFloat())
                                },
                                onDragEnd = {
                                    if (currentPoints.size > 1) {
                                        strokes.add(
                                            PaintStroke(
                                                points = currentPoints.toList(),
                                                color = selectedColor,
                                                strokeWidth = strokeWidth,
                                                brushType = selectedBrush
                                            )
                                        )
                                        onAddStars(1)
                                    }
                                    currentPoints.clear()
                                }
                            )
                        }
                    }
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    // Draw completed strokes
                    strokes.forEach { stroke ->
                        drawCustomStroke(stroke)
                    }

                    // Draw active dragging stroke
                    if (currentPoints.size > 1) {
                        drawCustomStroke(
                            PaintStroke(
                                points = currentPoints.toList(),
                                color = selectedColor,
                                strokeWidth = strokeWidth,
                                brushType = selectedBrush
                            )
                        )
                    }

                    // Draw stamps
                    stamps.forEach { stamp ->
                        // Draw stamp background glow
                        drawCircle(
                            color = Color.White.copy(alpha = 0.2f),
                            radius = stamp.size * 0.8f,
                            center = stamp.offset
                        )
                    }
                }

                // Render Stamp Emojis overlay
                stamps.forEach { stamp ->
                    Text(
                        text = stamp.emoji,
                        fontSize = (stamp.size * 0.75f).sp,
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    (stamp.offset.x - stamp.size * 0.5f).roundToInt(),
                                    (stamp.offset.y - stamp.size * 0.5f).roundToInt()
                                )
                            }
                    )
                }
            }

            // Bottom Friendly Touch Cue
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "✨ Draw with your finger to make glowing magical tunes! 🎶",
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = VibrantAmber
                )
            }
        }

        // Celebration Confetti
        ConfettiCelebration(
            isActive = showMagicCelebration,
            onFinished = { showMagicCelebration = false }
        )
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawCustomStroke(stroke: PaintStroke) {
    if (stroke.points.size < 2) return

    val path = Path().apply {
        moveTo(stroke.points[0].x, stroke.points[0].y)
        for (i in 1 until stroke.points.size) {
            lineTo(stroke.points[i].x, stroke.points[i].y)
        }
    }

    when (stroke.brushType) {
        BrushType.RAINBOW -> {
            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    colors = listOf(
                        VibrantRose,
                        VibrantCoral,
                        VibrantAmber,
                        VibrantLime,
                        VibrantCyan,
                        VibrantSky400,
                        VibrantPurple,
                        VibrantPink
                    )
                ),
                style = Stroke(
                    width = stroke.strokeWidth,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
        BrushType.NEON_GLOW -> {
            // Outer glow
            drawPath(
                path = path,
                color = stroke.color.copy(alpha = 0.35f),
                style = Stroke(
                    width = stroke.strokeWidth * 1.8f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
            // Core
            drawPath(
                path = path,
                color = Color.White,
                style = Stroke(
                    width = stroke.strokeWidth * 0.6f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
        BrushType.BUBBLE -> {
            stroke.points.forEachIndexed { idx, pt ->
                if (idx % 3 == 0) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(Color.White.copy(alpha = 0.8f), stroke.color.copy(alpha = 0.5f)),
                            center = pt,
                            radius = stroke.strokeWidth
                        ),
                        radius = stroke.strokeWidth * 0.8f,
                        center = pt
                    )
                }
            }
        }
        BrushType.STAR_TRAIL -> {
            stroke.points.forEachIndexed { idx, pt ->
                if (idx % 4 == 0) {
                    drawCircle(
                        color = VibrantAmber,
                        radius = stroke.strokeWidth * 0.5f,
                        center = pt
                    )
                }
            }
        }
        BrushType.HEART_STAMP -> {
            // Covered by stamp renderer
        }
    }
}

@Composable
fun CircleToolButton(
    icon: String,
    label: String,
    onClick: () -> Unit,
    backgroundColor: Color
) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .border(2.5.dp, Color.White, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = icon, fontSize = 18.sp)
    }
}
