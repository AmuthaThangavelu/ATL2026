package com.example.ui.components

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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.MusicOff
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VolumeMute
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoundEngine
import com.example.ui.theme.VibrantAmber
import com.example.ui.theme.VibrantAmberLight
import com.example.ui.theme.VibrantAmberShadow
import com.example.ui.theme.VibrantBevelDark
import com.example.ui.theme.VibrantBevelShadow
import com.example.ui.theme.VibrantCyan
import com.example.ui.theme.VibrantLime
import com.example.ui.theme.VibrantLimeShadow
import com.example.ui.theme.VibrantPink
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantRose
import com.example.ui.theme.VibrantRoseShadow
import com.example.ui.theme.VibrantSky400
import com.example.ui.theme.VibrantSky700
import com.example.ui.theme.VibrantSky900
import com.example.ui.theme.VibrantWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Vibrant 3D Chunky Gummy Button designed for kids.
 * Features 3D bevel shadow, top highlight border, tactile press animation, and joyful sound trigger.
 */
@Composable
fun BouncyButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = VibrantAmber,
    shadowColor: Color = VibrantAmberShadow,
    highlightColor: Color = VibrantAmberLight,
    contentColor: Color = Color(0xFF3E2723),
    soundEngine: SoundEngine? = null,
    testTag: String = "bouncy_button",
    content: @Composable () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 600f),
        label = "button_bounce"
    )

    val bevelHeight = if (isPressed) 2.dp else 6.dp

    Box(
        modifier = modifier
            .scale(scale)
            .clip(RoundedCornerShape(30.dp))
            .background(shadowColor)
            .padding(bottom = bevelHeight)
            .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp, bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(backgroundColor)
            .border(2.dp, highlightColor.copy(alpha = 0.8f), RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp, bottomStart = 24.dp, bottomEnd = 24.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        soundEngine?.playBoing()
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
            .padding(horizontal = 22.dp, vertical = 12.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

/**
 * Top bar customized for kids with Vibrant Palette design (white 3D pill cards, crisp borders, sky-900 typography).
 */
@Composable
fun KidTopBar(
    title: String,
    emoji: String,
    stars: Int,
    soundEngine: SoundEngine,
    onBack: (() -> Unit)? = null,
    isSoundOn: Boolean,
    isMusicOn: Boolean,
    onToggleSound: () -> Unit,
    onToggleMusic: () -> Unit,
    subtitle: String = "WonderPlay Land",
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "star_pulse")
    val starScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "star_pulse_scale"
    )

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Home / Back 3D pill button & Title
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (onBack != null) {
                Gummy3DIconButton(
                    onClick = onBack,
                    icon = {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go Back",
                            tint = VibrantSky900,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    backgroundColor = VibrantWhite,
                    shadowColor = VibrantBevelDark,
                    soundEngine = soundEngine,
                    size = 46.dp,
                    testTag = "back_button"
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VibrantBevelDark)
                        .padding(bottom = 3.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(VibrantWhite)
                        .border(2.dp, Color.White, RoundedCornerShape(14.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🏠", fontSize = 22.sp)
                }
            }

            // Title with uppercase category subtitle
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = subtitle.uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 10.sp,
                    letterSpacing = 1.2.sp,
                    color = VibrantSky700.copy(alpha = 0.75f)
                )
                Text(
                    text = "$emoji $title",
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    color = VibrantSky900
                )
            }
        }

        // Right Controls: Stars pill + Music/Sound toggles
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Star Counter 3D Pill
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(18.dp))
                    .background(VibrantBevelDark)
                    .padding(bottom = 3.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(VibrantWhite)
                    .border(2.dp, Color.White, RoundedCornerShape(16.dp))
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "⭐",
                    fontSize = 18.sp,
                    modifier = Modifier.scale(starScale)
                )
                Text(
                    text = "$stars",
                    fontWeight = FontWeight.Black,
                    fontSize = 16.sp,
                    color = VibrantSky900
                )
            }

            // Music Toggle
            Gummy3DIconButton(
                onClick = onToggleMusic,
                icon = {
                    Icon(
                        imageVector = if (isMusicOn) Icons.Default.MusicNote else Icons.Default.MusicOff,
                        contentDescription = "Toggle Nursery Music",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                backgroundColor = if (isMusicOn) VibrantLime else Color(0xFF94A3B8),
                shadowColor = if (isMusicOn) VibrantLimeShadow else Color(0xFF64748B),
                soundEngine = soundEngine,
                size = 40.dp,
                testTag = "toggle_music_button"
            )

            // Sound Effects Toggle
            Gummy3DIconButton(
                onClick = onToggleSound,
                icon = {
                    Icon(
                        imageVector = if (isSoundOn) Icons.Default.VolumeUp else Icons.Default.VolumeMute,
                        contentDescription = "Toggle Sound Effects",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                },
                backgroundColor = if (isSoundOn) VibrantSky400 else Color(0xFF94A3B8),
                shadowColor = if (isSoundOn) Color(0xFF0284C7) else Color(0xFF64748B),
                soundEngine = soundEngine,
                size = 40.dp,
                testTag = "toggle_sound_button"
            )
        }
    }
}

