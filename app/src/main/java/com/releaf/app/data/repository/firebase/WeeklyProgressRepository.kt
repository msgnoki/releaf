package com.releaf.app.data.repository.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.releaf.app.data.model.firebase.WeeklyProgress
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

class WeeklyProgressRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val weeklyProgressCollection = firestore.collection("weeklyProgress")

    suspend fun createWeeklyProgress(userId: String, weeklyProgress: WeeklyProgress): Result<Unit> {
        return try {
            val docId = "${userId}_${weeklyProgress.weekStartDate}"
            weeklyProgressCollection.document(docId).set(weeklyProgress).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getCurrentWeekProgress(userId: String): Result<WeeklyProgress?> {
        return try {
            val weekStart = getCurrentWeekStart()
            val docId = "${userId}_$weekStart"
            val document = weeklyProgressCollection.document(docId).get().await()
            val weeklyProgress = document.toObject<WeeklyProgress>()
            Result.success(weeklyProgress)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCurrentWeekProgressFlow(userId: String): Flow<WeeklyProgress?> = callbackFlow {
        val weekStart = getCurrentWeekStart()
        val docId = "${userId}_$weekStart"
        
        val listener = weeklyProgressCollection.document(docId).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }
            
            val weeklyProgress = snapshot?.toObject<WeeklyProgress>()
            trySend(weeklyProgress)
        }
        
        awaitClose { listener.remove() }
    }

    suspend fun getWeekProgress(userId: String, weekStartDate: String): Result<WeeklyProgress?> {
        return try {
            val docId = "${userId}_$weekStartDate"
            val document = weeklyProgressCollection.document(docId).get().await()
            val weeklyProgress = document.toObject<WeeklyProgress>()
            Result.success(weeklyProgress)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getUserWeeklyHistory(userId: String, limit: Int = 12): Result<List<WeeklyProgress>> {
        return try {
            val documents = weeklyProgressCollection
                .whereEqualTo("userId", userId)
                .orderBy("weekStartDate", Query.Direction.DESCENDING)
                .limit(limit.toLong())
                .get()
                .await()
            
            val weeklyHistory = documents.mapNotNull { it.toObject<WeeklyProgress>() }
            Result.success(weeklyHistory)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addSessionToCurrentWeek(userId: String, durationMinutes: Int): Result<Unit> {
        return try {
            val weekStart = getCurrentWeekStart()
            val docId = "${userId}_$weekStart"
            val dayOfWeek = LocalDate.now().dayOfWeek.value - 1 // Convert to 0-6 (Monday = 0)
            
            val weeklyProgressDoc = weeklyProgressCollection.document(docId)
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(weeklyProgressDoc)
                val currentProgress = snapshot.toObject<WeeklyProgress>() ?: WeeklyProgress(
                    userId = userId,
                    weekStartDate = weekStart,
                    dailySessions = List(7) { 0 },
                    dailyMinutes = List(7) { 0 }
                )
                
                val updatedSessions = currentProgress.dailySessions.toMutableList()
                val updatedMinutes = currentProgress.dailyMinutes.toMutableList()
                
                updatedSessions[dayOfWeek] = updatedSessions[dayOfWeek] + 1
                updatedMinutes[dayOfWeek] = updatedMinutes[dayOfWeek] + durationMinutes
                
                val updatedProgress = currentProgress.copy(
                    dailySessions = updatedSessions,
                    dailyMinutes = updatedMinutes,
                    updatedAt = System.currentTimeMillis()
                )
                
                transaction.set(weeklyProgressDoc, updatedProgress)
            }.await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun initializeCurrentWeek(userId: String): Result<Unit> {
        return try {
            val weekStart = getCurrentWeekStart()
            val weeklyProgress = WeeklyProgress(
                userId = userId,
                weekStartDate = weekStart,
                dailySessions = List(7) { 0 },
                dailyMinutes = List(7) { 0 },
                updatedAt = System.currentTimeMillis()
            )
            
            createWeeklyProgress(userId, weeklyProgress)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getWeeklyStats(userId: String): Result<WeeklyStats> {
        return try {
            val currentWeekProgress = getCurrentWeekProgress(userId).getOrNull()
            
            if (currentWeekProgress == null) {
                return Result.success(WeeklyStats(
                    totalSessions = 0,
                    totalMinutes = 0,
                    activeDays = 0,
                    dailyData = List(7) { DayData(0, 0) }
                ))
            }
            
            val totalSessions = currentWeekProgress.dailySessions.sum()
            val totalMinutes = currentWeekProgress.dailyMinutes.sum()
            val activeDays = currentWeekProgress.dailySessions.count { it > 0 }
            val dailyData = currentWeekProgress.dailySessions.zip(currentWeekProgress.dailyMinutes) { sessions, minutes ->
                DayData(sessions, minutes)
            }
            
            val stats = WeeklyStats(
                totalSessions = totalSessions,
                totalMinutes = totalMinutes,
                activeDays = activeDays,
                dailyData = dailyData
            )
            
            Result.success(stats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun getCurrentWeekStart(): String {
        val today = LocalDate.now()
        val monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        return monday.format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}

data class WeeklyStats(
    val totalSessions: Int,
    val totalMinutes: Int,
    val activeDays: Int,
    val dailyData: List<DayData>
)

data class DayData(
    val sessions: Int,
    val minutes: Int
)