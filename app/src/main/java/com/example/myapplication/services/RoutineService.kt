package com.example.myapplication.services

import com.example.myapplication.data.TechniquesRepository
import com.example.myapplication.data.model.RoutineOfDay
import com.example.myapplication.data.model.RoutineBlock
import com.example.myapplication.data.model.TechniqueCategory
import com.example.myapplication.data.model.DurationPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.UUID
object RoutineService {
    private val _todaysRoutine = MutableStateFlow<RoutineOfDay?>(null)
    val todaysRoutine: Flow<RoutineOfDay?> = _todaysRoutine.asStateFlow()
    
    private val _isGenerating = MutableStateFlow(false)
    val isGenerating: Flow<Boolean> = _isGenerating.asStateFlow()
    
    private val routineCache = mutableMapOf<String, RoutineOfDay>()
    
    suspend fun generateTodaysRoutine(
        userId: String,
        durationPreference: DurationPreference = DurationPreference.MEDIUM,
        favoriteCategory: TechniqueCategory? = null,
        avoidRecentIds: List<String> = emptyList()
    ): RoutineOfDay {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        val cacheKey = "${userId}_$today"
        
        // Return cached routine if exists
        routineCache[cacheKey]?.let { cachedRoutine ->
            _todaysRoutine.value = cachedRoutine
            return cachedRoutine
        }
        
        _isGenerating.value = true
        
        try {
            val routine = createRoutine(
                userId = userId,
                date = today,
                targetDuration = durationPreference,
                preferredCategory = favoriteCategory,
                excludeIds = avoidRecentIds
            )
            
            routineCache[cacheKey] = routine
            _todaysRoutine.value = routine
            
            return routine
            
        } finally {
            _isGenerating.value = false
        }
    }
    
    private fun createRoutine(
        userId: String,
        date: String,
        targetDuration: DurationPreference,
        preferredCategory: TechniqueCategory? = null,
        excludeIds: List<String> = emptyList()
    ): RoutineOfDay {
        val allTechniques = TechniquesRepository.getAllTechniques()
            .filter { !excludeIds.contains(it.id) }
        
        val availableTechniques = if (preferredCategory != null) {
            // 70% preferred category, 30% mixed
            val preferred = allTechniques.filter { it.category == preferredCategory }
            val others = allTechniques.filter { it.category != preferredCategory }
            
            preferred.sortedByDescending { it.popularity }.take(2) +
            others.sortedByDescending { it.popularity }.take(1)
        } else {
            // Balanced selection across categories
            val categories = allTechniques.map { it.category }.distinct()
            categories.mapNotNull { category ->
                allTechniques
                    .filter { it.category == category }
                    .maxByOrNull { it.popularity }
            }.take(3)
        }
        
        var currentMinutes = 0
        val blocks = mutableListOf<RoutineBlock>()
        var order = 0
        
        // Add techniques until we reach target duration
        for (technique in availableTechniques) {
            if (currentMinutes >= targetDuration.maxMinutes) break
            
            val blockDuration = minOf(
                technique.durationMinutesStart,
                targetDuration.maxMinutes - currentMinutes
            )
            
            if (blockDuration > 0) {
                blocks.add(
                    RoutineBlock(
                        techniqueId = technique.id,
                        techniqueName = technique.name,
                        durationMinutes = blockDuration,
                        order = order++,
                        category = technique.category
                    )
                )
                currentMinutes += blockDuration
            }
        }
        
        // Ensure minimum duration
        if (currentMinutes < targetDuration.minMinutes && availableTechniques.isNotEmpty()) {
            val lastBlock = blocks.lastOrNull()
            if (lastBlock != null) {
                val additionalMinutes = targetDuration.minMinutes - currentMinutes
                val updatedBlock = lastBlock.copy(
                    durationMinutes = lastBlock.durationMinutes + additionalMinutes
                )
                blocks[blocks.size - 1] = updatedBlock
                currentMinutes = targetDuration.minMinutes
            }
        }
        
        return RoutineOfDay(
            id = UUID.randomUUID().toString(),
            userId = userId,
            date = date,
            blocks = blocks,
            totalMinutes = currentMinutes
        )
    }
    
    suspend fun markBlockCompleted(routineId: String, blockOrder: Int) {
        val currentRoutine = _todaysRoutine.value ?: return
        if (currentRoutine.id != routineId) return
        
        val updatedBlocks = currentRoutine.blocks.map { block ->
            if (block.order == blockOrder) {
                block.copy(completed = true, completedAt = System.currentTimeMillis())
            } else block
        }
        
        val allCompleted = updatedBlocks.all { it.completed }
        val updatedRoutine = currentRoutine.copy(
            blocks = updatedBlocks,
            completed = allCompleted,
            completedAt = if (allCompleted) System.currentTimeMillis() else null
        )
        
        _todaysRoutine.value = updatedRoutine
        
        // Update cache
        val cacheKey = "${updatedRoutine.userId}_${updatedRoutine.date}"
        routineCache[cacheKey] = updatedRoutine
    }
    
    fun getRoutineProgress(): Float {
        val routine = _todaysRoutine.value ?: return 0f
        if (routine.blocks.isEmpty()) return 0f
        
        val completedBlocks = routine.blocks.count { it.completed }
        return completedBlocks.toFloat() / routine.blocks.size
    }
    
    fun getCompletedMinutes(): Int {
        val routine = _todaysRoutine.value ?: return 0
        return routine.blocks.filter { it.completed }.sumOf { it.durationMinutes }
    }
    
    fun clearCache() {
        routineCache.clear()
        _todaysRoutine.value = null
    }
}