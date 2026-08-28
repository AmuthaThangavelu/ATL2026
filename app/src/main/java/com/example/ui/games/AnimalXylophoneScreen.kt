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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Celebration
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.PlayArrow
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
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.SoundEngine
import com.example.model.MusicalKey
import com.example.model.NurserySong
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
import com.example.ui.theme.VibrantSky300
import com.example.ui.theme.VibrantSky400
import com.example.ui.theme.VibrantSky700
import com.example.ui.theme.VibrantSky900
import com.example.ui.theme.VibrantWhite
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AnimalXylophoneScreen(
    soundEngine: SoundEngine,
    stars: Int,
    onAddStars: (Int) -> Unit,
    onBack: () -> Unit,
    isSoundOn: Boolean,
    isMusicOn: Boolean,
    onToggleSound: () -> Unit,
    onToggleMusic: () -> Unit
) {
    val musicalKeys = remember {
        listOf(
            MusicalKey(0, "C", "Do", 261.63f, VibrantRose, "🦁", "Lion", "Roar"),
            MusicalKey(1, "D", "Re", 293.66f, VibrantCoral, "🦊", "Fox", "Yip"),
            MusicalKey(2, "E", "Mi", 329.63f, VibrantAmber, "🐥", "Duck", "Quack"),
            MusicalKey(3, "F", "Fa", 349.23f, VibrantLime, "🐸", "Frog", "Ribbit"),
            MusicalKey(4, "G", "Sol", 392.00f, VibrantSky400, "🐬", "Dolphin", "Click"),
            MusicalKey(5, "A", "La", 440.00f, Color(0xFF0284C7), "🐦", "Bird", "Tweet"),
            MusicalKey(6, "B", "Ti", 493.88f, VibrantPurple, "🐱", "Cat", "Meow"),
            MusicalKey(7, "C+", "High Do", 523.25f, VibrantPink, "🐰", "Bunny", "Hop")
        )
    }

    val songs = remember {
        listOf(
            NurserySong(
                title = "Twinkle Star",
                emoji = "⭐",
                notes = listOf(0, 0, 4, 4, 5, 5, 4, 3, 3, 2, 2, 1, 1, 0),
                noteNames = listOf("Do", "Do", "Sol", "Sol", "La", "La", "Sol", "Fa", "Fa", "Mi", "Mi", "Re", "Re", "Do")
            ),
            NurserySong(
                title = "Mary's Lamb",
                emoji = "🐑",
                notes = listOf(2, 1, 0, 1, 2, 2, 2, 1, 1, 1, 2, 4, 4),
                noteNames = listOf("Mi", "Re", "Do", "Re", "Mi", "Mi", "Mi", "Re", "Re", "Re", "Mi", "Sol", "Sol")
            ),
            NurserySong(
                title = "Old MacDonald",
                emoji = "🚜",
                notes = listOf(0, 0, 0, 4, 5, 5, 4, 2, 2, 1, 1, 0),
                noteNames = listOf("Do", "Do", "Do", "Sol", "La", "La", "Sol", "Mi", "Mi", "Re", "Re", "Do")
            ),
            NurserySong(
                title = "Row Your Boat",
                emoji = "⛵",
                notes = listOf(0, 0, 0, 1, 2, 2, 1, 2, 3, 4),
                noteNames = listOf("Do", "Do", "Do", "Re", "Mi", "Mi", "Re", "Mi", "Fa", "Sol")
            )
        )
    }

    var selectedSong by remember { mutableStateOf<NurserySong?>(null) }
    var currentSongNoteIndex by remember { mutableIntStateOf(0) }
    var showSongCelebration by remember { mutableStateOf(false) }

    // Active playing key for animation
    var activeKeyId by remember { mutableStateOf<Int?>(null) }

    val coroutineScope = rememberCoroutineScope()

    fun handleKeyPress(key: MusicalKey) {
        soundEngine.playNote(key.frequency)
        activeKeyId = key.id

        coroutineScope.launch {
            delay(350)
            if (activeKeyId == key.id) {
                activeKeyId = null
            }
        }

        // Check song progress
        selectedSong?.let { song ->
            val expectedNote = song.notes.getOrNull(currentSongNoteIndex)
            if (expectedNote == key.id) {
                if (currentSongNoteIndex + 1 < song.notes.size) {
                    currentSongNoteIndex++
                } else {
                    // Song completed!
                    soundEngine.playVictory()
                    showSongCelebration = true
                    onAddStars(8)
                    currentSongNoteIndex = 0
                }
            }
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
            .testTag("animal_xylophone_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            KidTopBar(
                title = "Animal Xylophone",
                emoji = "🎹",
                stars = stars,
                soundEngine = soundEngine,
                onBack = onBack,
                isSoundOn = isSoundOn,
                isMusicOn = isMusicOn,
                onToggleSound = onToggleSound,
                onToggleMusic = onToggleMusic,
                subtitle = "Melody Meadow"
            )

            // Song Selection Header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (selectedSong == null) "🎵 SONG PLAY ALONG" else "🎶 PLAYING: ${selectedSong?.emoji} ${selectedSong?.title}",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        letterSpacing = 1.2.sp,
                        color = VibrantSky700
                    )

                    if (selectedSong != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(VibrantBevelDark)
                                .padding(bottom = 2.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(VibrantWhite)
                                .clickable {
                                    selectedSong = null
                                    currentSongNoteIndex = 0
                                }
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "✕ Free Play",
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp,
                                color = VibrantRose
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Song selection cards with 3D Bevel
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(songs) { song ->
                        val isCurrent = selectedSong == song
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isCurrent) VibrantAmberShadow else VibrantBevelDark)
                                .padding(bottom = 3.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isCurrent) VibrantAmber else VibrantWhite)
                                .border(
                                    2.dp,
                                    if (isCurrent) Color.White else VibrantBevelShadow,
                                    RoundedCornerShape(16.dp)
                                )
                                .clickable {
                                    selectedSong = song
                                    currentSongNoteIndex = 0
                                    soundEngine.playBoing()
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = song.emoji, fontSize = 18.sp)
                                Text(
                                    text = song.title,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 13.sp,
                                    color = if (isCurrent) Color(0xFF3E2723) else VibrantSky900
                                )
                            }
                        }
                    }
                }
            }

            // Song Guide Helper Bar
            selectedSong?.let { song ->
                val nextNoteKey = song.notes.getOrNull(currentSongNoteIndex)
                val targetKey = musicalKeys.find { it.id == nextNoteKey }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VibrantBevelDark)
                        .padding(bottom = 3.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(VibrantWhite)
                        .border(2.dp, VibrantAmber, RoundedCornerShape(14.dp))
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = "👉 Next Note:",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = VibrantSky900
                        )
                        targetKey?.let {
                            Text(
                                text = "${it.animalEmoji} ${it.solfege}",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = it.color
                            )
                        }
                    }

                    Text(
                        text = "${currentSongNoteIndex + 1}/${song.notes.size}",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = VibrantSky700
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Rainbow Musical Xylophone Keys
            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                musicalKeys.forEachIndexed { index, key ->
                    val isNextSongNote = selectedSong?.notes?.getOrNull(currentSongNoteIndex) == key.id
                    val isActive = activeKeyId == key.id

                    // Gradual xylophone bar height scaling
                    val heightFraction = 0.92f - (index * 0.05f)

                    SingleXylophoneKey(
                        key = key,
                        isTarget = isNextSongNote,
                        isActive = isActive,
                        heightFraction = heightFraction,
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        onPress = { handleKeyPress(key) }
                    )
                }
            }

            // Bottom free play instructions in 3D pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(18.dp))
                        .background(VibrantBevelDark)
                        .padding(bottom = 2.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(VibrantWhite)
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "🎵 Tap or swipe animals to make joyful melodies!",
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        color = VibrantSky900,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Celebration Confetti
        ConfettiCelebration(
            isActive = showSongCelebration,
            onFinished = { showSongCelebration = false }
        )
    }
}

