package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.TechniquesRepository
import com.example.myapplication.data.Technique
import com.example.myapplication.data.model.TechniqueCategory
import com.example.myapplication.ui.components.TechniqueCard
import com.example.myapplication.ui.components.CompactCategoryChip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SleepScreen(
    onTechniqueClick: (Technique) -> Unit,
    modifier: Modifier = Modifier
) {
    val allTechniques = remember { TechniquesRepository.getAllTechniques() }
    
    // Filter techniques suitable for sleep and relaxation
    val sleepTechniques = remember(allTechniques) {
        allTechniques.filter { technique ->
            technique.category == TechniqueCategory.SOMMEIL ||
            technique.category == TechniqueCategory.RELAXATION ||
            technique.category == TechniqueCategory.RESPIRATION ||
            technique.tags.any { tag -> 
                tag.contains("long-time") || 
                tag.contains("medium-time") 
            }
        }.sortedBy { 
            // Sort by suitability for sleep
            when (it.category) {
                TechniqueCategory.SOMMEIL -> 0
                TechniqueCategory.RELAXATION -> 1
                TechniqueCategory.RESPIRATION -> 2
                else -> 3
            }
        }
    }
    
    val soundTherapy = remember(allTechniques) {
        allTechniques.find { it.id == "sound-therapy" }
    }
    
    val calmingBreathing = remember(allTechniques) {
        allTechniques.filter { 
            it.id == "breathing" || 
            it.id == "guided-breathing" 
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Header
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Sommeil",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Techniques pour vous aider à vous détendre et vous préparer au sommeil",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }
        
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Quick bedtime preparation
            item {
                SleepSection(
                    title = "Préparation au coucher",
                    description = "Techniques rapides pour vous détendre avant de dormir",
                    icon = Icons.Default.Bedtime,
                    techniques = calmingBreathing,
                    onTechniqueClick = onTechniqueClick
                )
            }
            
            // Sound therapy section
            soundTherapy?.let { technique ->
                item {
                    SleepSection(
                        title = "Ambiance sonore",
                        description = "Sons apaisants pour accompagner votre endormissement",
                        icon = Icons.Default.MusicNote,
                        techniques = listOf(technique),
                        onTechniqueClick = onTechniqueClick
                    )
                }
            }
            
            // All sleep techniques
            if (sleepTechniques.isNotEmpty()) {
                item {
                    SleepSection(
                        title = "Toutes les techniques",
                        description = "L'ensemble de nos techniques adaptées au sommeil",
                        icon = Icons.Default.NightsStay,
                        techniques = sleepTechniques,
                        onTechniqueClick = onTechniqueClick
                    )
                }
            }
            
            // Sleep tips card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Conseils pour un meilleur sommeil",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                color = MaterialTheme.colorScheme.onSecondaryContainer
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        val tips = listOf(
                            "Éteignez les écrans 30 minutes avant de dormir",
                            "Maintenez une température fraîche dans votre chambre",
                            "Créez une routine de coucher régulière",
                            "Évitez la caféine après 14h",
                            "Utilisez votre lit uniquement pour dormir"
                        )
                        
                        tips.forEach { tip ->
                            Row(
                                modifier = Modifier.padding(vertical = 2.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = "• ",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                Text(
                                    text = tip,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SleepSection(
    title: String,
    description: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    techniques: List<Technique>,
    onTechniqueClick: (Technique) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            if (techniques.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    techniques.forEach { technique ->
                        TechniqueCard(
                            technique = technique,
                            onClick = { onTechniqueClick(technique) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}