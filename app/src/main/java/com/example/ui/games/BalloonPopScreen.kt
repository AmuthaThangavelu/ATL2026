package com.example.ui.games

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoundEngine
import com.example.model.BalloonItem
import com.example.model.PoppedParticle
import com.example.ui.components.BouncyButton
import com.example.ui.components.ConfettiCelebration
import com.example.ui.components.KidTopBar
import com.example.ui.theme.VibrantAmber
import com.example.ui.theme.VibrantAmberLight
import com.example.ui.theme.VibrantAmberShadow
import com.example.ui.theme.VibrantBevelDark
import com.example.ui.theme.VibrantBevelShadow
import com.example.ui.theme.VibrantCyan
import com.example.ui.theme.VibrantLime
import com.example.ui.theme.VibrantPink
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantRose
import com.example.ui.theme.VibrantSky400
import com.example.ui.theme.VibrantSky700
import com.example.ui.theme.VibrantSky900
import com.example.ui.theme.VibrantWhite
import kotlinx.coroutines.delay
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun BalloonPopScreen(
    soundEngine: SoundEngine,
    stars: Int,
    onAddStars: (Int) -> Unit,
    onBack: () -> Unit,
    isSoundOn: Boolean,
    isMusicOn: Boolean,
    onToggleSound: () -> Unit,
    onToggleMusic: () -> Unit
) {
    val balloons = remember { mutableStateListOf<BalloonItem>() }
    val particles = remember { mutableStateListOf<PoppedParticle>() }
    var score by remember { mutableIntStateOf(0) }
    var poppedCount by remember { mutableIntStateOf(0) }
    var showCelebration by remember { mutableStateOf(false) }

    // Mission target color
    var targetColor by remember { mutableStateOf(VibrantRose) }
    var targetColorName by remember { mutableStateOf("Rose") }
    var targetColorEmoji by remember { mutableStateOf("🔴") }
    var missionProgress by remember { mutableIntStateOf(0) }
    val missionGoal = 5

    val balloonColors = listOf(
        Pair(VibrantRose, "Rose" to "🔴"),
        Pair(VibrantAmber, "Amber" to "🟡"),
        Pair(VibrantLime, "Lime" to "🟢"),
        Pair(VibrantSky400, "Sky" to "🔵"),
        Pair(VibrantPink, "Pink" to "🌸"),
        Pair(VibrantPurple, "Purple" to "🟣")
    )

    fun resetMission() {
        val picked = balloonColors.random()
        targetColor = picked.first
        targetColorName = picked.second.first
        targetColorEmoji = picked.second.second
        missionProgress = 0
    }

    // Balloon physics loop
    LaunchedEffect(Unit) {
        var idCounter = 1L
        while (true) {
            // Spawn new balloons if fewer than 10
            if (balloons.size < 9) {
                val colorPair = balloonColors.random()
                val isSpecial = Random.nextFloat() < 0.25f
                val emoji = if (isSpecial) {
                    listOf("⭐", "🦄", "🌈", "🐱", "🐶", "🎵").random()
                } else {
                    listOf("🎈", "✨", "❤️", "🐥", "🌟").random()
                }

                balloons.add(
                    BalloonItem(
                        id = idCounter++,
                        xFraction = Random.nextFloat() * 0.78f + 0.08f,
                        yFraction = 1.15f,
                        sizeDp = if (isSpecial) Random.nextFloat() * 20f + 85f else Random.nextFloat() * 15f + 70f,
                        color = colorPair.first,
                        emoji = emoji,
                        isSpecial = isSpecial,
                        speed = Random.nextFloat() * 0.0035f + 0.0025f
                    )
                )
            }

            // Move balloons upward with gentle sway
            val iterator = balloons.listIterator()
            while (iterator.hasNext()) {
                val b = iterator.next()
                val nextY = b.yFraction - b.speed
                if (nextY < -0.2f || b.isPopped) {
                    iterator.remove()
                } else {
                    iterator.set(b.copy(yFraction = nextY))
                }
            }

            // Update particles
            val pIterator = particles.listIterator()
            while (pIterator.hasNext()) {
                val p = pIterator.next()
                val nextAlpha = p.alpha - 0.04f
                if (nextAlpha <= 0f) {
                    pIterator.remove()
                } else {
                    pIterator.set(
                        p.copy(
                            x = p.x + p.vx,
                            y = p.y + p.vy + 0.002f, // gravity
                            alpha = nextAlpha
                        )
                    )
                }
            }

            delay(24)
        }
    }

    fun popBalloon(balloon: BalloonItem) {
        if (balloon.isPopped) return

        // Audio feedback
        soundEngine.playPop()
        if (balloon.isSpecial) {
            soundEngine.playSparkle()
        }

        // Add particles
        val numParticles = if (balloon.isSpecial) 12 else 8
        for (i in 0 until numParticles) {
            val angle = Random.nextFloat() * 6.28f
            val speed = Random.nextFloat() * 0.02f + 0.01f
            particles.add(
                PoppedParticle(
                    id = System.currentTimeMillis() + i,
                    x = balloon.xFraction,
                    y = balloon.yFraction,
                    color = balloon.color,
                    vx = (kotlin.math.cos(angle) * speed).toFloat(),
                    vy = (kotlin.math.sin(angle) * speed).toFloat(),
                    size = Random.nextFloat() * 14f + 10f,
                    text = if (i == 0) (if (balloon.isSpecial) "+30 ⭐" else "+10") else null
                )
            )
        }

        val points = if (balloon.isSpecial) 30 else 10
        val starsEarned = if (balloon.isSpecial) 3 else 1
        score += points
        poppedCount++
        onAddStars(starsEarned)

        // Mission tracking
        if (balloon.color == targetColor) {
            missionProgress++
            if (missionProgress >= missionGoal) {
                soundEngine.playVictory()
                showCelebration = true
                onAddStars(10)
                resetMission()
            }
        }

        // Mark popped
        val index = balloons.indexOfFirst { it.id == balloon.id }
        if (index != -1) {
            balloons.removeAt(index)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        VibrantCyan,
                        Color(0xFFBAE6FD),
                        Color(0xFFE0F2FE)
                    )
                )
            )
            .testTag("balloon_pop_screen")
    ) {
        // Background Decorative Clouds
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            // Cloud 1
            drawCircle(Color.White.copy(alpha = 0.5f), radius = 60.dp.toPx(), center = Offset(w * 0.15f, h * 0.15f))
            drawCircle(Color.White.copy(alpha = 0.5f), radius = 80.dp.toPx(), center = Offset(w * 0.28f, h * 0.16f))
            // Cloud 2
            drawCircle(Color.White.copy(alpha = 0.45f), radius = 70.dp.toPx(), center = Offset(w * 0.82f, h * 0.32f))
            drawCircle(Color.White.copy(alpha = 0.45f), radius = 90.dp.toPx(), center = Offset(w * 0.70f, h * 0.34f))
        }

        Column(modifier = Modifier.fillMaxSize()) {
            KidTopBar(
                title = "Balloon Pop",
                emoji = "🎈",
                stars = stars,
                soundEngine = soundEngine,
                onBack = onBack,
                isSoundOn = isSoundOn,
                isMusicOn = isMusicOn,
                onToggleSound = onToggleSound,
                onToggleMusic = onToggleMusic,
                subtitle = "Balloon Carnival"
            )

            // Mission & Score Banner (3D Gummy Pills)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Mission Goal Pill
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(VibrantBevelDark)
                        .padding(bottom = 3.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(VibrantWhite)
                        .border(2.dp, targetColor, RoundedCornerShape(18.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = "Pop $targetColorEmoji $targetColorName:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = VibrantSky900
                    )
                    Text(
                        text = "$missionProgress/$missionGoal",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = targetColor
                    )
                }

                // Popped Count Badge
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(VibrantBevelDark)
                        .padding(bottom = 3.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(VibrantWhite)
                        .border(2.dp, VibrantPurple, RoundedCornerShape(18.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "🎈 Popped:",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = VibrantSky900
                    )
                    Text(
                        text = "$poppedCount",
                        fontWeight = FontWeight.Black,
                        fontSize = 15.sp,
                        color = VibrantPurple
                    )
                }
            }

            // Interactive Balloon Playing Canvas
            BoxWithConstraints(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                val canvasWidth = maxWidth
                val canvasHeight = maxHeight

                // Render Balloons
                balloons.forEach { balloon ->
                    val sway = (sin(balloon.yFraction * 10f) * 16).dp
                    val xOffset = canvasWidth * balloon.xFraction + sway
                    val yOffset = canvasHeight * balloon.yFraction

                    SingleBalloonView(
                        balloon = balloon,
                        modifier = Modifier
                            .offset {
                                IntOffset(
                                    xOffset.roundToPx(),
                                    yOffset.roundToPx()
                                )
                            }
                            .size(balloon.sizeDp.dp),
                        onPop = { popBalloon(balloon) }
                    )
                }

                // Render Explosion Particles
                particles.forEach { p ->
                    val px = canvasWidth * p.x
                    val py = canvasHeight * p.y

                    if (p.text != null) {
                        Text(
                            text = p.text,
                            fontWeight = FontWeight.Black,
                            fontSize = 22.sp,
                            color = VibrantAmber,
                            modifier = Modifier
                                .offset { IntOffset(px.roundToPx(), py.roundToPx()) }
                                .graphicsLayer { alpha = p.alpha }
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .offset { IntOffset(px.roundToPx(), py.roundToPx()) }
                                .size(p.size.dp)
                                .graphicsLayer { alpha = p.alpha }
                                .clip(CircleShape)
                                .background(p.color)
                        )
                    }
                }
            }

            // Bottom action hint / helper
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(VibrantBevelDark)
                        .padding(bottom = 3.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(VibrantWhite)
                        .border(2.dp, Color.White, RoundedCornerShape(18.dp))
                        .padding(horizontal = 18.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "👉 Tap or Swipe to Pop Balloons! 🎈✨",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = VibrantSky900
                    )
                }
            }
        }

        // Celebration Confetti
        ConfettiCelebration(
            isActive = showCelebration,
            onFinished = { showCelebration = false }
        )
    }
}

