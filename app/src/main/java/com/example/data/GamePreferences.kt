package com.example.data

import android.content.Context
import com.example.model.StickerReward
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GamePreferences private constructor(context: Context) {

    private val prefs = context.getSharedPreferences("wonderplay_kids_data", Context.MODE_PRIVATE)

    private val _totalStars = MutableStateFlow(prefs.getInt("total_stars", 15)) // Start with 15 welcoming bonus stars
    val totalStars: StateFlow<Int> = _totalStars.asStateFlow()

    private val _unlockedStickers = MutableStateFlow(
        prefs.getStringSet("unlocked_stickers", setOf("star_1", "star_2")) ?: setOf("star_1", "star_2")
    )
    val unlockedStickers: StateFlow<Set<String>> = _unlockedStickers.asStateFlow()

    fun addStars(amount: Int) {
        val newTotal = _totalStars.value + amount
        _totalStars.value = newTotal
        prefs.edit().putInt("total_stars", newTotal).apply()
        checkStickerUnlocks(newTotal)
    }

    fun unlockSticker(stickerId: String) {
        val newSet = _unlockedStickers.value + stickerId
        _unlockedStickers.value = newSet
        prefs.edit().putStringSet("unlocked_stickers", newSet).apply()
    }

    private fun checkStickerUnlocks(currentStars: Int) {
        val allStickers = getAllDefaultStickers()
        val currentUnlocked = _unlockedStickers.value.toMutableSet()
        var changed = false
        for (sticker in allStickers) {
            if (currentStars >= sticker.starsRequired && !currentUnlocked.contains(sticker.id)) {
                currentUnlocked.add(sticker.id)
                changed = true
            }
        }
        if (changed) {
            _unlockedStickers.value = currentUnlocked
            prefs.edit().putStringSet("unlocked_stickers", currentUnlocked).apply()
        }
    }

    fun getAllDefaultStickers(): List<StickerReward> {
        val unlocked = _unlockedStickers.value
        val list = listOf(
            StickerReward("star_1", "Happy Star", "⭐", "Welcome to WonderPlay!", 0, true),
            StickerReward("star_2", "Cute Puppy", "🐶", "Friendly puppy pal", 5, unlocked.contains("star_1")),
            StickerReward("star_3", "Little Lion", "🦁", "King of the jungle!", 20, unlocked.contains("star_3")),
            StickerReward("star_4", "Magic Rainbow", "🌈", "Full of sparkling colors", 40, unlocked.contains("star_4")),
            StickerReward("star_5", "Sweet Cupcake", "🧁", "Delicious sugary treat", 60, unlocked.contains("star_5")),
            StickerReward("star_6", "Baby Dino", "🦖", "Friendly roaring dino", 80, unlocked.contains("star_6")),
            StickerReward("star_7", "Magic Wand", "🪄", "Abracadabra sparkles!", 100, unlocked.contains("star_7")),
            StickerReward("star_8", "Rocket Ship", "🚀", "Blast off to space!", 130, unlocked.contains("star_8")),
            StickerReward("star_9", "Fluffy Panda", "🐼", "Loves crunchy bamboo", 160, unlocked.contains("star_9")),
            StickerReward("star_10", "Golden Crown", "👑", "Super kid champion!", 200, unlocked.contains("star_10")),
            StickerReward("star_11", "Unicorn Magic", "🦄", "Sparkly magical dream", 250, unlocked.contains("star_11")),
            StickerReward("star_12", "Shiny Trophy", "🏆", "Master of all games!", 300, unlocked.contains("star_12"))
        )
        return list.map { it.copy(isUnlocked = unlocked.contains(it.id) || it.starsRequired <= _totalStars.value) }
    }

    companion object {
        @Volatile
        private var INSTANCE: GamePreferences? = null

        fun getInstance(context: Context): GamePreferences {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: GamePreferences(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
