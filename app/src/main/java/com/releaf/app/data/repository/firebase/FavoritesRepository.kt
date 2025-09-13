package com.releaf.app.data.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.releaf.app.data.model.firebase.FavoriteTechnique
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FavoritesRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val favoritesCollection = firestore.collection("favoriteTechniques")

    suspend fun addToFavorites(userId: String, techniqueId: String): Result<Unit> {
        return try {
            val favorite = FavoriteTechnique(
                userId = userId,
                techniqueId = techniqueId,
                updatedAt = System.currentTimeMillis()
            )
            
            val docId = "${userId}_$techniqueId"
            favoritesCollection.document(docId).set(favorite).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeFromFavorites(userId: String, techniqueId: String): Result<Unit> {
        return try {
            val docId = "${userId}_$techniqueId"
            favoritesCollection.document(docId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserFavorites(userId: String): Result<List<String>> {
        return try {
            val documents = favoritesCollection
                .whereEqualTo("userId", userId)
                .get()
                .await()
            
            val techniqueIds = documents.mapNotNull { 
                it.toObject<FavoriteTechnique>().techniqueId 
            }
            Result.success(techniqueIds)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserFavoritesFlow(userId: String): Flow<List<String>> = callbackFlow {
        val listener = favoritesCollection
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                
                val techniqueIds = snapshot?.mapNotNull { 
                    it.toObject<FavoriteTechnique>().techniqueId 
                } ?: emptyList()
                
                trySend(techniqueIds)
            }
        
        awaitClose { listener.remove() }
    }

    suspend fun isFavorite(userId: String, techniqueId: String): Result<Boolean> {
        return try {
            val docId = "${userId}_$techniqueId"
            val document = favoritesCollection.document(docId).get().await()
            Result.success(document.exists())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun toggleFavorite(userId: String, techniqueId: String): Result<Boolean> {
        return try {
            val isFavoriteResult = isFavorite(userId, techniqueId)
            if (isFavoriteResult.isFailure) {
                return Result.failure(isFavoriteResult.exceptionOrNull()!!)
            }
            
            val isCurrentlyFavorite = isFavoriteResult.getOrNull() ?: false
            
            if (isCurrentlyFavorite) {
                removeFromFavorites(userId, techniqueId)
                Result.success(false)
            } else {
                addToFavorites(userId, techniqueId)
                Result.success(true)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getFavoriteStats(userId: String): Result<FavoriteStats> {
        return try {
            val favoritesResult = getUserFavorites(userId)
            if (favoritesResult.isFailure) {
                return Result.failure(favoritesResult.exceptionOrNull()!!)
            }
            
            val favorites = favoritesResult.getOrNull() ?: emptyList()
            val stats = FavoriteStats(
                totalFavorites = favorites.size,
                favoriteIds = favorites
            )
            
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class FavoriteStats(
    val totalFavorites: Int,
    val favoriteIds: List<String>
)