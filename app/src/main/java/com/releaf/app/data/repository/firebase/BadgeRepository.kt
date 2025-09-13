package com.releaf.app.data.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.releaf.app.data.model.firebase.BadgeDefinition
import com.releaf.app.data.model.firebase.UnlockedBadge
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class BadgeRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val badgeDefinitionsCollection = firestore.collection("badgeDefinitions")
    private val unlockedBadgesCollection = firestore.collection("unlockedBadges")

    // Badge Definitions
    suspend fun createBadgeDefinition(badge: BadgeDefinition): Result<Unit> {
        return try {
            badgeDefinitionsCollection.document(badge.badgeId).set(badge).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllBadgeDefinitions(): Result<List<BadgeDefinition>> {
        return try {
            val documents = badgeDefinitionsCollection.get().await()
            val badges = documents.mapNotNull { it.toObject<BadgeDefinition>() }
            Result.success(badges)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBadgeDefinition(badgeId: String): Result<BadgeDefinition?> {
        return try {
            val document = badgeDefinitionsCollection.document(badgeId).get().await()
            val badge = document.toObject<BadgeDefinition>()
            Result.success(badge)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Unlocked Badges
    suspend fun unlockBadge(userId: String, badgeId: String): Result<Unit> {
        return try {
            val unlockedBadge = UnlockedBadge(
                userId = userId,
                badgeId = badgeId,
                unlockedAt = System.currentTimeMillis()
            )
            
            val docId = "${userId}_$badgeId"
            unlockedBadgesCollection.document(docId).set(unlockedBadge).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserUnlockedBadges(userId: String): Result<List<UnlockedBadge>> {
        return try {
            val documents = unlockedBadgesCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            val unlockedBadges = documents.mapNotNull { it.toObject<UnlockedBadge>() }
            Result.success(unlockedBadges)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserUnlockedBadgesFlow(userId: String): Flow<List<UnlockedBadge>> = callbackFlow {
        val listener = unlockedBadgesCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val unlockedBadges = snapshot?.mapNotNull { it.toObject<UnlockedBadge>() } ?: emptyList()
                trySend(unlockedBadges)
            }
        
        awaitClose { listener.remove() }
    }

    suspend fun isUserBadgeUnlocked(userId: String, badgeId: String): Result<Boolean> {
        return try {
            val docId = "${userId}_$badgeId"
            val document = unlockedBadgesCollection.document(docId).get().await()
            Result.success(document.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserBadgeStats(userId: String): Result<BadgeStats> {
        return try {
            val unlockedResult = getUserUnlockedBadges(userId)
            if (unlockedResult.isFailure) {
                return Result.failure(unlockedResult.exceptionOrNull()!!)
            }
            
            val allBadgesResult = getAllBadgeDefinitions()
            if (allBadgesResult.isFailure) {
                return Result.failure(allBadgesResult.exceptionOrNull()!!)
            }
            
            val unlockedBadges = unlockedResult.getOrNull() ?: emptyList()
            val allBadges = allBadgesResult.getOrNull() ?: emptyList()
            
            val stats = BadgeStats(
                totalUnlocked = unlockedBadges.size,
                totalAvailable = allBadges.size,
                recentlyUnlocked = unlockedBadges
                    .sortedByDescending { it.unlockedAt }
                    .take(3)
            )
            
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun initializeDefaultBadges(): Result<Unit> {
        return try {
            val defaultBadges = getDefaultBadges()
            
            for (badge in defaultBadges) {
                badgeDefinitionsCollection.document(badge.badgeId).set(badge).await()
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getDefaultBadges(): List<BadgeDefinition> {
        return listOf(
            BadgeDefinition("first_session", "Première Session", "Complétez votre première session de méditation", "COMMON", "FREQUENCY"),
            BadgeDefinition("streak_3", "Série de 3", "Méditez 3 jours consécutifs", "COMMON", "STREAK"),
            BadgeDefinition("streak_7", "Une Semaine", "Méditez 7 jours consécutifs", "UNCOMMON", "STREAK"),
            BadgeDefinition("streak_30", "Un Mois", "Méditez 30 jours consécutifs", "RARE", "STREAK"),
            BadgeDefinition("sessions_10", "Dévotion", "Complétez 10 sessions", "UNCOMMON", "FREQUENCY"),
            BadgeDefinition("sessions_50", "Régularité", "Complétez 50 sessions", "RARE", "FREQUENCY"),
            BadgeDefinition("sessions_100", "Centurion", "Complétez 100 sessions", "EPIC", "FREQUENCY"),
            BadgeDefinition("minutes_60", "Une Heure", "Atteignez 60 minutes de méditation", "COMMON", "DURATION"),
            BadgeDefinition("minutes_300", "Cinq Heures", "Atteignez 5 heures de méditation", "UNCOMMON", "DURATION"),
            BadgeDefinition("minutes_1000", "Maître du Temps", "Atteignez 1000 minutes de méditation", "EPIC", "DURATION"),
            BadgeDefinition("mood_improvement", "Guérisseur", "Améliorez votre humeur de 5 points en moyenne", "RARE", "MOOD_IMPROVEMENT")
        )
    }
}

data class BadgeStats(
    val totalUnlocked: Int,
    val totalAvailable: Int,
    val recentlyUnlocked: List<UnlockedBadge>
)