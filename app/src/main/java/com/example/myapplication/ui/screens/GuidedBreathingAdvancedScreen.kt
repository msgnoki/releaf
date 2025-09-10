package com.example.myapplication.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import kotlinx.coroutines.delay
import com.example.myapplication.utils.SystemTimer

// États de l'exercice
enum class ExerciseState {
    SELECTION, STARTED, PAUSED, COMPLETED
}

// Options de session
data class SessionLength(
    val cycles: Int,
    val name: String,
    val duration: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidedBreathingAdvancedScreen(
    selectedTechnique: GuidedBreathingTechnique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // État de l'exercice
    var exerciseState by remember { mutableStateOf(ExerciseState.SELECTION) }
    var selectedSessionLength by remember { mutableStateOf(8) } // Standard par défaut
    
    // Compteurs de session
    var currentCycle by remember { mutableIntStateOf(0) }
    var totalCycles by remember { mutableIntStateOf(8) }
    var completedCycles by remember { mutableIntStateOf(0) }
    
    // État de la respiration
    var breathingPhase by remember { mutableStateOf("exhale") }
    var currentPatternIndex by remember { mutableIntStateOf(0) }
    var phaseProgress by remember { mutableFloatStateOf(0f) }
    var remainingTime by remember { mutableLongStateOf(0L) }
    
    // Temps
    var elapsedTime by remember { mutableLongStateOf(0L) }
    var totalSessionTime by remember { mutableLongStateOf(0L) }
    
    // Options de longueur de session
    val sessionLengths = remember {
        listOf(
            SessionLength(5, "Rapide", "2-3 min"),
            SessionLength(8, "Standard", "3-5 min"),
            SessionLength(12, "Étendue", "5-8 min")
        )
    }
    
    // Pattern actuel
    val currentPattern = remember(currentPatternIndex) {
        selectedTechnique.pattern.getOrNull(currentPatternIndex) ?: selectedTechnique.pattern[0]
    }
    
    // Animation pour le cercle de respiration
    val animationValue by animateFloatAsState(
        targetValue = when (breathingPhase) {
            "inhale" -> 1f
            "hold_in" -> 1f
            "exhale" -> 0.3f
            "hold_out" -> 0.3f
            else -> 0.5f
        },
        animationSpec = tween(
            durationMillis = currentPattern.duration.toInt(),
            easing = LinearEasing
        ),
        label = "breathing_animation"
    )
    
    // Gestion du timer principal
    LaunchedEffect(exerciseState, currentPatternIndex) {
        when (exerciseState) {
            ExerciseState.STARTED -> {
                if (currentCycle > totalCycles) {
                    exerciseState = ExerciseState.COMPLETED
                    completedCycles = totalCycles
                    totalSessionTime = elapsedTime
                    return@LaunchedEffect
                }
                
                val pattern = selectedTechnique.pattern[currentPatternIndex]
                breathingPhase = pattern.phase
                remainingTime = pattern.duration
                
                // Initialiser le progrès selon la phase
                phaseProgress = when (pattern.phase) {
                    "inhale" -> 0f
                    "hold_in", "exhale" -> 100f
                    "hold_out" -> 0f
                    else -> 0f
                }
                
                // Timer de progression
                val startTime = System.currentTimeMillis()
                while (remainingTime > 0 && exerciseState == ExerciseState.STARTED) {
                    delay(16) // 60fps pour plus de fluidité
                    
                    val elapsed = System.currentTimeMillis() - startTime
                    val progress = (elapsed.toFloat() / pattern.duration) * 100f
                    
                    // Mettre à jour le progrès selon la phase
                    phaseProgress = when (pattern.phase) {
                        "inhale" -> progress
                        "hold_in" -> 100f
                        "exhale" -> 100f - progress
                        "hold_out" -> 0f
                        else -> progress
                    }
                    
                    remainingTime = maxOf(pattern.duration - elapsed, 0L)
                    elapsedTime += 50
                }
                
                // Passer à la phase suivante
                if (exerciseState == ExerciseState.STARTED) {
                    currentPatternIndex++
                    if (currentPatternIndex >= selectedTechnique.pattern.size) {
                        currentPatternIndex = 0
                        currentCycle++
                    }
                }
            }
            else -> {}
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text(stringResource(R.string.guided_breathing_title)) },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        when (exerciseState) {
            ExerciseState.SELECTION -> {
                // Écran de sélection des options
                SelectionContent(
                    technique = selectedTechnique,
                    sessionLengths = sessionLengths,
                    selectedSessionLength = selectedSessionLength,
                    onSessionLengthSelected = { selectedSessionLength = it },
                    onStartExercise = {
                        totalCycles = selectedSessionLength
                        currentCycle = 1
                        exerciseState = ExerciseState.STARTED
                    }
                )
            }
            
            ExerciseState.STARTED, ExerciseState.PAUSED -> {
                // Interface d'exercice
                ExerciseContent(
                    technique = selectedTechnique,
                    currentCycle = currentCycle,
                    totalCycles = totalCycles,
                    elapsedTime = elapsedTime,
                    breathingPhase = breathingPhase,
                    phaseProgress = phaseProgress,
                    remainingTime = remainingTime,
                    animationValue = animationValue,
                    isPaused = exerciseState == ExerciseState.PAUSED,
                    onPause = { exerciseState = ExerciseState.PAUSED },
                    onResume = { exerciseState = ExerciseState.STARTED },
                    onStop = {
                        exerciseState = ExerciseState.SELECTION
                        currentCycle = 0
                        currentPatternIndex = 0
                        elapsedTime = 0
                    }
                )
            }
            
            ExerciseState.COMPLETED -> {
                // Écran de completion
                CompletionContent(
                    technique = selectedTechnique,
                    completedCycles = completedCycles,
                    totalTime = totalSessionTime,
                    onPracticeAgain = {
                        exerciseState = ExerciseState.SELECTION
                        currentCycle = 0
                        currentPatternIndex = 0
                        elapsedTime = 0
                    },
                    onComplete = onComplete
                )
            }
        }
    }
}

@Composable
private fun SelectionContent(
    technique: GuidedBreathingTechnique,
    sessionLengths: List<SessionLength>,
    selectedSessionLength: Int,
    onSessionLengthSelected: (Int) -> Unit,
    onStartExercise: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // En-tête technique sélectionnée
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = technique.color.copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    technique.icon,
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = technique.color
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Text(
                    text = technique.name,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = technique.timing,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = technique.color
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = technique.description,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Sélection de la durée
        Text(
            text = "Durée de la session",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            sessionLengths.forEach { session ->
                SessionLengthCard(
                    session = session,
                    isSelected = selectedSessionLength == session.cycles,
                    onClick = { onSessionLengthSelected(session.cycles) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Bouton de démarrage
        Button(
            onClick = onStartExercise,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = technique.color
            )
        ) {
            Icon(
                Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Commencer l'exercice",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun SessionLengthCard(
    session: SessionLength,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
        } else null
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "${session.cycles}",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "cycles",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = session.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = session.duration,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun ExerciseContent(
    technique: GuidedBreathingTechnique,
    currentCycle: Int,
    totalCycles: Int,
    elapsedTime: Long,
    breathingPhase: String,
    phaseProgress: Float,
    remainingTime: Long,
    animationValue: Float,
    isPaused: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // En-tête de session avec stats
        SessionStatsCard(
            technique = technique,
            currentCycle = currentCycle,
            totalCycles = totalCycles,
            elapsedTime = elapsedTime,
            breathingPhase = breathingPhase
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Animation de respiration
        Box(
            modifier = Modifier.size(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            ) {
                drawAdvancedBreathingCircle(animationValue, technique.color, breathingPhase)
            }
            
            // Indicateur de phase au centre
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when (breathingPhase) {
                        "inhale" -> "Inspirez"
                        "hold_in" -> "Retenez"
                        "exhale" -> "Expirez"
                        "hold_out" -> "Pause"
                        else -> "Prêt"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = "${remainingTime / 1000}.${(remainingTime % 1000) / 100}s",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Barres de progression
        ProgressBarsSection(
            currentCycle = currentCycle,
            totalCycles = totalCycles,
            phaseProgress = phaseProgress,
            technique = technique
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Contrôles
        ControlButtonsRow(
            isPaused = isPaused,
            onPause = onPause,
            onResume = onResume,
            onStop = onStop
        )
    }
}

@Composable
private fun SessionStatsCard(
    technique: GuidedBreathingTechnique,
    currentCycle: Int,
    totalCycles: Int,
    elapsedTime: Long,
    breathingPhase: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = technique.color.copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem("Temps", formatTime(elapsedTime))
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp)
            )
            StatItem("Cycles", "$currentCycle/$totalCycles")
            Divider(
                modifier = Modifier
                    .height(40.dp)
                    .width(1.dp)
            )
            StatItem("Phase", technique.timing)
        }
    }
}

@Composable
private fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProgressBarsSection(
    currentCycle: Int,
    totalCycles: Int,
    phaseProgress: Float,
    technique: GuidedBreathingTechnique
) {
    Column {
        // Progression de session
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Progression de session",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$currentCycle/$totalCycles",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LinearProgressIndicator(
            progress = { (currentCycle.toFloat() / totalCycles) },
            modifier = Modifier.fillMaxWidth(),
            color = technique.color
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Progression de phase
        Text(
            text = "Phase actuelle",
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LinearProgressIndicator(
            progress = { phaseProgress / 100f },
            modifier = Modifier.fillMaxWidth(),
            color = technique.color
        )
    }
}

@Composable
private fun ControlButtonsRow(
    isPaused: Boolean,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isPaused) {
            Button(
                onClick = onResume,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF10B981) // Vert
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.resume))
            }
        } else {
            Button(
                onClick = onPause,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFF59E0B) // Orange
                )
            ) {
                Icon(Icons.Default.Pause, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.pause))
            }
        }
        
        OutlinedButton(
            onClick = onStop,
            modifier = Modifier
                .weight(1f)
                .height(56.dp)
        ) {
            Icon(Icons.Default.Stop, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(stringResource(R.string.stop))
        }
    }
}

@Composable
private fun CompletionContent(
    technique: GuidedBreathingTechnique,
    completedCycles: Int,
    totalTime: Long,
    onPracticeAgain: () -> Unit,
    onComplete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF10B981).copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF10B981)
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Exercice terminé !",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Excellent travail ! Vous avez complété $completedCycles cycles de ${technique.name} en ${formatTime(totalTime)}.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Air,
                            contentDescription = null,
                            tint = Color(0xFF3B82F6),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Respiration optimisée",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Psychology,
                            contentDescription = null,
                            tint = Color(0xFF8B5CF6),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Esprit centré",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Boutons d'action
        Column {
            Button(
                onClick = onPracticeAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = technique.color
                )
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.reset))
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedButton(
                onClick = onComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(stringResource(R.string.finish))
            }
        }
    }
}

private fun DrawScope.drawAdvancedBreathingCircle(
    animationValue: Float,
    color: Color,
    phase: String
) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val maxRadius = size.minDimension / 2 * 0.85f
    val currentRadius = maxRadius * animationValue
    
    // Cercle de fond
    drawCircle(
        color = color.copy(alpha = 0.1f),
        radius = maxRadius,
        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
    )
    
    // Cercle principal animé
    drawCircle(
        color = color.copy(alpha = 0.6f),
        radius = currentRadius,
        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
    )
    
    // Cercle intérieur
    drawCircle(
        color = color.copy(alpha = 0.8f),
        radius = currentRadius * 0.7f,
        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
    )
    
    // Point central
    drawCircle(
        color = color,
        radius = currentRadius * 0.1f,
        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
    )
}

private fun formatTime(milliseconds: Long): String {
    val minutes = milliseconds / 60000
    val seconds = (milliseconds % 60000) / 1000
    return "${minutes}:${seconds.toString().padStart(2, '0')}"
}