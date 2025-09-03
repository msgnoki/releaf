package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Technique
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreen(
    technique: Technique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isActive by remember { mutableStateOf(false) }
    var progress by remember { mutableStateOf(0f) }
    var timeRemaining by remember { mutableStateOf(120) } // 2 minutes par défaut
    var currentPhase by remember { mutableStateOf("Préparez-vous...") }
    
    // Optimized timer effect with proper coroutine management
    LaunchedEffect(isActive, technique.id) {
        if (isActive) {
            try {
                while (timeRemaining > 0 && isActive) {
                    delay(1000)
                    timeRemaining--
                    progress = 1f - (timeRemaining.toFloat() / 120f)
                    
                    // Update phase based on technique - extracted for performance
                    currentPhase = getPhaseForTechnique(technique.id, timeRemaining)
                }
                
                if (timeRemaining <= 0) {
                    onComplete()
                }
            } catch (e: kotlinx.coroutines.CancellationException) {
                // Proper cancellation handling
                throw e
            }
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text(technique.name) },
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
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Progress Circle
            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    progress = progress,
                    modifier = Modifier.fillMaxSize(),
                    strokeWidth = 8.dp,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "${timeRemaining / 60}:${String.format("%02d", timeRemaining % 60)}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    
                    Text(
                        text = if (isActive) "En cours" else "En pause",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Current Phase
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                )
            ) {
                Text(
                    text = currentPhase,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Control Buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Play/Pause Button
                FilledTonalButton(
                    onClick = { isActive = !isActive },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        if (isActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isActive) "Pause" else "Commencer",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isActive) "Pause" else "Commencer",
                        fontSize = 16.sp
                    )
                }
                
                // Reset Button
                OutlinedButton(
                    onClick = {
                        isActive = false
                        timeRemaining = 120
                        progress = 0f
                        currentPhase = "Préparez-vous..."
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Recommencer",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Recommencer",
                        fontSize = 16.sp
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Instructions
            Text(
                text = getInstructionsForTechnique(technique.id),
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

// Extracted phase calculation for better performance and separation of concerns
private fun getPhaseForTechnique(techniqueId: String, timeRemaining: Int): String {
    return when (techniqueId) {
        "breathing" -> when {
            timeRemaining % 8 < 4 -> "Inspirez..."
            else -> "Expirez..."
        }
        "grounding" -> when {
            timeRemaining > 100 -> "5 choses que vous voyez"
            timeRemaining > 80 -> "4 choses que vous touchez"
            timeRemaining > 60 -> "3 choses que vous entendez"
            timeRemaining > 40 -> "2 choses que vous sentez"
            timeRemaining > 20 -> "1 chose que vous goûtez"
            else -> "Prenez une profonde respiration"
        }
        else -> "Concentrez-vous sur l'exercice"
    }
}

private fun getInstructionsForTechnique(techniqueId: String): String {
    return when (techniqueId) {
        "breathing" -> "Inspirez pendant 4 secondes, retenez pendant 4 secondes, puis expirez pendant 4 secondes."
        "grounding" -> "Identifiez 5 choses que vous voyez, 4 que vous touchez, 3 que vous entendez, 2 que vous sentez, et 1 que vous goûtez."
        "guidedBreathing" -> "Suivez le rythme de respiration guidé pour calmer votre esprit."
        "progressiveMuscleRelaxation" -> "Contractez puis relâchez chaque groupe musculaire pendant 5-10 secondes."
        "peacefulVisualization" -> "Imaginez-vous dans un lieu calme et paisible. Concentrez-vous sur les détails."
        "thoughtLabeling" -> "Observez vos pensées sans jugement et étiquetez-les simplement comme 'pensée'."
        "stressReliefBubbles" -> "Concentrez-vous sur l'activité ludique pour détendre votre esprit."
        "soundTherapy" -> "Écoutez attentivement les sons apaisants et laissez-vous porter."
        "stressBall" -> "Serrez et relâchez de manière répétitive pour libérer la tension."
        else -> "Concentrez-vous sur votre respiration et restez présent dans l'instant."
    }
}