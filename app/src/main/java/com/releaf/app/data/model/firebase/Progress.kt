package com.releaf.app.data.model.firebase

import kotlinx.serialization.Serializable

@Serializable
data class Progress(
    val userId: String = "",
    val totalSessions: Int = 0,
    val totalMinutes: Int = 0,
    val currentStreak: Int = 0,
    val longestStreak: Int = 0,
    val sessionsThisWeek: Int = 0,
    val minutesThisWeek: Int = 0,
    val averageMoodImprovement: Float = 0f,
    val lastSessionDate: String = "", // ISO date string
    val weekStartDate: String = "", // ISO date string
    val updatedAt: Long = System.currentTimeMillis()
)