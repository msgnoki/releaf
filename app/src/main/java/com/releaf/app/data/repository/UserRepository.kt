package com.releaf.app.data.repository

import com.releaf.app.data.database.AppDatabase
import com.releaf.app.data.user.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
class UserRepository(
    private val database: AppDatabase
) {
    private val userDao = database.userDao()
    private val sessionDao = database.sessionDao()
    private val techniqueStatsDao = database.techniqueStatsDao()
    private val userGoalDao = database.userGoalDao()
    
    // User operations
    suspend fun getUser(uid: String): User? = userDao.getUser(uid)
    
    fun getUserFlow(uid: String): Flow<User?> = userDao.getUserFlow(uid)
    
    suspend fun insertUser(user: User) = userDao.insertUser(user)
    
    suspend fun updateUser(user: User) = userDao.updateUser(user)
    
    suspend fun deleteUser(uid: String) {
        userDao.deleteUserById(uid)
        sessionDao.deleteUserSessions(uid)
        techniqueStatsDao.deleteUserTechniqueStats(uid)
        userGoalDao.deleteUserGoals(uid)
        userDao.deleteUserPreferences(uid)
    }
    
    // User preferences
    suspend fun getUserPreferences(userId: String): UserPreferences? = 
        userDao.getUserPreferences(userId)
    
    fun getUserPreferencesFlow(userId: String): Flow<UserPreferences?> = 
        userDao.getUserPreferencesFlow(userId)
    
    suspend fun updateUserPreferences(preferences: UserPreferences) = 
        userDao.updateUserPreferences(preferences)
    
    suspend fun createDefaultUserPreferences(userId: String) {
        val defaultPreferences = UserPreferences(userId = userId)
        userDao.insertUserPreferences(defaultPreferences)
    }
    
    // Session operations
    suspend fun createSession(session: Session) {
        sessionDao.insertSession(session)
        updateUserStats(session)
        updateTechniqueStats(session)
        checkAndUpdateGoals(session)
    }
    
    fun getUserSessions(userId: String): Flow<List<Session>> = 
        sessionDao.getUserSessions(userId)
    
    fun getUserSessionsForTechnique(userId: String, techniqueId: String): Flow<List<Session>> = 
        sessionDao.getUserSessionsForTechnique(userId, techniqueId)
    
    suspend fun getUserSessionCount(userId: String): Int = 
        sessionDao.getUserSessionCount(userId)
    
    suspend fun getUserTotalDuration(userId: String): Int = 
        sessionDao.getUserTotalDuration(userId) ?: 0
    
    suspend fun getTodaySessions(userId: String): List<Session> = 
        sessionDao.getTodaySessions(userId)
    
    // Technique stats
    fun getUserTechniqueStats(userId: String): Flow<List<TechniqueStats>> = 
        techniqueStatsDao.getUserTechniqueStats(userId)
    
    suspend fun getTechniqueStats(userId: String, techniqueId: String): TechniqueStats? = 
        techniqueStatsDao.getTechniqueStats(userId, techniqueId)
    
    suspend fun getRecentlyUsedTechniques(userId: String): List<TechniqueStats> = 
        techniqueStatsDao.getRecentlyUsedTechniques(userId)
    
    // Goals
    fun getActiveUserGoals(userId: String): Flow<List<UserGoal>> = 
        userGoalDao.getActiveUserGoals(userId)
    
    suspend fun createGoal(goal: UserGoal) = 
        userGoalDao.insertGoal(goal)
    
    suspend fun updateGoal(goal: UserGoal) = 
        userGoalDao.updateGoal(goal)
    
    suspend fun markGoalAsAchieved(goalId: String) {
        userGoalDao.markGoalAsAchieved(goalId, System.currentTimeMillis())
    }
    
    // Private helper methods
    private suspend fun updateUserStats(session: Session) {
        val user = userDao.getUser(session.userId) ?: return
        val updatedUser = user.copy(
            totalSessionsCompleted = user.totalSessionsCompleted + 1,
            totalMinutesSpent = user.totalMinutesSpent + (session.durationSeconds / 60),
            lastSessionDate = session.createdAt,
            currentStreak = calculateCurrentStreak(session.userId),
            longestStreak = maxOf(user.longestStreak, calculateCurrentStreak(session.userId))
        )
        userDao.updateUser(updatedUser)
    }
    
    private suspend fun updateTechniqueStats(session: Session) {
        val existingStats = techniqueStatsDao.getTechniqueStats(session.userId, session.techniqueId)
        
        if (existingStats == null) {
            val newStats = TechniqueStats(
                userId = session.userId,
                techniqueId = session.techniqueId,
                totalSessions = 1,
                totalMinutes = session.durationSeconds / 60,
                averageRating = session.rating?.toFloat() ?: 0f,
                lastUsedAt = session.createdAt,
                averageMoodImprovement = calculateMoodImprovement(session)
            )
            techniqueStatsDao.insertTechniqueStats(newStats)
        } else {
            val totalSessions = existingStats.totalSessions + 1
            val totalMinutes = existingStats.totalMinutes + (session.durationSeconds / 60)
            val averageRating = if (session.rating != null) {
                ((existingStats.averageRating * existingStats.totalSessions) + session.rating) / totalSessions
            } else existingStats.averageRating
            
            val updatedStats = existingStats.copy(
                totalSessions = totalSessions,
                totalMinutes = totalMinutes,
                averageRating = averageRating,
                lastUsedAt = session.createdAt,
                averageMoodImprovement = calculateMoodImprovement(session, existingStats)
            )
            techniqueStatsDao.updateTechniqueStats(updatedStats)
        }
    }
    
    private suspend fun calculateCurrentStreak(userId: String): Int {
        val sessionDates = sessionDao.getUserSessionDates(userId)
        if (sessionDates.isEmpty()) return 0
        
        var streak = 1
        for (i in 1 until sessionDates.size) {
            // Logic to calculate consecutive days
            // This is simplified - you'd want more robust date comparison
            streak++
        }
        return streak
    }
    
    private fun calculateMoodImprovement(session: Session, existingStats: TechniqueStats? = null): Float {
        if (session.moodBefore == null || session.moodAfter == null) {
            return existingStats?.averageMoodImprovement ?: 0f
        }
        
        val improvement = session.moodAfter.value - session.moodBefore.value
        return if (existingStats == null) {
            improvement.toFloat()
        } else {
            ((existingStats.averageMoodImprovement * existingStats.totalSessions) + improvement) / (existingStats.totalSessions + 1)
        }
    }
    
    private suspend fun checkAndUpdateGoals(session: Session) {
        val activeGoals = userGoalDao.getActiveUserGoals(session.userId).first()
        
        activeGoals.forEach { goal ->
            when (goal.type) {
                GoalType.DAILY_MINUTES -> {
                    val todayMinutes = getTodayTotalMinutes(session.userId)
                    if (todayMinutes >= goal.targetValue && !goal.isAchieved) {
                        markGoalAsAchieved(goal.id)
                    }
                }
                GoalType.WEEKLY_SESSIONS -> {
                    // Implementation for weekly sessions
                }
                GoalType.STREAK_DAYS -> {
                    val currentStreak = calculateCurrentStreak(session.userId)
                    if (currentStreak >= goal.targetValue && !goal.isAchieved) {
                        markGoalAsAchieved(goal.id)
                    }
                }
                GoalType.TECHNIQUE_MASTERY -> {
                    val techniqueStats = getTechniqueStats(session.userId, session.techniqueId)
                    if (techniqueStats != null && techniqueStats.totalSessions >= goal.targetValue && !goal.isAchieved) {
                        markGoalAsAchieved(goal.id)
                    }
                }
                GoalType.MOOD_IMPROVEMENT -> {
                    // Implementation for mood improvement goals
                }
            }
        }
    }
    
    private suspend fun getTodayTotalMinutes(userId: String): Int {
        return getTodaySessions(userId).sumOf { it.durationSeconds / 60 }
    }
}