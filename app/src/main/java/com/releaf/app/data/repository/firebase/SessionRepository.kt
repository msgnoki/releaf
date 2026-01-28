package com.releaf.app.data.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.releaf.app.data.model.firebase.Session
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SessionRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val sessionsCollection = firestore.collection("sessions")

    /**
     * Crée une session avec un ID stable
     * L'ID est généré côté client et injecté dans le document
     */
    suspend fun createSession(session: Session): Result<String> {
        return try {
            // Créer une référence de document avec un ID auto-généré
            val documentRef = sessionsCollection.document()
            val sessionId = documentRef.id

            // Injecter l'ID dans la session avant sauvegarde
            val sessionWithId = session.copy(id = sessionId)

            // Sauvegarder avec l'ID inclus
            documentRef.set(sessionWithId).await()

            Result.success(sessionId)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getSession(sessionId: String): Result<Session?> {
        return try {
            val document = sessionsCollection.document(sessionId).get().await()
            val session = document.toObject<Session>()?.copy(id = document.id)
            Result.success(session)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserSessions(userId: String, limit: Int = 50): Result<List<Session>> {
        return try {
            val documents = sessionsCollection
                .whereEqualTo("userId", userId)
                .orderBy("startTime", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()

            val sessions = documents.mapNotNull { doc ->
                doc.toObject<Session>()?.copy(id = doc.id)
            }
            Result.success(sessions)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getUserSessionsFlow(userId: String, limit: Int = 50): Flow<List<Session>> = callbackFlow {
        val listener = sessionsCollection
            .whereEqualTo("userId", userId)
            .orderBy("startTime", Query.Direction.DESCENDING)
            .limit(limit.toLong())
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // Ne pas fermer le flow pour une erreur temporaire, envoyer une liste vide
                    trySend(emptyList())
                    return@addSnapshotListener
                }

                val sessions = snapshot?.mapNotNull { doc ->
                    doc.toObject<Session>()?.copy(id = doc.id)
                } ?: emptyList()
                trySend(sessions)
            }

        awaitClose { listener.remove() }
    }

    suspend fun updateSession(sessionId: String, session: Session): Result<Unit> {
        return try {
            sessionsCollection.document(sessionId).set(session).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun completeSession(sessionId: String, endTime: Long, moodAfter: Int, notes: String? = null): Result<Unit> {
        return try {
            val sessionDoc = sessionsCollection.document(sessionId)
            val updates = mapOf(
                "completed" to true,
                "endTime" to endTime,
                "moodAfter" to moodAfter,
                "notes" to notes
            )
            sessionDoc.update(updates).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteSession(sessionId: String): Result<Unit> {
        return try {
            sessionsCollection.document(sessionId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getTechniqueStats(userId: String, techniqueId: String): Result<TechniqueStats> {
        return try {
            val documents = sessionsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("techniqueId", techniqueId)
                .whereEqualTo("completed", true)
                .get()
                .await()

            val sessions = documents.mapNotNull { doc ->
                doc.toObject<Session>()?.copy(id = doc.id)
            }
            val totalSessions = sessions.size
            val totalMinutes = sessions.sumOf { it.duration }
            val averageMoodImprovement = if (sessions.isNotEmpty()) {
                sessions.map { it.moodAfter - it.moodBefore }.average().toFloat()
            } else 0f

            val stats = TechniqueStats(
                totalSessions = totalSessions,
                totalMinutes = totalMinutes,
                averageMoodImprovement = averageMoodImprovement
            )

            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

data class TechniqueStats(
    val totalSessions: Int,
    val totalMinutes: Int,
    val averageMoodImprovement: Float
)