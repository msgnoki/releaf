package com.releaf.app.data.model.firebase

import kotlinx.serialization.Serializable

@Serializable
data class WeeklyProgress(
    val userId: String = "",
    val weekStartDate: String = "", // ISO date string (YYYY-MM-DD)
    val dailySessions: List<Int> = listOf(0, 0, 0, 0, 0, 0, 0), // 7 days, Monday to Sunday
    val dailyMinutes: List<Int> = listOf(0, 0, 0, 0, 0, 0, 0), // 7 days, Monday to Sunday
    val updatedAt: Long = System.currentTimeMillis()
)