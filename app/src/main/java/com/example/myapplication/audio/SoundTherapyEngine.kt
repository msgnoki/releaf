package com.example.myapplication.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.*
import kotlin.math.*

/**
 * Moteur de thérapie sonore utilisant la génération audio native
 * Alternative à TarsosDSP pour une intégration plus simple sur Android
 */
class SoundTherapyEngine {
    
    private var audioTrack: AudioTrack? = null
    private var isPlaying = false
    private var audioJob: Job? = null
    
    private val sampleRate = 44100
    private val bufferSize = AudioTrack.getMinBufferSize(
        sampleRate,
        AudioFormat.CHANNEL_OUT_MONO,
        AudioFormat.ENCODING_PCM_16BIT
    )
    
    /**
     * Démarre la lecture d'une fréquence thérapeutique
     */
    suspend fun startFrequency(
        frequency: Int,
        binauralBeatHz: Int = 0,
        volume: Float = 0.5f
    ) = withContext(Dispatchers.IO) {
        stopAudio()
        
        // Configuration AudioTrack
        audioTrack = AudioTrack.Builder()
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            .setAudioFormat(
                AudioFormat.Builder()
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setSampleRate(sampleRate)
                    .setChannelMask(
                        if (binauralBeatHz > 0) AudioFormat.CHANNEL_OUT_STEREO 
                        else AudioFormat.CHANNEL_OUT_MONO
                    )
                    .build()
            )
            .setBufferSizeInBytes(bufferSize * 2)
            .build()
        
        isPlaying = true
        audioTrack?.play()
        
        // Génération audio en coroutine
        audioJob = launch {
            generateAudio(frequency, binauralBeatHz, volume)
        }
    }
    
    /**
     * Arrête la lecture audio
     */
    fun stopAudio() {
        isPlaying = false
        audioJob?.cancel()
        audioJob = null
        
        audioTrack?.apply {
            stop()
            release()
        }
        audioTrack = null
    }
    
    /**
     * Génère l'audio en temps réel
     */
    private suspend fun generateAudio(
        baseFrequency: Int,
        binauralBeatHz: Int,
        volume: Float
    ) = withContext(Dispatchers.IO) {
        val samplesPerBuffer = bufferSize / 2 // 16-bit = 2 bytes per sample
        val audioBuffer = ShortArray(if (binauralBeatHz > 0) samplesPerBuffer * 2 else samplesPerBuffer)
        
        var phase = 0.0
        var phaseLeft = 0.0
        var phaseRight = 0.0
        
        while (isPlaying && audioTrack != null) {
            try {
                if (binauralBeatHz > 0) {
                    // Battements binauraux (stéréo)
                    val leftFreq = baseFrequency.toDouble()
                    val rightFreq = baseFrequency + binauralBeatHz.toDouble()
                    
                    for (i in 0 until samplesPerBuffer) {
                        val leftSample = (sin(phaseLeft) * volume * Short.MAX_VALUE).toInt().toShort()
                        val rightSample = (sin(phaseRight) * volume * Short.MAX_VALUE).toInt().toShort()
                        
                        audioBuffer[i * 2] = leftSample     // Canal gauche
                        audioBuffer[i * 2 + 1] = rightSample // Canal droit
                        
                        phaseLeft += 2.0 * PI * leftFreq / sampleRate
                        phaseRight += 2.0 * PI * rightFreq / sampleRate
                        
                        // Éviter l'overflow
                        if (phaseLeft > 2.0 * PI) phaseLeft -= 2.0 * PI
                        if (phaseRight > 2.0 * PI) phaseRight -= 2.0 * PI
                    }
                } else {
                    // Fréquence simple (mono)
                    for (i in 0 until samplesPerBuffer) {
                        val sample = (sin(phase) * volume * Short.MAX_VALUE).toInt().toShort()
                        audioBuffer[i] = sample
                        
                        phase += 2.0 * PI * baseFrequency / sampleRate
                        if (phase > 2.0 * PI) phase -= 2.0 * PI
                    }
                }
                
                // Écriture dans AudioTrack
                val written = audioTrack?.write(audioBuffer, 0, audioBuffer.size) ?: -1
                if (written < 0) {
                    break
                }
                
                // Petite pause pour éviter la surcharge CPU
                delay(5)
                
            } catch (e: Exception) {
                // Gestion des erreurs audio
                break
            }
        }
    }
    
    /**
     * Vérifie si l'audio est en cours de lecture
     */
    fun isCurrentlyPlaying(): Boolean = isPlaying
    
    /**
     * Modifie le volume en temps réel
     */
    fun setVolume(volume: Float) {
        audioTrack?.setVolume(volume.coerceIn(0f, 1f))
    }
    
    /**
     * Libère les ressources
     */
    fun release() {
        stopAudio()
    }
}