package com.example.myapplication.ui.theme

import androidx.compose.ui.graphics.Color
import com.example.myapplication.data.model.TechniqueCategory

object CategoryColors {
    
    fun getCategoryColor(category: TechniqueCategory, isDarkTheme: Boolean = false): Color {
        return when (category) {
            TechniqueCategory.RESPIRATION -> if (isDarkTheme) RespirationDark else RespirationLight
            TechniqueCategory.RELAXATION -> if (isDarkTheme) RelaxationDark else RelaxationLight
            TechniqueCategory.ANCRAGE -> if (isDarkTheme) AncrageDark else AncrageLight
            TechniqueCategory.VISUALISATION -> if (isDarkTheme) VisualisationDark else VisualisationLight
            TechniqueCategory.STRESS_RELIEF -> if (isDarkTheme) StressReliefDark else StressReliefLight
            TechniqueCategory.SOMMEIL -> if (isDarkTheme) SommeilDark else SommeilLight
            TechniqueCategory.CRISE -> if (isDarkTheme) CriseDark else CriseLight
            TechniqueCategory.PLEINE_CONSCIENCE -> if (isDarkTheme) Color(0xFFFFB74D) else Color(0xFFFFCC02)
            TechniqueCategory.INTERACTIF -> if (isDarkTheme) Color(0xFFFF8A65) else Color(0xFFFF9800)
            TechniqueCategory.MEDITATION -> if (isDarkTheme) Color(0xFFAB47BC) else Color(0xFF9C27B0)
        }
    }
    
    fun getCategoryColorByHex(colorHex: String): Color {
        return try {
            Color(android.graphics.Color.parseColor(colorHex))
        } catch (e: Exception) {
            AccentPrimary // Fallback color
        }
    }
    
    fun getCategoryPastelColor(category: TechniqueCategory): Color {
        return when (category) {
            TechniqueCategory.RESPIRATION -> Color(0xFF81C784)
            TechniqueCategory.RELAXATION -> Color(0xFF90CAF9)
            TechniqueCategory.ANCRAGE -> Color(0xFFA5D6A7)
            TechniqueCategory.VISUALISATION -> Color(0xFFCE93D8)
            TechniqueCategory.STRESS_RELIEF -> Color(0xFFFFAB91)
            TechniqueCategory.SOMMEIL -> Color(0xFFB39DDB)
            TechniqueCategory.CRISE -> Color(0xFFEF9A9A)
            TechniqueCategory.PLEINE_CONSCIENCE -> Color(0xFFFFE082)
            TechniqueCategory.INTERACTIF -> Color(0xFFFFCC80)
            TechniqueCategory.MEDITATION -> Color(0xFFCE93D8)
        }
    }
    
    fun getIconColor(iconColorName: String): Color {
        return when (iconColorName.lowercase()) {
            "cyan" -> Color(0xFF00BCD4)
            "green" -> Color(0xFF4CAF50)
            "blue" -> Color(0xFF2196F3)
            "purple" -> Color(0xFF9C27B0)
            "teal" -> Color(0xFF009688)
            "indigo" -> Color(0xFF3F51B5)
            "orange" -> Color(0xFFFF9800)
            else -> AccentPrimary
        }
    }
}