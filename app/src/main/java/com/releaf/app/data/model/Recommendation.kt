package com.releaf.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Entity(tableName = "recommendations")
@Serializable
data class Recommendation(
    @PrimaryKey
    val id: String,
    val userId: String,
    val techniqueId: String,
    val reason: RecommendationReason,
    val reasonText: String,
    val score: Float,
    val category: TechniqueCategory,
    val priority: RecommendationPriority = RecommendationPriority.NORMAL,
    val validUntil: Long, // TTL for cache invalidation
    val createdAt: Long = System.currentTimeMillis()
)

enum class RecommendationReason(val displayText: String) {
    MOST_USED("Basé sur vos techniques favorites"),
    RECENT_PATTERN("Basé sur votre usage récent"),
    TIME_OF_DAY("Idéal pour ce moment de la journée"),
    ANXIETY_LEVEL("Adapté à votre niveau d'anxiété"),
    DURATION_PREFERENCE("Correspond à vos durées préférées"),
    CATEGORY_PREFERENCE("Dans vos catégories favorites"),
    STREAK_CONTINUATION("Pour continuer votre série"),
    NEW_USER("Recommandé pour débuter"),
    COMPLEMENTARY("Complète bien votre dernière session")
}

enum class RecommendationPriority {
    LOW,
    NORMAL, 
    HIGH,
    URGENT // For crisis situations
}

@Serializable
data class RecommendationWithTechnique(
    val recommendation: Recommendation,
    val techniqueId: String,
    val techniqueName: String,
    val duration: String,
    val shortDescription: String,
    val icon: String,
    val iconColor: String
)