@Composable
fun SingleXylophoneKey(
    key: MusicalKey,
    isTarget: Boolean,
    isActive: Boolean,
    heightFraction: Float,
    modifier: Modifier = Modifier,
    onPress: () -> Unit
) {
    var isTouching by remember { mutableStateOf(false) }

    val bounceScale by animateFloatAsState(
        targetValue = if (isActive || isTouching) 0.92f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 800f),
        label = "key_press_bounce"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "target_key_glow")
    val targetPulse by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.14f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "target_pulse_scale"
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        // Cute Animal Character Sitting On Top of the Key
        Box(
            modifier = Modifier
                .size(44.dp)
                .scale(if (isTarget) targetPulse else (if (isActive) 1.25f else 1f))
                .rotate(if (isActive) -8f else 0f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = key.animalEmoji,
                fontSize = 30.sp
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Target Guide Star Arrow
        if (isTarget) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Target Key Guide",
                tint = VibrantAmber,
                modifier = Modifier
                    .size(20.dp)
                    .scale(targetPulse)
            )
        } else {
            Spacer(modifier = Modifier.height(20.dp))
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Rainbow Xylophone Bar with 3D Gummy Bevel
        val isPressed = isActive || isTouching
        val shadowBevel = if (isPressed) 2.dp else 5.dp

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(heightFraction)
                .scale(bounceScale)
                .clip(RoundedCornerShape(22.dp))
                .background(key.color.copy(alpha = 0.65f))
                .padding(bottom = shadowBevel)
                .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            key.color.copy(alpha = 0.95f),
                            key.color,
                            key.color.copy(alpha = 0.88f)
                        )
                    )
                )
                .border(
                    width = if (isTarget) 3.dp else 2.dp,
                    color = if (isTarget) Color.White else Color.White.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp, bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isTouching = true
                            onPress()
                            tryAwaitRelease()
                            isTouching = false
                        }
                    )
                }
                .testTag("xylophone_key_${key.id}"),
            contentAlignment = Alignment.Center
        ) {
            // Metallic screw dots on top and bottom of bar
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.75f))
                )

                // Solfege Note label
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = key.solfege,
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                    Text(
                        text = key.noteName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }

                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.75f))
                )
            }
        }
    }
}
