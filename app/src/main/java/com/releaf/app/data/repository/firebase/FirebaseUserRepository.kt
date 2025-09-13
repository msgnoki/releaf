package com.releaf.app.data.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.releaf.app.data.model.firebase.FirebaseUser
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val usersCollection = firestore.collection("users")

    suspend fun createUser(userId: String, user: FirebaseUser): Result<Unit> {
        return try {
            usersCollection.document(userId).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUser(userId: String): Result<FirebaseUser?> {
        return try {
            val document = usersCollection.document(userId).get().await()
            val user = document.toObject<FirebaseUser>()
            Result.success(user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserFlow(userId: String): Flow<FirebaseUser?> = callbackFlow {
        val listener = usersCollection.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            
            val user = snapshot?.toObject<FirebaseUser>()
            trySend(user)
        }
        
        awaitClose { listener.remove() }
    }

    suspend fun updateUser(userId: String, user: FirebaseUser): Result<Unit> {
        return try {
            usersCollection.document(userId).set(user).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateUserXP(userId: String, xpToAdd: Int): Result<Unit> {
        return try {
            val userDoc = usersCollection.document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userDoc)
                val currentUser = snapshot.toObject<FirebaseUser>()
                
                if (currentUser != null) {
                    val newXp = currentUser.currentXp + xpToAdd
                    val newLevel = calculateLevel(newXp)
                    
                    val updatedUser = currentUser.copy(
                        currentXp = newXp,
                        level = newLevel,
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    transaction.set(userDoc, updatedUser)
                }
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteUser(userId: String): Result<Unit> {
        return try {
            usersCollection.document(userId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun calculateLevel(xp: Int): Int {
        return when {
            xp >= 2000 -> 6 // ZEN_MASTER
            xp >= 1000 -> 5 // MASTER
            xp >= 600 -> 4 // EXPERT
            xp >= 300 -> 3 // PRACTITIONER
            xp >= 100 -> 2 // APPRENTICE
            else -> 1 // BEGINNER
        }
    }
}