@Composable
fun SingleBalloonView(
    balloon: BalloonItem,
    modifier: Modifier = Modifier,
    onPop: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.25f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 800f),
        label = "balloon_press"
    )

    Box(
        modifier = modifier
            .scale(scale)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        onPop()
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        // Balloon Body with Gloss Highlight & String
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            val balloonRadiusX = w * 0.45f
            val balloonRadiusY = h * 0.48f

            // Balloon String
            val stringPath = Path().apply {
                moveTo(w / 2, h * 0.88f)
                quadraticTo(w / 2 + 10f, h * 0.95f, w / 2 - 5f, h * 1.15f)
            }
            drawPath(
                path = stringPath,
                color = Color.White.copy(alpha = 0.8f),
                style = Stroke(width = 4f)
            )

            // Balloon Knot
            val knotPath = Path().apply {
                moveTo(w / 2 - 8f, h * 0.88f)
                lineTo(w / 2 + 8f, h * 0.88f)
                lineTo(w / 2, h * 0.92f)
                close()
            }
            drawPath(knotPath, balloon.color)

            // Balloon Oval Body
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(
                        balloon.color.copy(alpha = 0.95f),
                        balloon.color
                    ),
                    center = Offset(w * 0.4f, h * 0.35f),
                    radius = balloonRadiusX * 1.4f
                ),
                topLeft = Offset(w * 0.05f, h * 0.02f),
                size = androidx.compose.ui.geometry.Size(balloonRadiusX * 2, balloonRadiusY * 2)
            )

            // Glossy Shine / Highlight
            drawOval(
                color = Color.White.copy(alpha = 0.55f),
                topLeft = Offset(w * 0.22f, h * 0.12f),
                size = androidx.compose.ui.geometry.Size(w * 0.22f, h * 0.14f)
            )
        }

        // Emoji / Symbol inside Balloon
        Text(
            text = balloon.emoji,
            fontSize = (balloon.sizeDp * 0.32f).sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }
}
