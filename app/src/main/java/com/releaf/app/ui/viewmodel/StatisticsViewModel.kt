package com.releaf.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.releaf.app.services.StatisticsService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StatisticsViewModel : ViewModel() {
    
    private val statisticsService = StatisticsService()
    
    private val _uiState = MutableStateFlow(StatisticsUiState())
    val uiState: StateFlow<StatisticsUiState> = _uiState.asStateFlow()
    
    init {
        loadStatistics()
    }
    
    data class StatisticsUiState(
        val isLoading: Boolean = true,
        val statistics: StatisticsService.UserStatistics? = null,
        val errorMessage: String? = null
    )
    
    private fun loadStatistics() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                
                // Utiliser le Flow pour des données en temps réel
                statisticsService.getStatisticsFlow().collect { statistics ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        statistics = statistics,
                        errorMessage = null
                    )
                }
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erreur lors du chargement: ${e.message}"
                )
            }
        }
    }
    
    fun refreshStatistics() {
        loadStatistics()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    /**
     * Enregistre une nouvelle session
     */
    fun recordSession(
        techniqueId: String,
        category: String,
        duration: Int,
        moodBefore: Int,
        moodAfter: Int,
        notes: String = ""
    ) {
        viewModelScope.launch {
            statisticsService.recordSession(
                techniqueId = techniqueId,
                category = category,
                duration = duration,
                moodBefore = moodBefore,
                moodAfter = moodAfter,
                notes = notes
            )
            // Les statistiques se mettront à jour automatiquement via le Flow
        }
    }
    
    /**
     * Crée des données de test (mode développement uniquement)
     */
    fun createTestData() {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                statisticsService.createTestData()
                // Les statistiques se mettront à jour automatiquement via le Flow
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erreur lors de la création des données de test: ${e.message}"
                )
            }
        }
    }
}