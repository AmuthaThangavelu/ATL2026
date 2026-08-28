package com.example.ui.games

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.material.icons.filled.CheckCircle
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoundEngine
import com.example.model.KidShape
import com.example.model.ShapeCard
import com.example.ui.components.BouncyButton
import com.example.ui.components.ConfettiCelebration
import com.example.ui.components.KidTopBar
import com.example.ui.theme.VibrantAmber
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
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ShapeSorterScreen(
    soundEngine: SoundEngine,
    stars: Int,
    onAddStars: (Int) -> Unit,
    onBack: () -> Unit,
    isSoundOn: Boolean,
    isMusicOn: Boolean,
    onToggleSound: () -> Unit,
    onToggleMusic: () -> Unit
) {
    val allShapes = remember { KidShape.values().toList() }

    var currentRound by remember { mutableIntStateOf(1) }
    var activeShapesInRound by remember { mutableStateOf(listOf<KidShape>()) }
    val shapesToMatch = remember { mutableStateListOf<ShapeCard>() }
    var selectedShapeCard by remember { mutableStateOf<ShapeCard?>(null) }
    var showCelebration by remember { mutableStateOf(false) }
    var feedbackMessage by remember { mutableStateOf("Match the smiling shapes into their baskets! ⭐") }

    val coroutineScope = rememberCoroutineScope()

    fun startNewRound() {
        val count = (currentRound + 2).coerceAtMost(5)
        val chosenShapes = allShapes.shuffled().take(count)
        activeShapesInRound = chosenShapes

        shapesToMatch.clear()
        val cards = chosenShapes.shuffled().mapIndexed { idx, shape ->
            ShapeCard(id = System.currentTimeMillis() + idx, shape = shape, isMatched = false)
        }
        shapesToMatch.addAll(cards)
        selectedShapeCard = null
        feedbackMessage = "Tap a shape, then tap its matching basket! 🎯"
    }

    LaunchedEffect(currentRound) {
        startNewRound()
    }

    fun handleMatchAttempt(card: ShapeCard, targetShape: KidShape) {
        if (card.shape == targetShape) {
            // Correct match!
            soundEngine.playBoing()
            soundEngine.playSparkle()

            val index = shapesToMatch.indexOfFirst { it.id == card.id }
            if (index != -1) {
                shapesToMatch[index] = card.copy(isMatched = true)
            }
            selectedShapeCard = null
            onAddStars(2)
            feedbackMessage = "Yay! Super Match! ${card.shape.emoji} ${card.shape.displayName} 🎉"

            // Check if all matched
            if (shapesToMatch.all { it.isMatched }) {
                coroutineScope.launch {
                    soundEngine.playVictory()
                    showCelebration = true
                    onAddStars(6)
                    feedbackMessage = "🌟 Wonderful Job! Round Complete! 🌟"
                    delay(2500)
                    currentRound++
                }
            }
        } else {
            // Wrong shape
            soundEngine.playGiggle()
            feedbackMessage = "Oops! Try another basket! 😊"
            selectedShapeCard = null
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
            .testTag("shape_sorter_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            KidTopBar(
                title = "Shape Sorter",
                emoji = "⭐",
                stars = stars,
                soundEngine = soundEngine,
                onBack = onBack,
                isSoundOn = isSoundOn,
                isMusicOn = isMusicOn,
                onToggleSound = onToggleSound,
                onToggleMusic = onToggleMusic,
                subtitle = "Shape Matching Fun"
            )

            // Header round indicator & cheerful banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(VibrantBevelDark)
                        .padding(bottom = 3.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(VibrantWhite)
                        .border(2.dp, VibrantPurple, RoundedCornerShape(18.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "🏆 Level $currentRound",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = VibrantPurple
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(VibrantWhite)
                        .border(1.5.dp, VibrantBevelShadow, RoundedCornerShape(16.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = feedbackMessage,
                        fontWeight = FontWeight.Black,
                        fontSize = 12.sp,
                        color = VibrantSky900
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Upper Section: Floating Shapes to Match
            Text(
                text = "1. PICK A SHAPE 👇",
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                letterSpacing = 1.2.sp,
                color = VibrantSky700,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 4
            ) {
                shapesToMatch.forEach { card ->
                    val isSelected = selectedShapeCard?.id == card.id
                    val isMatched = card.isMatched

                    ShapeCardItem(
                        card = card,
                        isSelected = isSelected,
                        isMatched = isMatched,
                        onClick = {
                            if (!isMatched) {
                                soundEngine.playBoing()
                                selectedShapeCard = if (isSelected) null else card
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // Lower Section: Shape Monster Baskets
            Text(
                text = "2. DROP INTO MATCHING BASKET 👇",
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                letterSpacing = 1.2.sp,
                color = VibrantRose,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                maxItemsInEachRow = 3
            ) {
                activeShapesInRound.forEach { basketShape ->
                    ShapeBasketView(
                        shape = basketShape,
                        hasSelectedCard = selectedShapeCard != null,
                        onClick = {
                            selectedShapeCard?.let { card ->
                                handleMatchAttempt(card, basketShape)
                            } ?: run {
                                soundEngine.playGiggle()
                                feedbackMessage = "Pick a shape from above first! 👆"
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // Bottom reload/reset button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                BouncyButton(
                    onClick = {
                        soundEngine.playBoing()
                        startNewRound()
                    },
                    backgroundColor = VibrantAmber,
                    shadowColor = VibrantAmberShadow,
                    soundEngine = soundEngine,
                    testTag = "refresh_round_button"
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "New Round",
                            tint = VibrantSky900
                        )
                        Text(
                            text = "New Shapes 🔄",
                            fontWeight = FontWeight.Black,
                            fontSize = 15.sp,
                            color = VibrantSky900
                        )
                    }
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
fun ShapeCardItem(
    card: ShapeCard,
    isSelected: Boolean,
    isMatched: Boolean,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = when {
            isMatched -> 0.8f
            isSelected -> 1.15f
            else -> 1f
        },
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 600f),
        label = "shape_card_scale"
    )

    val shadowBevel = if (isSelected) 2.dp else 4.dp

    Box(
        modifier = Modifier
            .padding(4.dp)
            .scale(scale)
            .size(76.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (isSelected) VibrantAmberShadow else card.shape.color.copy(alpha = 0.6f))
            .padding(bottom = shadowBevel)
            .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(if (isMatched) Color(0xFFF1F5F9) else VibrantWhite)
            .border(
                width = if (isSelected) 3.5.dp else 2.dp,
                color = if (isSelected) VibrantAmber else (if (isMatched) Color(0xFFCBD5E1) else card.shape.color),
                shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
            )
            .clickable { onClick() }
            .testTag("shape_card_${card.shape.name}"),
        contentAlignment = Alignment.Center
    ) {
        if (isMatched) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Matched",
                tint = VibrantLime,
                modifier = Modifier.size(36.dp)
            )
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = card.shape.emoji, fontSize = 30.sp)
                Text(
                    text = card.shape.displayName,
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    color = card.shape.color
                )
            }
        }
    }
}

@Composable
fun ShapeBasketView(
    shape: KidShape,
    hasSelectedCard: Boolean,
    onClick: () -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "basket_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "basket_pulse_scale"
    )

    Box(
        modifier = Modifier
            .padding(4.dp)
            .scale(if (hasSelectedCard) pulseScale else 1f)
            .size(width = 96.dp, height = 88.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(shape.color.copy(alpha = 0.65f))
            .padding(bottom = 4.dp)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 18.dp, bottomEnd = 18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        VibrantWhite,
                        shape.color.copy(alpha = 0.25f)
                    )
                )
            )
            .border(2.5.dp, shape.color, RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 18.dp, bottomEnd = 18.dp))
            .clickable { onClick() }
            .testTag("shape_basket_${shape.name}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = shape.emoji, fontSize = 28.sp)
            Text(
                text = "${shape.displayName} Jar",
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                color = shape.color
            )
        }
    }
}
