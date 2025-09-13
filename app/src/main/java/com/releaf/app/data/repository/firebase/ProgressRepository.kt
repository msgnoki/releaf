package com.releaf.app.data.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.releaf.app.data.model.firebase.Progress
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class ProgressRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val progressCollection = firestore.collection("progress")

    suspend fun createProgress(userId: String, progress: Progress): Result<Unit> {
        return try {
            progressCollection.document(userId).set(progress).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getProgress(userId: String): Result<Progress?> {
        return try {
            val document = progressCollection.document(userId).get().await()
            val progress = document.toObject<Progress>()
            Result.success(progress)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getProgressFlow(userId: String): Flow<Progress?> = callbackFlow {
        val listener = progressCollection.document(userId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            
            val progress = snapshot?.toObject<Progress>()
            trySend(progress)
        }
        
        awaitClose { listener.remove() }
    }

    suspend fun updateProgress(userId: String, progress: Progress): Result<Unit> {
        return try {
            progressCollection.document(userId).set(progress).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun incrementSession(userId: String, durationMinutes: Int, moodBefore: Int, moodAfter: Int): Result<Unit> {
        return try {
            val progressDoc = progressCollection.document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(progressDoc)
                val currentProgress = snapshot.toObject<Progress>() ?: Progress(userId = userId)
                
                val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
                val isConsecutiveDay = isConsecutiveDay(currentProgress.lastSessionDate, today)
                
                val newStreak = if (isConsecutiveDay) {
                    currentProgress.currentStreak + 1
                } else {
                    1
                }
                
                val moodImprovement = moodAfter - moodBefore
                val totalImprovement = currentProgress.averageMoodImprovement * currentProgress.totalSessions + moodImprovement
                val newAverageMoodImprovement = totalImprovement / (currentProgress.totalSessions + 1)
                
                val updatedProgress = currentProgress.copy(
                    totalSessions = currentProgress.totalSessions + 1,
                    totalMinutes = currentProgress.totalMinutes + durationMinutes,
                    currentStreak = newStreak,
                    longestStreak = maxOf(currentProgress.longestStreak, newStreak),
                    sessionsThisWeek = currentProgress.sessionsThisWeek + 1,
                    minutesThisWeek = currentProgress.minutesThisWeek + durationMinutes,
                    averageMoodImprovement = newAverageMoodImprovement,
                    lastSessionDate = today,
                    updatedAt = System.currentTimeMillis()
                )
                
                transaction.set(progressDoc, updatedProgress)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun resetWeeklyStats(userId: String): Result<Unit> {
        return try {
            val progressDoc = progressCollection.document(userId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(progressDoc)
                val currentProgress = snapshot.toObject<Progress>()
                
                if (currentProgress != null) {
                    val updatedProgress = currentProgress.copy(
                        sessionsThisWeek = 0,
                        minutesThisWeek = 0,
                        weekStartDate = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE),
                        updatedAt = System.currentTimeMillis()
                    )
                    
                    transaction.set(progressDoc, updatedProgress)
                }
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun isConsecutiveDay(lastSessionDate: String, currentDate: String): Boolean {
        if (lastSessionDate.isEmpty()) return false
        
        return try {
            val lastDate = LocalDate.parse(lastSessionDate, DateTimeFormatter.ISO_LOCAL_DATE)
            val today = LocalDate.parse(currentDate, DateTimeFormatter.ISO_LOCAL_DATE)
            lastDate.plusDays(1) == today
        } catch (e: Exception) {
            false
        }
    }
}