package com.example.ui.games

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.SoundEngine
import com.example.model.StickerReward
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StickerAlbumScreen(
    soundEngine: SoundEngine,
    stars: Int,
    stickers: List<StickerReward>,
    onBack: () -> Unit,
    isSoundOn: Boolean,
    isMusicOn: Boolean,
    onToggleSound: () -> Unit,
    onToggleMusic: () -> Unit
) {
    var selectedSticker by remember { mutableStateOf<StickerReward?>(null) }
    var showConfetti by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    val unlockedCount = stickers.count { it.isUnlocked }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        VibrantAmber,
                        Color(0xFFFEF3C7),
                        Color(0xFFF0FDF4)
                    )
                )
            )
            .testTag("sticker_album_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            KidTopBar(
                title = "Sticker Stars",
                emoji = "🏆",
                stars = stars,
                soundEngine = soundEngine,
                onBack = onBack,
                isSoundOn = isSoundOn,
                isMusicOn = isMusicOn,
                onToggleSound = onToggleSound,
                onToggleMusic = onToggleMusic,
                subtitle = "Your Trophies"
            )

            // Header Banner with Trophy image and progress
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(VibrantBevelDark)
                    .padding(bottom = 5.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        Brush.horizontalGradient(
                            listOf(VibrantAmber, VibrantCoral, VibrantRose)
                        )
                    )
                    .border(3.dp, Color.White, RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "⭐ Your Sticker Collection",
                            fontWeight = FontWeight.Black,
                            fontSize = 17.sp,
                            color = Color(0xFF3E2723)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Unlocked: $unlockedCount / ${stickers.size} Shiny Stickers!",
                            fontWeight = FontWeight.Black,
                            fontSize = 13.sp,
                            color = VibrantWhite
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "👉 Play games to earn stars & unlock more!",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = Color(0xFFFEF08A)
                        )
                    }

                    // Trophy Icon / Image
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(VibrantWhite)
                            .border(3.dp, VibrantAmber, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img_sticker_trophy),
                            contentDescription = "Trophy Star",
                            modifier = Modifier.size(64.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Sticker Gallery Grid
            Text(
                text = "✨ TAP UNLOCKED STICKERS TO PLAY SOUNDS! 👇",
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                letterSpacing = 1.2.sp,
                color = VibrantPurple,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 4.dp)
            )

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.Center,
                verticalArrangement = Arrangement.spacedBy(10.dp),
                maxItemsInEachRow = 3
            ) {
                stickers.forEach { sticker ->
                    StickerCardView(
                        sticker = sticker,
                        onClick = {
                            if (sticker.isUnlocked) {
                                soundEngine.playSparkle()
                                soundEngine.playBoing()
                                selectedSticker = sticker
                                showConfetti = true
                            } else {
                                soundEngine.playGiggle()
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Celebration Confetti
        ConfettiCelebration(
            isActive = showConfetti,
            onFinished = { showConfetti = false }
        )
    }
}

@Composable
fun StickerCardView(
    sticker: StickerReward,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.12f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 700f),
        label = "sticker_bounce"
    )

    val shadowBevel = if (isPressed) 2.dp else 4.dp

    Box(
        modifier = Modifier
            .padding(4.dp)
            .scale(scale)
            .width(104.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(if (sticker.isUnlocked) VibrantAmberShadow else VibrantBevelDark)
            .padding(bottom = shadowBevel)
            .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 18.dp, bottomEnd = 18.dp))
            .background(if (sticker.isUnlocked) VibrantWhite else Color(0xFFF1F5F9))
            .border(
                width = if (sticker.isUnlocked) 2.5.dp else 1.5.dp,
                color = if (sticker.isUnlocked) VibrantAmber else Color(0xFFCBD5E1),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp, bottomStart = 18.dp, bottomEnd = 18.dp)
            )
            .pointerInput(sticker.isUnlocked) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
            .padding(8.dp)
            .testTag("sticker_${sticker.id}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(if (sticker.isUnlocked) Color(0xFFFEF9C3) else Color(0xFFE2E8F0)),
                contentAlignment = Alignment.Center
            ) {
                if (sticker.isUnlocked) {
                    Text(text = sticker.emoji, fontSize = 32.sp)
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Locked",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "${sticker.starsRequired} ⭐",
                            fontWeight = FontWeight.Black,
                            fontSize = 10.sp,
                            color = VibrantCoral
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = sticker.title,
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                color = if (sticker.isUnlocked) VibrantSky900 else Color(0xFF64748B),
                textAlign = TextAlign.Center
            )
        }
    }
}
