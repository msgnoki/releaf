package com.releaf.app.services

import com.releaf.app.data.TechniquesRepository
import com.releaf.app.data.model.RecommendationWithTechnique
import com.releaf.app.data.model.TechniqueCategory
import com.releaf.app.data.model.DurationPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
object RecommendationService {
    private val _recommendations = MutableStateFlow<List<RecommendationWithTechnique>>(emptyList())
    val recommendations: Flow<List<RecommendationWithTechnique>> = _recommendations.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: Flow<Boolean> = _isLoading.asStateFlow()
    
    private var lastGenerationTime = 0L
    private val cacheValidityDuration = 30 * 60 * 1000L // 30 minutes
    
    suspend fun generateRecommendations(
        userId: String,
        recentTechniqueIds: List<String> = emptyList(),
        favoriteCategory: TechniqueCategory? = null,
        durationPreference: DurationPreference = DurationPreference.MEDIUM,
        experienceLevel: Int = 1,
        isInCrisis: Boolean = false,
        forceRefresh: Boolean = false
    ) {
        val currentTime = System.currentTimeMillis()
        
        // Check cache validity
        if (!forceRefresh && 
            currentTime - lastGenerationTime < cacheValidityDuration && 
            _recommendations.value.isNotEmpty()) {
            return
        }
        
        _isLoading.value = true
        
        try {
            val newRecommendations = TechniquesRepository.generateRecommendations(
                userId = userId,
                recentTechniqueIds = recentTechniqueIds,
                favoriteCategory = favoriteCategory,
                durationPreference = durationPreference,
                experienceLevel = experienceLevel,
                isInCrisis = isInCrisis
            )
            
            _recommendations.value = newRecommendations
            lastGenerationTime = currentTime
            
        } finally {
            _isLoading.value = false
        }
    }
    
    suspend fun getCrisisRecommendations(userId: String): List<RecommendationWithTechnique> {
        return TechniquesRepository.generateRecommendations(
            userId = userId,
            isInCrisis = true
        )
    }
    
    fun clearCache() {
        _recommendations.value = emptyList()
        lastGenerationTime = 0L
    }
    
    fun getRecommendationById(recommendationId: String): RecommendationWithTechnique? {
        return _recommendations.value.find { it.recommendation.id == recommendationId }
    }
}