package com.releaf.app.data.model.firebase

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class BadgeDefinition(
    val badgeId: String = UUID.randomUUID().toString(),
    val name: String = "",
    val description: String = "",
    val rarity: String = "", // COMMON, UNCOMMON, RARE, EPIC, LEGENDARY
    val category: String = "", // STREAK, DURATION, FREQUENCY, TECHNIQUE_MASTERY, MOOD_IMPROVEMENT, SPECIAL
    val icon: String? = null
)

enum class BadgeRarity {
    COMMON, UNCOMMON, RARE, EPIC, LEGENDARY
}

enum class BadgeCategory {
    STREAK, DURATION, FREQUENCY, TECHNIQUE_MASTERY, MOOD_IMPROVEMENT, SPECIAL
}