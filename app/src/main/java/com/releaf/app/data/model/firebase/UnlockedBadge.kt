package com.releaf.app.data.model.firebase

import kotlinx.serialization.Serializable

@Serializable
data class UnlockedBadge(
    val userId: String = "",
    val badgeId: String = "",
    val unlockedAt: Long = System.currentTimeMillis()
)