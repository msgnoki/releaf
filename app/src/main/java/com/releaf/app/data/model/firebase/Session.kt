package com.releaf.app.data.model.firebase

import kotlinx.serialization.Serializable

@Serializable
data class Session(
    val id: String = "",
    val userId: String = "",
    val techniqueId: String = "",
    val startTime: Long = System.currentTimeMillis(),
    val endTime: Long = System.currentTimeMillis(),
    val duration: Int = 0, // in minutes
    val completed: Boolean = false,
    val moodBefore: Int = 5, // 1-10 scale
    val moodAfter: Int = 5, // 1-10 scale
    val notes: String? = null
)