package com.releaf.app.data.database

import androidx.room.*
import com.releaf.app.data.user.TechniqueStats
import kotlinx.coroutines.flow.Flow

@Dao
interface TechniqueStatsDao {
    
    @Query("SELECT * FROM technique_stats WHERE userId = :userId ORDER BY totalSessions DESC")
    fun getUserTechniqueStats(userId: String): Flow<List<TechniqueStats>>
    
    @Query("SELECT * FROM technique_stats WHERE userId = :userId AND techniqueId = :techniqueId")
    suspend fun getTechniqueStats(userId: String, techniqueId: String): TechniqueStats?
    
    @Query("SELECT * FROM technique_stats WHERE userId = :userId AND techniqueId = :techniqueId")
    fun getTechniqueStatsFlow(userId: String, techniqueId: String): Flow<TechniqueStats?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTechniqueStats(stats: TechniqueStats)
    
    @Update
    suspend fun updateTechniqueStats(stats: TechniqueStats)
    
    @Delete
    suspend fun deleteTechniqueStats(stats: TechniqueStats)
    
    @Query("DELETE FROM technique_stats WHERE userId = :userId")
    suspend fun deleteUserTechniqueStats(userId: String)
    
    @Query("SELECT * FROM technique_stats WHERE userId = :userId ORDER BY lastUsedAt DESC LIMIT 3")
    suspend fun getRecentlyUsedTechniques(userId: String): List<TechniqueStats>
    
    @Query("SELECT * FROM technique_stats WHERE userId = :userId ORDER BY totalMinutes DESC LIMIT 1")
    suspend fun getMostUsedTechnique(userId: String): TechniqueStats?
}