/**
 * 3D Beveled Icon Button matching Vibrant Palette aesthetic.
 */
@Composable
fun Gummy3DIconButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    backgroundColor: Color,
    shadowColor: Color,
    soundEngine: SoundEngine?,
    modifier: Modifier = Modifier,
    size: Dp = 46.dp,
    testTag: String = "gummy_icon_button"
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.88f else 1f,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 700f),
        label = "icon_btn_bounce"
    )

    val bevel = if (isPressed) 1.dp else 4.dp

    Box(
        modifier = modifier
            .size(size)
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .background(shadowColor)
            .padding(bottom = bevel)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp, bottomStart = 12.dp, bottomEnd = 12.dp))
            .background(backgroundColor)
            .border(2.dp, Color.White.copy(alpha = 0.8f), RoundedCornerShape(14.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        soundEngine?.playBoing()
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

@Composable
fun CircleIconButton(
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    backgroundColor: Color,
    soundEngine: SoundEngine?,
    modifier: Modifier = Modifier,
    size: Dp = 48.dp,
    testTag: String = "circle_icon_button"
) {
    Gummy3DIconButton(
        onClick = onClick,
        icon = icon,
        backgroundColor = backgroundColor,
        shadowColor = backgroundColor.copy(alpha = 0.7f),
        soundEngine = soundEngine,
        modifier = modifier,
        size = size,
        testTag = testTag
    )
}

/**
 * Confetti particle explosion animation when kids accomplish something awesome.
 */
data class ConfettiPiece(
    val x: Float,
    val y: Float,
    val size: Float,
    val color: Color,
    val rotation: Float,
    val speedY: Float,
    val speedX: Float
)

@Composable
fun ConfettiCelebration(
    isActive: Boolean,
    onFinished: () -> Unit = {}
) {
    if (!isActive) return

    val confettiList = remember {
        val colors = listOf(
            Color(0xFFFF5252),
            Color(0xFFFFD600),
            Color(0xFF00E676),
            Color(0xFF40C4FF),
            Color(0xFF7C4DFF),
            Color(0xFFFF4081)
        )
        List(70) {
            ConfettiPiece(
                x = Random.nextFloat(),
                y = Random.nextFloat() * -0.4f,
                size = Random.nextFloat() * 14f + 10f,
                color = colors.random(),
                rotation = Random.nextFloat() * 360f,
                speedY = Random.nextFloat() * 0.008f + 0.006f,
                speedX = (Random.nextFloat() - 0.5f) * 0.004f
            )
        }
    }

    var tick by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(isActive) {
        val startTime = System.currentTimeMillis()
        while (System.currentTimeMillis() - startTime < 3500) {
            tick += 0.016f
            delay(16)
        }
        onFinished()
    }

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer { alpha = 0.95f }
    ) {
        val w = size.width
        val h = size.height

        for (p in confettiList) {
            val curY = ((p.y + p.speedY * tick * 60f) % 1.2f) * h
            val curX = (p.x + p.speedX * tick * 60f + kotlin.math.sin(tick * 5f + p.x * 10f) * 0.02f) * w
            val curRot = p.rotation + tick * 180f

            drawCircle(
                color = p.color,
                radius = p.size / 2,
                center = Offset(curX, curY)
            )
        }
    }
}
