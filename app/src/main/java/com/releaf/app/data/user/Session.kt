package com.releaf.app.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Modèle pour une session de technique de relaxation
 */
@Entity(tableName = "sessions")
@Serializable
data class Session(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val techniqueId: String,
    val techniqueName: String,
    val startTime: Long,
    val endTime: Long,
    val durationSeconds: Int,
    val completed: Boolean = true,
    val moodBefore: MoodLevel? = null,
    val moodAfter: MoodLevel? = null,
    val notes: String = "",
    val rating: Int? = null, // 1-5 étoiles
    val createdAt: Long = System.currentTimeMillis(),
    
    // Données spécifiques par technique
    val techniqueData: Map<String, String> = emptyMap() // JSON-like data
)

/**
 * Niveaux d'humeur avant et après les sessions
 */
enum class MoodLevel(val value: Int, val displayName: String, val emoji: String) {
    VERY_BAD(1, "Très mal", "😰"),
    BAD(2, "Mal", "😔"),
    NEUTRAL(3, "Neutre", "😐"),
    GOOD(4, "Bien", "😊"),
    VERY_GOOD(5, "Très bien", "😄")
}

/**
 * Statistiques agrégées par technique
 */
@Entity(tableName = "technique_stats")
data class TechniqueStats(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val techniqueId: String,
    val totalSessions: Int = 0,
    val totalMinutes: Int = 0,
    val averageRating: Float = 0f,
    val lastUsedAt: Long? = null,
    val favoriteTime: String? = null, // Moment de la journée préféré
    val averageMoodImprovement: Float = 0f,
    val currentStreak: Int = 0,
    val bestStreak: Int = 0
)

/**
 * Objectifs utilisateur
 */
@Entity(tableName = "user_goals")
data class UserGoal(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val type: GoalType,
    val targetValue: Int,
    val currentValue: Int = 0,
    val startDate: Long,
    val targetDate: Long,
    val isActive: Boolean = true,
    val isAchieved: Boolean = false,
    val achievedAt: Long? = null
)

enum class GoalType(val displayName: String, val unit: String) {
    DAILY_MINUTES("Minutes quotidiennes", "min"),
    WEEKLY_SESSIONS("Sessions hebdomadaires", "sessions"),
    STREAK_DAYS("Jours consécutifs", "jours"),
    TECHNIQUE_MASTERY("Maîtrise d'une technique", "sessions"),
    MOOD_IMPROVEMENT("Amélioration de l'humeur", "points")
}