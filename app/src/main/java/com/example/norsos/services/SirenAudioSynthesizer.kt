package com.example.norsos.services

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin

/**
 * Native offline audio synthesizer for emergency sirens and whistles.
 * Uses AudioTrack to generate pure sine tone sweeps without needing any external audio assets.
 */
class SirenAudioSynthesizer {

    private var audioTrack: AudioTrack? = null
    private var synthJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.Default)

    @Volatile
    var isPlaying: Boolean = false
        private set

    @Volatile
    var currentSoundType: SoundType = SoundType.NONE
        private set

    enum class SoundType {
        NONE,
        SIREN,
        WHISTLE,
        SOS_MORSE
    }

    fun startSiren() {
        stop()
        isPlaying = true
        currentSoundType = SoundType.SIREN
        synthJob = scope.launch {
            runAudioLoop { phase, sampleIndex, sampleRate ->
                // Smooth sinusoidal sweep between 650 Hz and 1350 Hz with period of ~1.2 seconds
                val cycleTime = 1.2
                val t = (sampleIndex % (sampleRate * cycleTime)) / (sampleRate * cycleTime)
                val freq = 650.0 + 700.0 * (0.5 * (1.0 + sin(2 * PI * t - PI / 2)))
                val nextPhase = phase + (2.0 * PI * freq / sampleRate)
                Pair(sin(phase), nextPhase % (2 * PI))
            }
        }
    }

    fun startWhistle() {
        stop()
        isPlaying = true
        currentSoundType = SoundType.WHISTLE
        synthJob = scope.launch {
            runAudioLoop { phase, sampleIndex, sampleRate ->
                // Piercing 2800 Hz whistle with rapid flutter (40 Hz vibrato)
                val vibrato = 50.0 * sin(2.0 * PI * 35.0 * sampleIndex / sampleRate)
                val freq = 2800.0 + vibrato
                val nextPhase = phase + (2.0 * PI * freq / sampleRate)
                Pair(sin(phase), nextPhase % (2 * PI))
            }
        }
    }

    private suspend fun runAudioLoop(
        sampleGenerator: (currentPhase: Double, sampleIndex: Long, sampleRate: Int) -> Pair<Double, Double>
    ) {
        val sampleRate = 44100
        val bufferSize = AudioTrack.getMinBufferSize(
            sampleRate,
            AudioFormat.CHANNEL_OUT_MONO,
            AudioFormat.ENCODING_PCM_16BIT
        ).coerceAtLeast(4096)

        try {
            val track = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_ALARM)
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
                .setBufferSizeInBytes(bufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .build()

            audioTrack = track
            track.play()

            val buffer = ShortArray(bufferSize / 2)
            var sampleIndex = 0L
            var phase = 0.0

            while (scope.isActive && isPlaying) {
                for (i in buffer.indices) {
                    val (sampleVal, nextPhase) = sampleGenerator(phase, sampleIndex, sampleRate)
                    phase = nextPhase
                    sampleIndex++
                    buffer[i] = (sampleVal * 30000.0).toInt().coerceIn(-32767, 32767).toShort()
                }
                track.write(buffer, 0, buffer.size)
            }
        } catch (e: Exception) {
            Log.e("SirenAudio", "Error generating audio", e)
        } finally {
            cleanupTrack()
        }
    }

    fun stop() {
        isPlaying = false
        currentSoundType = SoundType.NONE
        synthJob?.cancel()
        synthJob = null
        cleanupTrack()
    }

    private fun cleanupTrack() {
        try {
            audioTrack?.let {
                if (it.playState == AudioTrack.PLAYSTATE_PLAYING) {
                    it.stop()
                }
                it.release()
            }
        } catch (e: Exception) {
            Log.w("SirenAudio", "Error releasing track", e)
        } finally {
            audioTrack = null
        }
    }
}
