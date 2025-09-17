package com.releaf.app.services

import com.google.firebase.auth.FirebaseAuth
import com.releaf.app.data.model.firebase.*
import com.releaf.app.data.repository.firebase.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Service pour les statistiques utilisateur avec données Firebase réelles
 */
class StatisticsService {
    
    private val auth = FirebaseAuth.getInstance()
    private val progressRepository = ProgressRepository()
    private val sessionRepository = SessionRepository()
    private val badgeRepository = BadgeRepository()
    private val weeklyProgressRepository = WeeklyProgressRepository()
    
    /**
     * Statistiques complètes de l'utilisateur
     */
    data class UserStatistics(
        val totalSessions: Int = 0,
        val totalMinutes: Int = 0,
        val currentStreak: Int = 0,
        val longestStreak: Int = 0,
        val averageMoodImprovement: Float = 0f,
        val badgesCount: Int = 0,
        val weeklyMinutes: Int = 0,
        val monthlyMinutes: Int = 0,
        val averageSessionDuration: Int = 0,
        val level: Int = 1,
        val currentXp: Int = 0,
        val completionRate: Float = 0f,
        val favoriteCategory: String = "RESPIRATION",
        val recentSessions: List<Session> = emptyList()
    )
    
    /**
     * Flow des statistiques utilisateur en temps réel
     */
    fun getStatisticsFlow(): Flow<UserStatistics?> {
        val userId = auth.currentUser?.uid ?: return flowOf(null)
        
        return combine(
            progressRepository.getProgressFlow(userId),
            sessionRepository.getUserSessionsFlow(userId),
            badgeRepository.getUserUnlockedBadgesFlow(userId),
            weeklyProgressRepository.getCurrentWeekProgressFlow(userId)
        ) { progress, sessions, badges, weeklyProgress ->
            calculateStatistics(progress, sessions, badges, weeklyProgress)
        }
    }
    
    /**
     * Obtient les statistiques une seule fois
     */
    suspend fun getStatistics(): Result<UserStatistics> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User not authenticated"))
        
        return try {
            val progress = progressRepository.getProgress(userId).getOrNull()
            val sessions = sessionRepository.getUserSessions(userId, limit = 100).getOrNull() ?: emptyList()
            val badges = badgeRepository.getUserUnlockedBadges(userId).getOrNull() ?: emptyList()
            val weeklyProgress = weeklyProgressRepository.getCurrentWeekProgress(userId).getOrNull()
            
            val statistics = calculateStatistics(progress, sessions, badges, weeklyProgress)
            Result.success(statistics)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Calcule les statistiques à partir des données Firebase
     */
    private fun calculateStatistics(
        progress: Progress?,
        sessions: List<Session>,
        badges: List<UnlockedBadge>,
        weeklyProgress: WeeklyProgress?
    ): UserStatistics {
        
        // Sessions récentes (30 derniers jours)
        val recentSessions = sessions.filter { isWithinDays(it.createdAt, 30) }
        val thisWeekSessions = sessions.filter { isThisWeek(it.createdAt) }
        val thisMonthSessions = sessions.filter { isThisMonth(it.createdAt) }
        
        // Calculs des statistiques
        val totalSessions = sessions.size
        val totalMinutes = sessions.sumOf { it.duration }
        val weeklyMinutes = thisWeekSessions.sumOf { it.duration }
        val monthlyMinutes = thisMonthSessions.sumOf { it.duration }
        
        val averageSessionDuration = if (totalSessions > 0) totalMinutes / totalSessions else 0
        
        // Amélioration moyenne de l'humeur
        val moodImprovements = sessions.map { it.moodAfter - it.moodBefore }
        val averageMoodImprovement = if (moodImprovements.isNotEmpty()) {
            moodImprovements.average().toFloat()
        } else 0f
        
        // Streak actuel (basé sur les dates des sessions)
        val currentStreak = calculateCurrentStreak(sessions)
        val longestStreak = progress?.longestStreak ?: currentStreak
        
        // Taux de completion (sessions complétées vs total)
        val completedSessions = sessions.count { it.completed }
        val completionRate = if (totalSessions > 0) (completedSessions.toFloat() / totalSessions) * 100 else 0f
        
        // Catégorie favorite
        val favoriteCategory = sessions.groupBy { it.category }
            .maxByOrNull { it.value.size }?.key ?: "RESPIRATION"
        
        // Niveau et XP (basé sur les minutes totales)
        val level = calculateLevel(totalMinutes)
        val currentXp = calculateCurrentXp(totalMinutes)
        
        return UserStatistics(
            totalSessions = totalSessions,
            totalMinutes = totalMinutes,
            currentStreak = currentStreak,
            longestStreak = longestStreak,
            averageMoodImprovement = averageMoodImprovement,
            badgesCount = badges.size,
            weeklyMinutes = weeklyMinutes,
            monthlyMinutes = monthlyMinutes,
            averageSessionDuration = averageSessionDuration,
            level = level,
            currentXp = currentXp,
            completionRate = completionRate,
            favoriteCategory = favoriteCategory,
            recentSessions = recentSessions.take(10)
        )
    }
    
    /**
     * Calcule le streak actuel basé sur les sessions
     */
    private fun calculateCurrentStreak(sessions: List<Session>): Int {
        if (sessions.isEmpty()) return 0
        
        // Grouper par date et trier par date décroissante
        val sessionsByDate = sessions
            .map { LocalDate.ofEpochDay(it.createdAt / (24 * 60 * 60 * 1000)) }
            .distinct()
            .sortedDescending()
        
        if (sessionsByDate.isEmpty()) return 0
        
        // Vérifier si la date la plus récente est aujourd'hui ou hier
        val today = LocalDate.now()
        val yesterday = today.minusDays(1)
        val mostRecent = sessionsByDate.first()
        
        if (mostRecent != today && mostRecent != yesterday) {
            return 0 // Streak cassé
        }
        
        // Compter les jours consécutifs
        var streak = 0
        var currentDate = if (mostRecent == today) today else yesterday
        
        for (sessionDate in sessionsByDate) {
            if (sessionDate == currentDate) {
                streak++
                currentDate = currentDate.minusDays(1)
            } else if (sessionDate == currentDate.plusDays(1)) {
                // Continue, c'est le même jour
                continue
            } else {
                break // Streak cassé
            }
        }
        
        return streak
    }
    
    /**
     * Calcule le niveau basé sur les minutes totales
     */
    private fun calculateLevel(totalMinutes: Int): Int {
        return (totalMinutes / 100) + 1 // 1 niveau par 100 minutes
    }
    
    /**
     * Calcule l'XP actuel
     */
    private fun calculateCurrentXp(totalMinutes: Int): Int {
        return (totalMinutes % 100) * 10 // 10 XP par minute, reset tous les 100 minutes
    }
    
    /**
     * Vérifie si un timestamp est dans les N derniers jours
     */
    private fun isWithinDays(timestamp: Long, days: Int): Boolean {
        val sessionDate = LocalDate.ofEpochDay(timestamp / (24 * 60 * 60 * 1000))
        val cutoffDate = LocalDate.now().minusDays(days.toLong())
        return sessionDate.isAfter(cutoffDate) || sessionDate.isEqual(cutoffDate)
    }
    
    /**
     * Vérifie si un timestamp est dans la semaine actuelle
     */
    private fun isThisWeek(timestamp: Long): Boolean {
        val sessionDate = LocalDate.ofEpochDay(timestamp / (24 * 60 * 60 * 1000))
        val today = LocalDate.now()
        val weekStart = today.minusDays(today.dayOfWeek.value - 1L)
        return sessionDate.isAfter(weekStart.minusDays(1)) || sessionDate.isEqual(weekStart)
    }
    
    /**
     * Vérifie si un timestamp est dans le mois actuel
     */
    private fun isThisMonth(timestamp: Long): Boolean {
        val sessionDate = LocalDate.ofEpochDay(timestamp / (24 * 60 * 60 * 1000))
        val today = LocalDate.now()
        return sessionDate.month == today.month && sessionDate.year == today.year
    }
    
    /**
     * Enregistre une nouvelle session
     */
    suspend fun recordSession(
        techniqueId: String,
        category: String,
        duration: Int,
        moodBefore: Int,
        moodAfter: Int,
        notes: String = ""
    ): Result<String> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User not authenticated"))
        
        val session = com.releaf.app.data.model.firebase.Session(
            userId = userId,
            techniqueId = techniqueId,
            category = category,
            duration = duration,
            moodBefore = moodBefore,
            moodAfter = moodAfter,
            notes = notes,
            completed = true,
            startTime = System.currentTimeMillis(),
            endTime = System.currentTimeMillis() + (duration * 60 * 1000),
            createdAt = System.currentTimeMillis()
        )
        
        return sessionRepository.createSession(session)
    }
    
