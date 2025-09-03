package com.example.myapplication.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "user_progress")
@Serializable
data class UserProgress(
    @PrimaryKey
    val userId: String,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val totalSessions: Int = 0,
    val totalMinutes: Int = 0,
    val sessionsThisWeek: Int = 0,
    val minutesThisWeek: Int = 0,
    val sessionsThisMonth: Int = 0,
    val minutesThisMonth: Int = 0,
    val lastSessionDate: String? = null, // LocalDate as String
    val favoriteCategory: TechniqueCategory? = null,
    val favoriteDuration: DurationPreference = DurationPreference.MEDIUM,
    val averageMoodImprovement: Float = 0f,
    val level: UserLevel = UserLevel.BEGINNER,
    val experiencePoints: Int = 0,
    val badges: List<String> = emptyList(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class DurationPreference(val displayName: String, val minMinutes: Int, val maxMinutes: Int) {
    QUICK("Rapide", 1, 3),
    SHORT("Court", 3, 7), 
    MEDIUM("Moyen", 7, 15),
    LONG("Long", 15, 30),
    EXTENDED("Étendu", 30, 60)
}

enum class UserLevel(val displayName: String, val minXP: Int, val color: String) {
    BEGINNER("Débutant", 0, "#4CAF50"),
    APPRENTICE("Apprenti", 100, "#2196F3"), 
    PRACTITIONER("Pratiquant", 300, "#FF9800"),
    EXPERT("Expert", 600, "#9C27B0"),
    MASTER("Maître", 1000, "#F44336"),
    ZEN_MASTER("Maître Zen", 2000, "#FFD700")
}

@Serializable
data class Badge(
    val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val category: BadgeCategory,
    val rarity: BadgeRarity,
    val unlockedAt: Long? = null
)

enum class BadgeCategory {
    STREAK, DURATION, FREQUENCY, TECHNIQUE_MASTERY, MOOD_IMPROVEMENT, SPECIAL
}

enum class BadgeRarity(val color: String) {
    COMMON("#9E9E9E"),
    UNCOMMON("#4CAF50"), 
    RARE("#2196F3"),
    EPIC("#9C27B0"),
    LEGENDARY("#FF9800")
}