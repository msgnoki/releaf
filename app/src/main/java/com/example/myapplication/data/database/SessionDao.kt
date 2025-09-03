package com.example.myapplication.data.database

import androidx.room.*
import com.example.myapplication.data.user.Session
import kotlinx.coroutines.flow.Flow

@Dao
interface SessionDao {
    
    @Query("SELECT * FROM sessions WHERE userId = :userId ORDER BY createdAt DESC")
    fun getUserSessions(userId: String): Flow<List<Session>>
    
    @Query("SELECT * FROM sessions WHERE userId = :userId AND techniqueId = :techniqueId ORDER BY createdAt DESC")
    fun getUserSessionsForTechnique(userId: String, techniqueId: String): Flow<List<Session>>
    
    @Query("SELECT * FROM sessions WHERE id = :sessionId")
    suspend fun getSession(sessionId: String): Session?
    
    @Insert
    suspend fun insertSession(session: Session)
    
    @Update
    suspend fun updateSession(session: Session)
    
    @Delete
    suspend fun deleteSession(session: Session)
    
    @Query("DELETE FROM sessions WHERE userId = :userId")
    suspend fun deleteUserSessions(userId: String)
    
    @Query("SELECT COUNT(*) FROM sessions WHERE userId = :userId")
    suspend fun getUserSessionCount(userId: String): Int
    
    @Query("SELECT SUM(durationSeconds) FROM sessions WHERE userId = :userId")
    suspend fun getUserTotalDuration(userId: String): Int?
    
    @Query("SELECT * FROM sessions WHERE userId = :userId AND DATE(createdAt/1000, 'unixepoch') = DATE('now') ORDER BY createdAt DESC")
    suspend fun getTodaySessions(userId: String): List<Session>
    
    @Query("SELECT DISTINCT DATE(createdAt/1000, 'unixepoch') FROM sessions WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getUserSessionDates(userId: String): List<String>
}