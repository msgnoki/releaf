package com.releaf.app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.time.LocalDate

@Entity(tableName = "routine_of_day")
@Serializable
data class RoutineOfDay(
    @PrimaryKey
    val id: String,
    val userId: String,
    val date: String, // LocalDate as String for Room compatibility
    val blocks: List<RoutineBlock>,
    val totalMinutes: Int,
    val completed: Boolean = false,
    val completedAt: Long? = null,
    val generatedAt: Long = System.currentTimeMillis()
)

@Serializable
data class RoutineBlock(
    val techniqueId: String,
    val techniqueName: String,
    val durationMinutes: Int,
    val order: Int,
    val category: TechniqueCategory,
    val completed: Boolean = false,
    val completedAt: Long? = null
)

enum class TechniqueCategory(val displayName: String, val colorHex: String, val emoji: String) {
    RESPIRATION("Respiration", "#81C784", "🌬️"),
    RELAXATION("Relaxation", "#90CAF9", "🧘"),
    ANCRAGE("Ancrage", "#A5D6A7", "⚓"),
    VISUALISATION("Visualisation", "#CE93D8", "🌸"),
    STRESS_RELIEF("Anti-stress", "#FFAB91", "💆"),
    SOMMEIL("Sommeil", "#B39DDB", "😴"),
    CRISE("Crise d'anxiété", "#EF9A9A", "🚨"),
    PLEINE_CONSCIENCE("Pleine Conscience", "#FFCC02", "🧠"),
    INTERACTIF("Interactif", "#FF9800", "🎮"),
    MEDITATION("Méditation", "#9C27B0", "🕉️")
}