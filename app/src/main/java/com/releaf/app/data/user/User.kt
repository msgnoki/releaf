package com.releaf.app.data.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.Date

/**
 * Modèle de données pour l'utilisateur
 */
@Entity(tableName = "users")
@Serializable
data class User(
    @PrimaryKey
    val uid: String = "",
    val email: String = "",
    val displayName: String = "",
    val photoUrl: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val lastLoginAt: Long = System.currentTimeMillis(),
    
    // Préférences utilisateur
    val preferredTechniques: List<String> = emptyList(),
    val anxietyLevel: AnxietyLevel = AnxietyLevel.MODERATE,
    val dailyGoalMinutes: Int = 10,
    val reminderEnabled: Boolean = true,
    val reminderTime: String = "20:00", // Format HH:mm
    
    // Statistiques
    val totalSessionsCompleted: Int = 0,
    val totalMinutesSpent: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val lastSessionDate: Long? = null
)

/**
 * Niveaux d'anxiété de l'utilisateur
 */
enum class AnxietyLevel(val displayName: String, val description: String) {
    LOW("Faible", "Anxiété occasionnelle et gérable"),
    MODERATE("Modérée", "Anxiété régulière nécessitant des techniques"),
    HIGH("Élevée", "Anxiété fréquente impactant le quotidien"),
    SEVERE("Sévère", "Anxiété intense nécessitant un suivi médical")
}

/**
 * Préférences utilisateur pour l'interface
 */
@Entity(tableName = "user_preferences")
data class UserPreferences(
    @PrimaryKey
    val userId: String,
    val darkModeEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val hapticFeedbackEnabled: Boolean = true,
    val soundEnabled: Boolean = true,
    val biometricAuthEnabled: Boolean = false,
    val language: String = "fr",
    val theme: String = "default"
)