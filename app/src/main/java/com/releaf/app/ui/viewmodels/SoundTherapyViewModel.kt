package com.releaf.app.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.releaf.app.audio.SoundTherapyEngine
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SoundTherapyState(
    val isPlaying: Boolean = false,
    val selectedFrequency: Int? = null,
    val selectedBinauralBeat: Int = 0,
    val volume: Float = 0.5f,
    val sessionDuration: Long = 0L,
    val error: String? = null
)

class SoundTherapyViewModel : ViewModel() {
    
    private val soundEngine = SoundTherapyEngine()
    
    private val _uiState = MutableStateFlow(SoundTherapyState())
    val uiState: StateFlow<SoundTherapyState> = _uiState.asStateFlow()
    
    private var sessionStartTime: Long = 0
    
    /**
     * Démarre la lecture d'une fréquence
     */
    fun startFrequency(frequency: Int, binauralBeat: Int = 0) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    selectedFrequency = frequency,
                    selectedBinauralBeat = binauralBeat,
                    error = null
                )
                
                soundEngine.startFrequency(
                    frequency = frequency,
                    binauralBeatHz = binauralBeat,
                    volume = _uiState.value.volume
                )
                
                sessionStartTime = System.currentTimeMillis()
                _uiState.value = _uiState.value.copy(isPlaying = true)
                
                // Démarrer le timer de session
                startSessionTimer()
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = "Erreur lors du démarrage audio: ${e.message}",
                    isPlaying = false
                )
            }
        }
    }
    
    /**
     * Arrête la lecture audio
     */
    fun stopAudio() {
        soundEngine.stopAudio()
        _uiState.value = _uiState.value.copy(
            isPlaying = false,
            selectedFrequency = null
        )
    }
    
    /**
     * Modifie le volume
     */
    fun setVolume(volume: Float) {
        val clampedVolume = volume.coerceIn(0f, 1f)
        soundEngine.setVolume(clampedVolume)
        _uiState.value = _uiState.value.copy(volume = clampedVolume)
    }
    
    /**
     * Change les battements binauraux en temps réel
     */
    fun setBinauralBeat(binauralBeat: Int) {
        val currentFreq = _uiState.value.selectedFrequency
        if (currentFreq != null && _uiState.value.isPlaying) {
            // Redémarre avec les nouveaux paramètres
            viewModelScope.launch {
                soundEngine.stopAudio()
                soundEngine.startFrequency(
                    frequency = currentFreq,
                    binauralBeatHz = binauralBeat,
                    volume = _uiState.value.volume
                )
                _uiState.value = _uiState.value.copy(selectedBinauralBeat = binauralBeat)
            }
        } else {
            _uiState.value = _uiState.value.copy(selectedBinauralBeat = binauralBeat)
        }
    }
    
    /**
     * Démare le timer de session
     */
    private fun startSessionTimer() {
        viewModelScope.launch {
            while (_uiState.value.isPlaying) {
                kotlinx.coroutines.delay(1000)
                if (_uiState.value.isPlaying) {
                    val duration = System.currentTimeMillis() - sessionStartTime
                    _uiState.value = _uiState.value.copy(sessionDuration = duration)
                }
            }
        }
    }
    
    /**
     * Réinitialise l'erreur
     */
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    /**
     * Libère les ressources à la destruction du ViewModel
     */
    override fun onCleared() {
        super.onCleared()
        soundEngine.release()
    }
}