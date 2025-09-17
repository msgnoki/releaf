package com.releaf.app.ui.components

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
import com.releaf.app.R
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
import com.releaf.app.data.Technique
import com.releaf.app.data.model.TechniqueCategory
import com.releaf.app.ui.theme.MyApplicationTheme
import com.releaf.app.ui.theme.ReleafPreviews
import com.releaf.app.ui.theme.ReleafDevicePreviews
import com.releaf.app.ui.theme.TechniqueCardTitleStyle
import com.releaf.app.ui.theme.TechniqueDurationStyle

@Composable
fun TechniqueCard(
    technique: Technique,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null
) {
    // Get drawable resource for the technique
    val iconResId = remember(technique.id) { getDrawableForTechnique(technique.id) }
    
    // Design rond uniforme pour toutes les techniques
    RoundTechniqueCard(
        name = technique.name,
        duration = technique.duration,
        iconResId = iconResId,
        onClick = onClick,
        isFavorite = isFavorite,
        onFavoriteClick = onFavoriteClick,
        modifier = modifier
    )
}

@Composable
private fun RoundTechniqueCard(
    name: String,
    duration: String,
    iconResId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onFavoriteClick: (() -> Unit)? = null
) {
    val textColor = MaterialTheme.colorScheme.primary // Utilise la couleur primaire du thème (vert #61cb4c)
    
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image ronde avec icône cœur en superposition
        Box {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
            )
            
            // Icône cœur en haut à droite
            if (onFavoriteClick != null) {
                IconButton(
                    onClick = onFavoriteClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(24.dp)
                        .background(
                            Color.Transparent,
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isFavorite) "Retirer des favoris" else "Ajouter aux favoris",
                        tint = if (isFavorite) Color.Red else Color.Gray,
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Titre avec couleur du thème
        Text(
            text = name,
            style = TechniqueCardTitleStyle,
            color = textColor,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        // Durée avec couleur du thème
        Text(
            text = duration,
            style = TechniqueDurationStyle,
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}


// Map technique IDs to custom PNG icons
private fun getDrawableForTechnique(techniqueId: String): Int {
    return when (techniqueId) {
        "breathing" -> R.drawable.icon_respiration_2min
        "grounding" -> R.drawable.icon_ancrage_54321
        "guided-breathing" -> R.drawable.icon_respiration_guidee
        "progressive-muscle-relaxation" -> R.drawable.icon_relaxation_musculaire
        "peaceful-visualization" -> R.drawable.icon_visualisation_paisible
        "thought-labeling" -> R.drawable.icon_etiquetage_pensees
        "stress-relief-bubbles" -> R.drawable.icon_bulles_antistress
        "sound-therapy" -> R.drawable.icon_therapie_sonore
        "stress-ball" -> R.drawable.icon_balle_antistress
        "breathing-stress-15" -> R.drawable.icon_respiration_1_5
        "respiration-e12" -> R.drawable.icon_respiration_diaphragmatique
        "autogenic-training" -> R.drawable.icon_training_autogene
        "breathing-box" -> R.drawable.icon_respiration_carree
        "breathing-478" -> R.drawable.icon_breathing_478
        "body-scan-meditation" -> R.drawable.icon_body_scan
        "mindful-breathing" -> R.drawable.icon_mindful_breathing
        "loving-kindness-meditation" -> R.drawable.icon_loving_kindness
        "auto-hypnosis-autogenic" -> R.drawable.icon_autogenic_hypnosis
        "forest-immersion-nature" -> R.drawable.icon_forest_immersion
        "meditation-breath-awareness" -> R.drawable.icon_breath_awareness
        else -> R.drawable.icon_respiration_2min // Default fallback
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