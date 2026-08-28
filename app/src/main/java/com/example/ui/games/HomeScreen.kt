package com.example.ui.games

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.SoundEngine
import com.example.model.GameType
import com.example.ui.components.BouncyButton
import com.example.ui.components.KidTopBar
import com.example.ui.theme.VibrantAmber
import com.example.ui.theme.VibrantAmberLight
import com.example.ui.theme.VibrantAmberShadow
import com.example.ui.theme.VibrantBevelDark
import com.example.ui.theme.VibrantBevelShadow
import com.example.ui.theme.VibrantCyan
import com.example.ui.theme.VibrantLime
import com.example.ui.theme.VibrantLimeShadow
import com.example.ui.theme.VibrantMainSkyGradient
import com.example.ui.theme.VibrantPink
import com.example.ui.theme.VibrantPurple
import com.example.ui.theme.VibrantRose
import com.example.ui.theme.VibrantRoseShadow
import com.example.ui.theme.VibrantSky300
import com.example.ui.theme.VibrantSky400
import com.example.ui.theme.VibrantSky700
import com.example.ui.theme.VibrantSky900
import com.example.ui.theme.VibrantWhite

@Composable
fun HomeScreen(
    soundEngine: SoundEngine,
    stars: Int,
    onSelectGame: (GameType) -> Unit,
    isSoundOn: Boolean,
    isMusicOn: Boolean,
    onToggleSound: () -> Unit,
    onToggleMusic: () -> Unit
) {
    val games = remember { GameType.values().toList() }

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
            .testTag("home_screen")
    ) {
        // Decorative background cloud capsules
        Box(
            modifier = Modifier
                .offset(x = (-20).dp, y = 40.dp)
                .size(width = 110.dp, height = 44.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(Color.White.copy(alpha = 0.45f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .offset(x = 20.dp, y = 140.dp)
                .size(width = 130.dp, height = 50.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(Color.White.copy(alpha = 0.4f))
        )

        Column(modifier = Modifier.fillMaxSize()) {
            KidTopBar(
                title = "WonderPlay",
                emoji = "🌈",
                stars = stars,
                soundEngine = soundEngine,
                onBack = null,
                isSoundOn = isSoundOn,
                isMusicOn = isMusicOn,
                onToggleSound = onToggleSound,
                onToggleMusic = onToggleMusic,
                subtitle = "Joyful Kids Adventure"
            )

            // Cheerful Hero Banner with Vibrant Sky Gradient & 3D Artwork
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
                    .height(136.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(VibrantBevelDark)
                    .padding(bottom = 5.dp)
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 26.dp, bottomEnd = 26.dp))
                    .background(VibrantMainSkyGradient)
                    .border(3.dp, Color.White, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp, bottomStart = 26.dp, bottomEnd = 26.dp))
            ) {
                // Hero Image
                Image(
                    painter = painterResource(id = R.drawable.img_kids_hero),
                    contentDescription = "Kids Playground Banner",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // High-contrast vibrant overlay
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(
                                    VibrantSky900.copy(alpha = 0.75f),
                                    Color.Transparent
                                )
                            )
                        )
                        .padding(horizontal = 18.dp, vertical = 14.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Column {
                        Text(
                            text = "✨ WONDERPLAY LAND",
                            fontWeight = FontWeight.Black,
                            fontSize = 11.sp,
                            letterSpacing = 1.2.sp,
                            color = VibrantAmber
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Tap to Play & Learn!",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "Super fun games, sounds & stars! 🎈🎵",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Color(0xFFE0F2FE)
                        )
                    }
                }
            }

            // Section Subtitle & Heading
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "FUN ACTIVITIES",
                        fontWeight = FontWeight.Black,
                        fontSize = 10.sp,
                        letterSpacing = 1.5.sp,
                        color = VibrantSky700.copy(alpha = 0.8f)
                    )
                    Text(
                        text = "🎮 Choose Your Game",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = VibrantSky900
                    )
                }

                // Mini Category badge
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(14.dp))
                        .background(VibrantWhite)
                        .border(1.5.dp, VibrantBevelShadow, RoundedCornerShape(14.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "6 Games ⭐",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        color = VibrantRose
                    )
                }
            }

            // Grid of 6 Engaging Kids Games with Chunky 3D Gummy Cards
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                items(games) { game ->
                    KidGameCard(
                        game = game,
                        soundEngine = soundEngine,
                        onClick = {
                            soundEngine.playBoing()
                            onSelectGame(game)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun KidGameCard(
    game: GameType,
    soundEngine: SoundEngine,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.45f, stiffness = 600f),
        label = "card_bounce"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "emoji_wobble")
    val wobbleAngle by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "wobble_angle"
    )

    val shadowBevel = if (isPressed) 2.dp else 5.dp

    Box(
        modifier = Modifier
            .scale(scale)
            .fillMaxWidth()
            .height(144.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(game.primaryColor.copy(alpha = 0.7f))
            .padding(bottom = shadowBevel)
            .clip(RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 22.dp, bottomEnd = 22.dp))
            .background(VibrantWhite)
            .border(2.5.dp, game.primaryColor.copy(alpha = 0.5f), RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp, bottomStart = 22.dp, bottomEnd = 22.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onClick()
                    }
                )
            }
            .padding(10.dp)
            .testTag("game_card_${game.name.lowercase()}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Big Bouncy Emoji in 3D Circle
            Box(
                modifier = Modifier
                    .size(54.dp)
                    .rotate(wobbleAngle)
                    .clip(CircleShape)
                    .background(game.primaryColor.copy(alpha = 0.18f))
                    .border(2.5.dp, game.primaryColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = game.emoji,
                    fontSize = 30.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Game Title in Bold Sky900
            Text(
                text = game.title,
                fontWeight = FontWeight.Black,
                fontSize = 15.sp,
                color = VibrantSky900
            )

            // Subtitle in Primary Game Color
            Text(
                text = game.subtitle,
                fontWeight = FontWeight.Bold,
                fontSize = 10.sp,
                color = game.primaryColor
            )
        }
    }
}
