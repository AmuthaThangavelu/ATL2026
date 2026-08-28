package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.audio.SoundEngine
import com.example.data.GamePreferences
import com.example.model.GameType
import com.example.ui.games.AnimalXylophoneScreen
import com.example.ui.games.BalloonPopScreen
import com.example.ui.games.FeedAnimalsScreen
import com.example.ui.games.HomeScreen
import com.example.ui.games.ShapeSorterScreen
import com.example.ui.games.SparklePaintScreen
import com.example.ui.games.StickerAlbumScreen
import com.example.ui.theme.WonderPlayTheme

class MainActivity : ComponentActivity() {

    private lateinit var soundEngine: SoundEngine
    private lateinit var gamePrefs: GamePreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        soundEngine = SoundEngine.getInstance(applicationContext)
        gamePrefs = GamePreferences.getInstance(applicationContext)

        setContent {
            WonderPlayTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .windowInsetsPadding(WindowInsets.safeDrawing)
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        WonderPlayApp(
                            soundEngine = soundEngine,
                            gamePrefs = gamePrefs
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun WonderPlayApp(
    soundEngine: SoundEngine,
    gamePrefs: GamePreferences
) {
    var currentScreen by remember { mutableStateOf<GameType?>(null) }

    val stars by gamePrefs.totalStars.collectAsState()
    val isSoundOn by soundEngine.isSoundEnabled.collectAsState()
    val isMusicOn by soundEngine.isMusicEnabled.collectAsState()

    // Back handling
    if (currentScreen != null) {
        BackHandler {
            soundEngine.playBoing()
            currentScreen = null
        }
    }

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            null -> {
                HomeScreen(
                    soundEngine = soundEngine,
                    stars = stars,
                    onSelectGame = { currentScreen = it },
                    isSoundOn = isSoundOn,
                    isMusicOn = isMusicOn,
                    onToggleSound = { soundEngine.toggleSound() },
                    onToggleMusic = { soundEngine.toggleMusic() }
                )
            }
            GameType.BALLOON_POP -> {
                BalloonPopScreen(
                    soundEngine = soundEngine,
                    stars = stars,
                    onAddStars = { gamePrefs.addStars(it) },
                    onBack = { currentScreen = null },
                    isSoundOn = isSoundOn,
                    isMusicOn = isMusicOn,
                    onToggleSound = { soundEngine.toggleSound() },
                    onToggleMusic = { soundEngine.toggleMusic() }
                )
            }
            GameType.ANIMAL_PIANO -> {
                AnimalXylophoneScreen(
                    soundEngine = soundEngine,
                    stars = stars,
                    onAddStars = { gamePrefs.addStars(it) },
                    onBack = { currentScreen = null },
                    isSoundOn = isSoundOn,
                    isMusicOn = isMusicOn,
                    onToggleSound = { soundEngine.toggleSound() },
                    onToggleMusic = { soundEngine.toggleMusic() }
                )
            }
            GameType.SHAPE_SORTER -> {
                ShapeSorterScreen(
                    soundEngine = soundEngine,
                    stars = stars,
                    onAddStars = { gamePrefs.addStars(it) },
                    onBack = { currentScreen = null },
                    isSoundOn = isSoundOn,
                    isMusicOn = isMusicOn,
                    onToggleSound = { soundEngine.toggleSound() },
                    onToggleMusic = { soundEngine.toggleMusic() }
                )
            }
            GameType.MAGIC_PAINT -> {
                SparklePaintScreen(
                    soundEngine = soundEngine,
                    stars = stars,
                    onAddStars = { gamePrefs.addStars(it) },
                    onBack = { currentScreen = null },
                    isSoundOn = isSoundOn,
                    isMusicOn = isMusicOn,
                    onToggleSound = { soundEngine.toggleSound() },
                    onToggleMusic = { soundEngine.toggleMusic() }
                )
            }
            GameType.FEED_ANIMALS -> {
                FeedAnimalsScreen(
                    soundEngine = soundEngine,
                    stars = stars,
                    onAddStars = { gamePrefs.addStars(it) },
                    onBack = { currentScreen = null },
                    isSoundOn = isSoundOn,
                    isMusicOn = isMusicOn,
                    onToggleSound = { soundEngine.toggleSound() },
                    onToggleMusic = { soundEngine.toggleMusic() }
                )
            }
            GameType.STICKER_ALBUM -> {
                StickerAlbumScreen(
                    soundEngine = soundEngine,
                    stars = stars,
                    stickers = gamePrefs.getAllDefaultStickers(),
                    onBack = { currentScreen = null },
                    isSoundOn = isSoundOn,
                    isMusicOn = isMusicOn,
                    onToggleSound = { soundEngine.toggleSound() },
                    onToggleMusic = { soundEngine.toggleMusic() }
                )
            }
        }
    }
}

