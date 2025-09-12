package com.releaf.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.releaf.app.data.Technique

// Data classes pour les techniques de respiration guidée
data class GuidedBreathingTechnique(
    val key: String,
    val name: String,
    val description: String,
    val timing: String,
    val color: Color,
    val icon: ImageVector,
    val bestFor: String,
    val pattern: List<BreathingPhase>
)

data class BreathingPhase(
    val phase: String,
    val duration: Long, // en millisecondes
    val text: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidedBreathingSelectionScreen(
    technique: Technique,
    onBackClick: () -> Unit,
    onTechniqueSelected: (GuidedBreathingTechnique) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTechnique by remember { mutableStateOf("box") }
    
    // Définition des 4 techniques de respiration guidée
    val techniques = remember {
        listOf(
            GuidedBreathingTechnique(
                key = "box",
                name = "Respiration carrée",
                description = "Un timing égal pour toutes les phases crée une clarté mentale et une concentration.",
                timing = "4-4-4-4",
                color = Color(0xFF3B82F6), // Bleu
                icon = Icons.Default.CropSquare,
                bestFor = "Concentration et focalisation",
                pattern = listOf(
                    BreathingPhase("inhale", 4000, "Inspirez"),
                    BreathingPhase("hold_in", 4000, "Retenez"),
                    BreathingPhase("exhale", 4000, "Expirez"),
                    BreathingPhase("hold_out", 4000, "Retenez")
                )
            ),
            GuidedBreathingTechnique(
                key = "calming",
                name = "Technique 4-7-8",
                description = "Maintien prolongé pour une relaxation profonde",
                timing = "4-7-8",
                color = Color(0xFF8B5CF6), // Violet
                icon = Icons.Default.NightsStay,
                bestFor = "Relaxation profonde et sommeil",
                pattern = listOf(
                    BreathingPhase("inhale", 4000, "Inspirez"),
                    BreathingPhase("hold_in", 7000, "Retenez"),
                    BreathingPhase("exhale", 8000, "Expirez")
                )
            ),
            GuidedBreathingTechnique(
                key = "energizing",
                name = "4-4-6 Énergisant",
                description = "Modèle équilibré pour la vigilance",
                timing = "4-4-6",
                color = Color(0xFF10B981), // Vert
                icon = Icons.Default.Bolt,
                bestFor = "Énergie et clarté mentale",
                pattern = listOf(
                    BreathingPhase("inhale", 4000, "Inspirez"),
                    BreathingPhase("hold_in", 4000, "Retenez"),
                    BreathingPhase("exhale", 6000, "Expirez")
                )
            ),
            GuidedBreathingTechnique(
                key = "quick",
                name = "Réinitialisation rapide",
                description = "Technique rapide pour un soulagement immédiat",
                timing = "3-3-3",
                color = Color(0xFFF59E0B), // Orange
                icon = Icons.Default.Schedule,
                bestFor = "Soulagement rapide du stress",
                pattern = listOf(
                    BreathingPhase("inhale", 3000, "Inspirez"),
                    BreathingPhase("hold_in", 3000, "Retenez"),
                    BreathingPhase("exhale", 3000, "Expirez")
                )
            )
        )
    }
    
    val currentTechnique = techniques.find { it.key == selectedTechnique } ?: techniques[0]
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Respiration guidée") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header avec icône
            Icon(
                Icons.Default.Air,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Respiration guidée",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Suivez des exercices de respiration structurés conçus pour réguler votre système nerveux autonome. Ces guides visuels vous aident à maintenir un timing et un rythme appropriés.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
            )
            
            // Grille de sélection des techniques (2x2)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(vertical = 16.dp)
            ) {
                items(techniques) { tech ->
                    TechniqueSelectionCard(
                        technique = tech,
                        isSelected = selectedTechnique == tech.key,
                        onClick = { selectedTechnique = tech.key }
                    )
                }
            }
            
            // Bouton de démarrage
            Button(
                onClick = { onTechniqueSelected(currentTechnique) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = currentTechnique.color
                )
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Commencer ${currentTechnique.name}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun TechniqueSelectionCard(
    technique: GuidedBreathingTechnique,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.9f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                technique.color.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, technique.color)
        } else {
            androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Icône
            Icon(
                technique.icon,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = if (isSelected) technique.color else MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Nom de la technique
            Text(
                text = technique.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
            
            // Description courte
            Text(
                text = technique.description,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 14.sp,
                maxLines = 2
            )
            
            // Pattern timing avec couleur
            Text(
                text = technique.timing,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = technique.color
            )
            
            // Meilleur pour
            Text(
                text = technique.bestFor,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}