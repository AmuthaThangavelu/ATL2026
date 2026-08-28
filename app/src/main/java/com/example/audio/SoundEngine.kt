package com.example.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * High-performance real-time sound engine tailored for kids apps.
 * Synthesizes crisp, cheerful sound effects and nursery music box tunes using low-latency PCM audio.
 */
class SoundEngine private constructor(private val context: Context) {

    private val scope = CoroutineScope(Dispatchers.Default + Job())
    private var musicJob: Job? = null

    private val prefs = context.getSharedPreferences("wonderplay_sound_prefs", Context.MODE_PRIVATE)

    private val _isSoundEnabled = MutableStateFlow(prefs.getBoolean("sound_enabled", true))
    val isSoundEnabled: StateFlow<Boolean> = _isSoundEnabled.asStateFlow()

    private val _isMusicEnabled = MutableStateFlow(prefs.getBoolean("music_enabled", true))
    val isMusicEnabled: StateFlow<Boolean> = _isMusicEnabled.asStateFlow()

    private val sampleRate = 44100
    private val audioTrackCache = ConcurrentHashMap<String, ShortArray>()

    init {
        // Precompute common kid-friendly sounds
        prepareSounds()
        if (_isMusicEnabled.value) {
            startBackgroundMusic()
        }
    }

    fun toggleSound(): Boolean {
        val newState = !_isSoundEnabled.value
        _isSoundEnabled.value = newState
        prefs.edit().putBoolean("sound_enabled", newState).apply()
        if (newState) {
            playPop()
        }
        return newState
    }

    fun toggleMusic(): Boolean {
        val newState = !_isMusicEnabled.value
        _isMusicEnabled.value = newState
        prefs.edit().putBoolean("music_enabled", newState).apply()
        if (newState) {
            startBackgroundMusic()
        } else {
            stopBackgroundMusic()
        }
        return newState
    }

    private fun prepareSounds() {
        audioTrackCache["pop"] = generatePopPcm()
        audioTrackCache["boing"] = generateBoingPcm()
        audioTrackCache["chomp"] = generateChompPcm()
        audioTrackCache["giggle"] = generateGigglePcm()
        audioTrackCache["sparkle"] = generateSparklePcm()
        audioTrackCache["victory"] = generateVictoryPcm()
    }

