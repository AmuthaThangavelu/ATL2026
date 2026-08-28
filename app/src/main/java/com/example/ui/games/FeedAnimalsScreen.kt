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
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.audio.SoundEngine
import com.example.model.AnimalFriend
import com.example.model.FoodItem
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun FeedAnimalsScreen(
    soundEngine: SoundEngine,
    stars: Int,
    onAddStars: (Int) -> Unit,
    onBack: () -> Unit,
    isSoundOn: Boolean,
    isMusicOn: Boolean,
    onToggleSound: () -> Unit,
    onToggleMusic: () -> Unit
) {
    val animalFriends = remember {
        listOf(
            AnimalFriend(
                id = "panda",
                name = "Panda",
                emoji = "🐼",
                avatarColor = Color(0xFFDCFCE7),
                favoriteFoods = listOf(
                    FoodItem("bamboo", "Bamboo", "🎋"),
                    FoodItem("apple", "Apple", "🍏"),
                    FoodItem("melon", "Melon", "🍉"),
                    FoodItem("cake", "Pancake", "🥞")
                )
            ),
            AnimalFriend(
                id = "monkey",
                name = "Monkey",
                emoji = "🐵",
                avatarColor = Color(0xFFFEF3C7),
                favoriteFoods = listOf(
                    FoodItem("banana", "Banana", "🍌"),
                    FoodItem("strawberry", "Strawberry", "🍓"),
                    FoodItem("peanut", "Peanut", "🥜"),
                    FoodItem("grape", "Grapes", "🍇")
                )
            ),
            AnimalFriend(
                id = "bunny",
                name = "Bunny",
                emoji = "🐰",
                avatarColor = Color(0xFFFCE7F3),
                favoriteFoods = listOf(
                    FoodItem("carrot", "Carrot", "🥕"),
                    FoodItem("red_apple", "Apple", "🍎"),
                    FoodItem("corn", "Corn", "🌽"),
                    FoodItem("cookie", "Cookie", "🍪")
                )
            ),
            AnimalFriend(
                id = "dino",
                name = "Dino",
                emoji = "🦖",
                avatarColor = Color(0xFFE0F2FE),
                favoriteFoods = listOf(
                    FoodItem("icecream", "Ice Cream", "🍦"),
                    FoodItem("burger", "Burger", "🍔"),
                    FoodItem("donut", "Donut", "🍩"),
                    FoodItem("pizza", "Pizza", "🍕")
                )
            )
        )
    }

    var selectedAnimal by remember { mutableStateOf(animalFriends.first()) }
    var hungerProgress by remember { mutableFloatStateOf(0f) }
    var isEatingAnimation by remember { mutableStateOf(false) }
    var showCelebration by remember { mutableStateOf(false) }
    var speechBubble by remember { mutableStateOf("I'm hungry! Feed me yummy treats! 😋") }

    val coroutineScope = rememberCoroutineScope()

    fun feedFood(food: FoodItem) {
        soundEngine.playChomp()
        isEatingAnimation = true
        speechBubble = "YUM! Delicious ${food.name}! 😋❤️"

        val newProgress = (hungerProgress + 0.25f).coerceAtMost(1f)
        hungerProgress = newProgress
        onAddStars(2)

        coroutineScope.launch {
            delay(400)
            soundEngine.playGiggle()
            delay(600)
            isEatingAnimation = false

            if (newProgress >= 1f) {
                soundEngine.playVictory()
                showCelebration = true
                speechBubble = "🎉 My tummy is full! Thank you super friend! ❤️"
                onAddStars(8)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        VibrantAmber,
                        Color(0xFFFEF08A),
                        Color(0xFFFFFBEB)
                    )
                )
            )
            .testTag("feed_animals_screen")
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            KidTopBar(
                title = "Feed Animals",
                emoji = "🐼",
                stars = stars,
                soundEngine = soundEngine,
                onBack = onBack,
                isSoundOn = isSoundOn,
                isMusicOn = isMusicOn,
                onToggleSound = onToggleSound,
                onToggleMusic = onToggleMusic,
                subtitle = "Feed & Care"
            )

            // Select Animal Friend Row
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(animalFriends) { animal ->
                    val isSelected = selectedAnimal.id == animal.id
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(22.dp))
                            .background(if (isSelected) VibrantAmberShadow else VibrantBevelDark)
                            .padding(bottom = 3.dp)
                            .clip(RoundedCornerShape(19.dp))
                            .background(if (isSelected) VibrantAmber else VibrantWhite)
                            .border(
                                2.dp,
                                if (isSelected) Color.White else VibrantBevelShadow,
                                RoundedCornerShape(19.dp)
                            )
                            .clickable {
                                soundEngine.playBoing()
                                selectedAnimal = animal
                                hungerProgress = 0f
                                speechBubble = "Hi! Feed me my favorite treats! 😋"
                            }
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(text = animal.emoji, fontSize = 20.sp)
                            Text(
                                text = animal.name,
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = if (isSelected) Color(0xFF3E2723) else VibrantSky900
                            )
                        }
                    }
                }
            }

            // Tummy Fullness Progress Bar
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🥣 TUMMY FULLNESS:",
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        letterSpacing = 1.sp,
                        color = Color(0xFF15803D)
                    )
                    Text(
                        text = "${(hungerProgress * 100).toInt()}%",
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = VibrantRose
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(18.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(VibrantWhite)
                        .border(2.dp, VibrantBevelShadow, RoundedCornerShape(12.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(hungerProgress)
                            .height(18.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.horizontalGradient(
                                    listOf(VibrantLime, VibrantCyan)
                                )
                            )
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Speech Bubble from Animal
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(VibrantBevelDark)
                        .padding(bottom = 3.dp)
                        .clip(RoundedCornerShape(17.dp))
                        .background(VibrantWhite)
                        .border(2.dp, VibrantPurple, RoundedCornerShape(17.dp))
                        .padding(horizontal = 20.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = speechBubble,
                        fontWeight = FontWeight.Black,
                        fontSize = 14.sp,
                        color = VibrantSky900,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Big Animated Animal Friend Centerpiece
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AnimalCenterpiece(
                    animal = selectedAnimal,
                    isEating = isEatingAnimation,
                    isFull = hungerProgress >= 1f
                )
            }

            // Food Tray / Conveyor at Bottom
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .background(VibrantWhite)
                    .border(2.5.dp, VibrantBevelShadow, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "👉 TAP FOOD TO FEED ${selectedAnimal.name.uppercase()}! 👇",
                    fontWeight = FontWeight.Black,
                    fontSize = 11.sp,
                    letterSpacing = 1.2.sp,
                    color = VibrantCoral
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    selectedAnimal.favoriteFoods.forEach { food ->
                        FoodItemCard(
                            food = food,
                            onFeed = { feedFood(food) }
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
fun AnimalCenterpiece(
    animal: AnimalFriend,
    isEating: Boolean,
    isFull: Boolean
) {
    val infiniteTransition = rememberInfiniteTransition(label = "animal_idle")
    val idleSway by infiniteTransition.animateFloat(
        initialValue = -3f,
        targetValue = 3f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "animal_idle_sway"
    )

    val eatingScale by animateFloatAsState(
        targetValue = if (isEating) 1.25f else 1f,
        animationSpec = spring(dampingRatio = 0.35f, stiffness = 800f),
        label = "eating_bounce"
    )

    Box(
        modifier = Modifier
            .size(190.dp)
            .scale(eatingScale)
            .rotate(if (isFull) idleSway * 2 else idleSway)
            .clip(CircleShape)
            .background(VibrantBevelDark)
            .padding(bottom = 6.dp)
            .clip(CircleShape)
            .background(animal.avatarColor)
            .border(5.dp, Color.White, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = animal.emoji,
                fontSize = 90.sp
            )

            if (isFull) {
                Text(
                    text = "👑 Full & Happy! ❤️",
                    fontWeight = FontWeight.Black,
                    fontSize = 12.sp,
                    color = VibrantRose
                )
            }
        }
    }
}

@Composable
fun FoodItemCard(
    food: FoodItem,
    onFeed: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = 0.4f, stiffness = 700f),
        label = "food_press"
    )

    Box(
        modifier = Modifier
            .scale(scale)
            .clip(RoundedCornerShape(20.dp))
            .background(VibrantAmberShadow)
            .padding(bottom = 4.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(Color(0xFFFFFBEB))
            .border(2.dp, VibrantAmber, RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp, bottomStart = 16.dp, bottomEnd = 16.dp))
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressed = true
                        tryAwaitRelease()
                        isPressed = false
                        onFeed()
                    }
                )
            }
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .testTag("food_item_${food.id}"),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = food.emoji, fontSize = 32.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = food.name,
                fontWeight = FontWeight.Black,
                fontSize = 11.sp,
                color = VibrantSky900
            )
        }
    }
}