    /**
     * Crée des données de test pour démonstration
     * À utiliser uniquement en mode développement
     */
    suspend fun createTestData(): Result<Unit> {
        val userId = auth.currentUser?.uid ?: return Result.failure(Exception("User not authenticated"))
        
        return try {
            // Créer quelques sessions de test sur les derniers jours
            val categories = listOf("RESPIRATION", "MEDITATION", "RELAXATION", "MINDFULNESS")
            val techniques = listOf("breathing", "meditation", "body-scan", "grounding")
            
            // Sessions des 14 derniers jours
            repeat(15) { dayOffset ->
                val sessionTime = System.currentTimeMillis() - (dayOffset * 24 * 60 * 60 * 1000)
                
                // 1-3 sessions par jour
                val sessionsPerDay = (1..3).random()
                repeat(sessionsPerDay) { sessionIndex ->
                    val sessionDuration = (5..30).random() // 5-30 minutes
                    val moodBefore = (3..7).random() // Humeur avant
                    val moodAfter = minOf(10, moodBefore + (1..4).random()) // Amélioration
                    
                    val session = Session(
                        userId = userId,
                        techniqueId = techniques.random(),
                        category = categories.random(),
                        duration = sessionDuration,
                        moodBefore = moodBefore,
                        moodAfter = moodAfter,
                        notes = "Session de test automatique",
                        completed = true,
                        startTime = sessionTime,
                        endTime = sessionTime + (sessionDuration * 60 * 1000),
                        createdAt = sessionTime
                    )
                    
                    sessionRepository.createSession(session)
                }
            }
            
            // Créer un profil de progression
            val totalMinutes = (15 * 2.5 * 15).toInt() // Estimation des minutes totales
            val progress = Progress(
                userId = userId,
                totalSessions = 30,
                totalMinutes = totalMinutes,
                currentStreak = 7,
                longestStreak = 12,
                sessionsThisWeek = 8,
                minutesThisWeek = 120,
                averageMoodImprovement = 2.3f,
                lastSessionDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                weekStartDate = LocalDate.now().minusDays(LocalDate.now().dayOfWeek.value - 1L).format(DateTimeFormatter.ISO_LOCAL_DATE),
                updatedAt = System.currentTimeMillis()
            )
            
            progressRepository.createProgress(userId, progress)
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}