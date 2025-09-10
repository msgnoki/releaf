package com.example.myapplication.utils

import kotlinx.coroutines.delay

object SystemTimer {
    
    /**
     * Attend de manière précise en utilisant l'horloge système
     * @param durationMs Durée en millisecondes
     */
    suspend fun preciseDelay(durationMs: Long) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + durationMs
        
        while (System.currentTimeMillis() < endTime) {
            delay(16) // Check every frame (~60fps pour fluidité)
        }
    }
    
    /**
     * Execute une action avec un timing précis basé sur l'horloge système
     * @param durationMs Durée en millisecondes
     * @param onProgress Callback avec le pourcentage de progression (0.0 à 1.0)
     * @param onComplete Callback à la fin
     */
    suspend fun preciseTimer(
        durationMs: Long,
        onProgress: (Float) -> Unit = {},
        onComplete: () -> Unit = {}
    ) {
        val startTime = System.currentTimeMillis()
        val endTime = startTime + durationMs
        
        while (System.currentTimeMillis() < endTime) {
            val elapsed = System.currentTimeMillis() - startTime
            val progress = (elapsed.toFloat() / durationMs).coerceIn(0f, 1f)
            onProgress(progress)
            delay(16) // 60fps refresh rate
        }
        
        onProgress(1f) // Assurer que nous finissons à 100%
        onComplete()
    }
    
    /**
     * Horloge en temps réel pour les animations longues
     */
    fun currentTimeMillis(): Long = System.currentTimeMillis()
}