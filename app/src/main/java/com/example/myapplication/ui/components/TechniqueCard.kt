package com.example.myapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material3.*
import com.example.myapplication.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Technique
import com.example.myapplication.data.model.TechniqueCategory
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.theme.ReleafPreviews
import com.example.myapplication.ui.theme.ReleafDevicePreviews

@Composable
fun TechniqueCard(
    technique: Technique,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Get drawable resource for the technique
    val iconResId = remember(technique.id) { getDrawableForTechnique(technique.id) }
    
    // Design rond uniforme pour toutes les techniques
    RoundTechniqueCard(
        name = technique.name,
        duration = technique.duration,
        iconResId = iconResId,
        onClick = onClick,
        modifier = modifier
    )
}

@Composable
private fun RoundTechniqueCard(
    name: String,
    duration: String,
    iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val skyBlue = Color(0xFF87CEEB) // Couleur bleu ciel
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image ronde - all assets are now PNG bitmaps
        Image(
            painter = painterResource(id = iconResId),
            contentDescription = name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Titre en bleu ciel
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = skyBlue,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Durée en bleu ciel
        Text(
            text = duration,
            fontSize = 12.sp,
            color = skyBlue,
            textAlign = TextAlign.Center
        )
    }
}


// Map technique IDs to custom drawable resources from assets
// Uses the 4 main category icons: respiration, relaxation, meditation, music
private fun getDrawableForTechnique(techniqueId: String): Int {
    return when (techniqueId) {
        // Breathing/Respiration techniques
        "breathing", "guided-breathing", "breathing-stress-15", "respiration-e12", 
        "breathing-box", "breathing-478", "mindful-breathing", "meditation-breath-awareness" -> 
            R.drawable.respiration
            
        // Relaxation techniques
        "progressive-muscle-relaxation", "autogenic-training", "stress-ball", "grounding",
        "stress-relief-bubbles" -> 
            R.drawable.relaxation
            
        // Meditation and visualization techniques  
        "body-scan-meditation", "loving-kindness-meditation", "auto-hypnosis-autogenic",
        "peaceful-visualization", "forest-immersion-nature", "thought-labeling" ->
            R.drawable.meditayion
            
        // Sound therapy
        "sound-therapy" -> 
            R.drawable.music
            
        else -> R.drawable.respiration // Default fallback to respiration
    }
}

// Pure function for color mapping
private fun getColorForTechnique(colorName: String): Color {
    return when (colorName) {
        "cyan" -> Color.Cyan
        "green" -> Color.Green
        "blue" -> Color.Blue
        "purple" -> Color(0xFF9C27B0)
        "indigo" -> Color(0xFF3F51B5)
        "teal" -> Color(0xFF009688)
        "orange" -> Color(0xFFFF9800)
        else -> Color(0xFF3F51B5) // Default material blue instead of theme-dependent
    }
}

// Preview Functions
@ReleafPreviews
@Composable
fun TechniqueCardPreview() {
    MyApplicationTheme {
        TechniqueCard(
            technique = Technique(
                id = "breathing",
                icon = "air",
                iconColor = "cyan",
                tags = listOf("high-anxiety", "short-time"),
                name = "Respiration Consciente",
                shortDescription = "Exercice de respiration guidée pour se détendre",
                description = "Quand l'anxiété frappe, votre respiration devient superficielle et rapide.",
                duration = "5 min",
                category = TechniqueCategory.RESPIRATION,
                durationMinutesStart = 2,
                durationMinutesEnd = 5
            ),
            onClick = { }
        )
    }
}

@Preview(name = "Long Title", showBackground = true)
@Composable
fun TechniqueCardLongTitlePreview() {
    MyApplicationTheme {
        TechniqueCard(
            technique = Technique(
                id = "progressive-muscle-relaxation",
                icon = "accessibility",
                iconColor = "purple",
                tags = listOf("moderate-anxiety", "medium-time"),
                name = "Relaxation Musculaire Progressive Complète",
                shortDescription = "Technique avancée de relaxation",
                description = "L'anxiété crée souvent une tension physique.",
                duration = "20 min",
                category = TechniqueCategory.RELAXATION,
                durationMinutesStart = 8,
                durationMinutesEnd = 20
            ),
            onClick = { }
        )
    }
}

@Preview(name = "Grid of Cards", showBackground = true)
@Composable
fun TechniqueCardGridPreview() {
    MyApplicationTheme {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TechniqueCard(
                modifier = Modifier.weight(1f),
                technique = Technique(
                    id = "breathing",
                    icon = "air",
                    iconColor = "cyan",
                    tags = listOf("high-anxiety", "short-time"),
                    name = "Respiration",
                    shortDescription = "Exercice de respiration",
                    description = "Exercice de respiration simple.",
                    duration = "5 min",
                    category = TechniqueCategory.RESPIRATION,
                    durationMinutesStart = 2,
                    durationMinutesEnd = 5
                ),
                onClick = { }
            )
            TechniqueCard(
                modifier = Modifier.weight(1f),
                technique = Technique(
                    id = "stress-ball",
                    icon = "sports_handball",
                    iconColor = "orange",
                    tags = listOf("high-anxiety", "short-time"),
                    name = "Balle Anti-Stress",
                    shortDescription = "Exercice tactile",
                    description = "Balle anti-stress virtuelle.",
                    duration = "1 min",
                    category = TechniqueCategory.STRESS_RELIEF,
                    durationMinutesStart = 1,
                    durationMinutesEnd = 5
                ),
                onClick = { }
            )
        }
    }
}

@ReleafDevicePreviews
@Composable
fun TechniqueCardDifferentSizesPreview() {
    MyApplicationTheme {
        TechniqueCard(
            technique = Technique(
                id = "sound-therapy",
                icon = "graphic_eq",
                iconColor = "green",
                tags = listOf("moderate-anxiety", "medium-time"),
                name = "Thérapie Sonore",
                shortDescription = "Sons relaxants de la nature",
                description = "Des fréquences sonores spécifiques.",
                duration = "30 min",
                category = TechniqueCategory.RELAXATION,
                durationMinutesStart = 5,
                durationMinutesEnd = 30
            ),
            onClick = { }
        )
    }
}