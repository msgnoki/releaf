package com.releaf.app.data.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.releaf.app.data.model.firebase.UserPreferences
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class UserPreferencesRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val preferencesCollection = firestore.collection("userPreferences")

    suspend fun createPreferences(userId: String, preferences: UserPreferences): Result<Unit> {
        return try {
            preferencesCollection.document(userId).set(preferences).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getPreferences(userId: String): Result<UserPreferences?> {
        return try {
            val document = preferencesCollection.document(userId).get().await()
            val preferences = document.toObject<UserPreferences>()
            Result.success(preferences)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getPreferencesFlow(userId: String): Flow<UserPreferences?> = callbackFlow {
        val listener = preferencesCollection.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            
            val preferences = snapshot?.toObject<UserPreferences>()
            trySend(preferences)
        }
        
        awaitClose { listener.remove() }
    }

    suspend fun updatePreferences(userId: String, preferences: UserPreferences): Result<Unit> {
        return try {
            val updatedPreferences = preferences.copy(
                updatedAt = System.currentTimeMillis()
            )
            preferencesCollection.document(userId).set(updatedPreferences).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateLanguage(userId: String, language: String): Result<Unit> {
        return try {
            val updates = mapOf(
                "language" to language,
                "updatedAt" to System.currentTimeMillis()
            )
            preferencesCollection.document(userId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateNotifications(userId: String, enabled: Boolean): Result<Unit> {
        return try {
            val updates = mapOf(
                "notificationsEnabled" to enabled,
                "updatedAt" to System.currentTimeMillis()
            )
            preferencesCollection.document(userId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updatePreferredDuration(userId: String, duration: String): Result<Unit> {
        return try {
            val updates = mapOf(
                "preferredDuration" to duration,
                "updatedAt" to System.currentTimeMillis()
            )
            preferencesCollection.document(userId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateReminderTime(userId: String, reminderTime: String?): Result<Unit> {
        return try {
            val updates = mapOf(
                "reminderTime" to reminderTime,
                "updatedAt" to System.currentTimeMillis()
            )
            preferencesCollection.document(userId).update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createDefaultPreferences(userId: String): Result<Unit> {
        return try {
            val defaultPreferences = UserPreferences(
                userId = userId,
                language = "fr",
                notificationsEnabled = true,
                preferredDuration = "MEDIUM",
                updatedAt = System.currentTimeMillis()
            )
            
            preferencesCollection.document(userId).set(defaultPreferences).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}