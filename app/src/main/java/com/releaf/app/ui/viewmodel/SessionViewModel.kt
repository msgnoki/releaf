package com.releaf.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.releaf.app.data.Technique
import com.releaf.app.data.model.TechniqueCategory
import com.releaf.app.services.StatisticsService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SessionViewModel : ViewModel() {
    
    private val statisticsService = StatisticsService()
    
    private val _sessionState = MutableStateFlow(SessionState())
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()
    
    data class SessionState(
        val isRecording: Boolean = false,
        val startTime: Long = 0L,
        val currentTechnique: Technique? = null,
        val moodBefore: Int = 5,
        val isSessionCompleted: Boolean = false,
        val errorMessage: String? = null
    )
    
    /**
     * Démarre une session
     */
    fun startSession(technique: Technique, moodBefore: Int = 5) {
        _sessionState.value = _sessionState.value.copy(
            isRecording = true,
            startTime = System.currentTimeMillis(),
            currentTechnique = technique,
            moodBefore = moodBefore,
            isSessionCompleted = false,
            errorMessage = null
        )
    }
    
    /**
     * Termine une session et l'enregistre dans Firebase
     */
    fun completeSession(moodAfter: Int = 7, notes: String = "") {
        val currentState = _sessionState.value
        
        if (!currentState.isRecording || currentState.currentTechnique == null) {
            return
        }
        
        viewModelScope.launch {
            try {
                val technique = currentState.currentTechnique
                val duration = ((System.currentTimeMillis() - currentState.startTime) / 1000 / 60).toInt()
                val category = getCategoryFromTechnique(technique.id)
                
                // Durée minimum de 1 minute
                val finalDuration = maxOf(1, duration)
                
                val result = statisticsService.recordSession(
                    techniqueId = technique.id,
                    category = category,
                    duration = finalDuration,
                    moodBefore = currentState.moodBefore,
                    moodAfter = moodAfter,
                    notes = notes
                )
                
                if (result.isFailure) {
                    throw result.exceptionOrNull() ?: Exception("Échec de l'enregistrement")
                }
                
                _sessionState.value = _sessionState.value.copy(
                    isRecording = false,
                    isSessionCompleted = true
                )
                
            } catch (e: Exception) {
                _sessionState.value = _sessionState.value.copy(
                    errorMessage = "Erreur lors de l'enregistrement: ${e.message}"
                )
            }
        }
    }
    
    /**
     * Annule une session en cours
     */
    fun cancelSession() {
        _sessionState.value = SessionState()
    }
    
    /**
     * Remet à zéro l'état après completion
     */
    fun resetState() {
        _sessionState.value = SessionState()
    }
    
    /**
     * Met à jour l'humeur avant la session
     */
    fun updateMoodBefore(mood: Int) {
        _sessionState.value = _sessionState.value.copy(moodBefore = mood)
    }
    
    /**
     * Détermine la catégorie d'une technique
     */
    private fun getCategoryFromTechnique(techniqueId: String): String {
        return when (techniqueId) {
            "breathing", "guided-breathing", "box-breathing", "478-breathing", 
            "diaphragmatic-breathing", "mindful-breathing" -> "RESPIRATION"
            
            "meditation", "meditation-breath-awareness", "loving-kindness" -> "MEDITATION"
            
            "progressive-muscle-relaxation", "body-scan", "autogenic-training", 
            "auto-hypnosis-autogenic" -> "RELAXATION"
            
            "grounding", "thought-labeling", "forest-immersion", 
            "peaceful-visualization" -> "MINDFULNESS"
            
            "stress-relief-bubbles", "stress-ball", "sound-therapy" -> "STRESS_RELIEF"
            
            else -> "MINDFULNESS"
        }
    }
    
    /**
     * Obtient la durée actuelle de la session
     */
    fun getCurrentDuration(): Int {
        val currentState = _sessionState.value
        if (!currentState.isRecording) return 0
        
        return ((System.currentTimeMillis() - currentState.startTime) / 1000 / 60).toInt()
    }
}