    fun playPop() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val pcm = audioTrackCache["pop"] ?: generatePopPcm()
            playPcm(pcm)
        }
    }

    fun playBoing() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val pcm = audioTrackCache["boing"] ?: generateBoingPcm()
            playPcm(pcm)
        }
    }

    fun playChomp() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val pcm = audioTrackCache["chomp"] ?: generateChompPcm()
            playPcm(pcm)
        }
    }

    fun playGiggle() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val pcm = audioTrackCache["giggle"] ?: generateGigglePcm()
            playPcm(pcm)
        }
    }

    fun playSparkle() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val pcm = audioTrackCache["sparkle"] ?: generateSparklePcm()
            playPcm(pcm)
        }
    }

    fun playVictory() {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val pcm = audioTrackCache["victory"] ?: generateVictoryPcm()
            playPcm(pcm)
        }
    }

    fun playNote(frequency: Float, durationMs: Int = 450) {
        if (!_isSoundEnabled.value) return
        scope.launch {
            val pcm = generateXylophoneTone(frequency, durationMs)
            playPcm(pcm)
        }
    }

    fun playDrawingChime(pitchRatio: Float) {
        if (!_isSoundEnabled.value) return
        val baseFreq = 440f
        val freq = baseFreq * (0.6f + pitchRatio * 1.4f)
        scope.launch {
            val pcm = generateSparkleTone(freq, 120)
            playPcm(pcm)
        }
    }

    private fun playPcm(pcmData: ShortArray) {
        try {
            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(sampleRate)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(pcmData.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            track.write(pcmData, 0, pcmData.size)
            track.play()

            // Release after completion
            scope.launch {
                val durationMs = (pcmData.size * 1000L) / sampleRate + 50
                delay(durationMs)
                try {
                    track.stop()
                    track.release()
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {}
    }

    private fun startBackgroundMusic() {
        musicJob?.cancel()
        musicJob = scope.launch {
            // Sweet gentle nursery music box arpeggio melody (Twinkle Twinkle / Lullaby vibes)
            val melody = listOf(
                Pair(523.25f, 400L), // C5
                Pair(523.25f, 400L), // C5
                Pair(783.99f, 400L), // G5
                Pair(783.99f, 400L), // G5
                Pair(880.00f, 400L), // A5
                Pair(880.00f, 400L), // A5
                Pair(783.99f, 800L), // G5
                Pair(698.46f, 400L), // F5
                Pair(698.46f, 400L), // F5
                Pair(659.25f, 400L), // E5
                Pair(659.25f, 400L), // E5
                Pair(587.33f, 400L), // D5
                Pair(587.33f, 400L), // D5
                Pair(523.25f, 800L)  // C5
            )

            while (isActive && _isMusicEnabled.value) {
                for ((freq, duration) in melody) {
                    if (!isActive || !_isMusicEnabled.value) break
                    if (_isSoundEnabled.value) {
                        val pcm = generateSoftMusicBoxTone(freq, (duration * 0.75f).toInt())
                        playPcm(pcm)
                    }
                    delay(duration)
                }
                delay(1200L)
            }
        }
    }

    private fun stopBackgroundMusic() {
        musicJob?.cancel()
        musicJob = null
    }

    // --- Sound Synthesis Algorithms ---

    private fun generatePopPcm(): ShortArray {
        val durationSec = 0.09
        val numSamples = (sampleRate * durationSec).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            // Rapid pitch drop from 850Hz to 120Hz
            val freq = 850.0 * (1.0 - progress * 0.85)
            val envelope = (1.0 - progress) * (1.0 - progress)
            val sinVal = sin(2.0 * PI * freq * t)
            val popVal = sinVal * envelope * 28000.0
            buffer[i] = popVal.toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    private fun generateBoingPcm(): ShortArray {
        val durationSec = 0.22
        val numSamples = (sampleRate * durationSec).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            // Upward boing sweep with vibrato
            val baseFreq = 220.0 + progress * 480.0 + sin(2.0 * PI * 22.0 * t) * 40.0
            val env = (1.0 - progress * 0.9)
            val s = sin(2.0 * PI * baseFreq * t) * env * 26000.0
            buffer[i] = s.toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    private fun generateChompPcm(): ShortArray {
        val durationSec = 0.14
        val numSamples = (sampleRate * durationSec).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            val freq = 340.0 - progress * 160.0
            val env = sin(PI * progress)
            val s = (sin(2.0 * PI * freq * t) + 0.3 * sin(2.0 * PI * (freq * 2.5) * t)) * env * 25000.0
            buffer[i] = s.toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    private fun generateGigglePcm(): ShortArray {
        val durationSec = 0.28
        val numSamples = (sampleRate * durationSec).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            // 3 quick chirpy bubbles
            val chirpPhase = (progress * 3.0) % 1.0
            val freq = 650.0 + chirpPhase * 350.0
            val env = sin(PI * chirpPhase) * (1.0 - progress * 0.4)
            val s = sin(2.0 * PI * freq * t) * env * 24000.0
            buffer[i] = s.toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    private fun generateSparklePcm(): ShortArray {
        val durationSec = 0.4
        val numSamples = (sampleRate * durationSec).toInt()
        val buffer = ShortArray(numSamples)
        val freqs = doubleArrayOf(1046.50, 1318.51, 1567.98, 2093.00, 2637.02)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            val noteIdx = (progress * freqs.size).toInt().coerceIn(0, freqs.size - 1)
            val noteProgress = (progress * freqs.size) - noteIdx
            val f = freqs[noteIdx]
            val env = exp(-noteProgress * 4.0) * (1.0 - progress * 0.3)
            val s = (sin(2.0 * PI * f * t) + 0.25 * sin(2.0 * PI * (f * 2.0) * t)) * env * 24000.0
            buffer[i] = s.toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    private fun generateVictoryPcm(): ShortArray {
        val durationSec = 0.65
        val numSamples = (sampleRate * durationSec).toInt()
        val buffer = ShortArray(numSamples)
        val chordFreqs = doubleArrayOf(523.25, 659.25, 783.99, 1046.50) // C Major arpeggio + flourish
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            var sample = 0.0
            for ((idx, f) in chordFreqs.withIndex()) {
                val start = idx * 0.12
                if (progress >= start) {
                    val noteProg = (progress - start) / (1.0 - start)
                    val env = exp(-noteProg * 2.5)
                    sample += sin(2.0 * PI * f * t) * env * 6000.0
                }
            }
            buffer[i] = sample.toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    private fun generateXylophoneTone(freq: Float, durationMs: Int): ShortArray {
        val durationSec = durationMs / 1000.0
        val numSamples = (sampleRate * durationSec).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            // Warm marimba/xylophone harmonics: fundamental + soft 3rd harmonic bell overtone
            val env = exp(-progress * 5.0)
            val f = freq.toDouble()
            val sample = (sin(2.0 * PI * f * t) + 0.35 * sin(2.0 * PI * (f * 3.0) * t) + 0.15 * sin(2.0 * PI * (f * 4.0) * t)) * env * 28000.0
            buffer[i] = sample.toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    private fun generateSparkleTone(freq: Float, durationMs: Int): ShortArray {
        val durationSec = durationMs / 1000.0
        val numSamples = (sampleRate * durationSec).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            val env = (1.0 - progress) * (1.0 - progress)
            val f = freq.toDouble()
            val sample = (sin(2.0 * PI * f * t) + 0.2 * sin(2.0 * PI * (f * 2.0) * t)) * env * 22000.0
            buffer[i] = sample.toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    private fun generateSoftMusicBoxTone(freq: Float, durationMs: Int): ShortArray {
        val durationSec = durationMs / 1000.0
        val numSamples = (sampleRate * durationSec).toInt()
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            val t = i.toDouble() / sampleRate
            val progress = i.toDouble() / numSamples
            val env = exp(-progress * 4.2)
            val f = freq.toDouble()
            // Soft crystalline chime
            val sample = (sin(2.0 * PI * f * t) + 0.2 * sin(2.0 * PI * (f * 2.0) * t)) * env * 12000.0
            buffer[i] = sample.toInt().coerceIn(-32767, 32767).toShort()
        }
        return buffer
    }

    companion object {
        @Volatile
        private var INSTANCE: SoundEngine? = null

        fun getInstance(context: Context): SoundEngine {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SoundEngine(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
