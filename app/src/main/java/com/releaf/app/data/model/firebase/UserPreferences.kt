package com.releaf.app.data.model.firebase

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val userId: String = "",
    val language: String = "fr", // fr, en, es, zh
    val notificationsEnabled: Boolean = true,
    val preferredDuration: String = "MEDIUM", // QUICK, SHORT, MEDIUM, LONG, EXTENDED
    val updatedAt: Long = System.currentTimeMillis(),
    val reminderTime: String? = null
)

enum class Language(val code: String, val displayName: String) {
    FRENCH("fr", "Français"),
    ENGLISH("en", "English"),
    SPANISH("es", "Español"),
    CHINESE("zh", "中文")
}

enum class PreferredDuration(val code: String, val displayName: String, val minMinutes: Int, val maxMinutes: Int) {
    QUICK("QUICK", "Rapide", 1, 3),
    SHORT("SHORT", "Court", 3, 7),
    MEDIUM("MEDIUM", "Moyen", 7, 15),
    LONG("LONG", "Long", 15, 30),
    EXTENDED("EXTENDED", "Étendu", 30, 60)
}