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
import com.example.myapplication.data.Technique
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuidedBreathingExerciseScreen(
    technique: Technique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isActive by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(300) } // 5 minutes for guided breathing
    var currentPhase by remember { mutableStateOf("Préparez-vous à commencer") }
    var currentCycle by remember { mutableStateOf(0) }
    var breathingPhase by remember { mutableStateOf(GuidedBreathingPhase.PREPARE) }
    
    // Animation for breathing circle
    val animationValue by animateFloatAsState(
        targetValue = when (breathingPhase) {
            GuidedBreathingPhase.PREPARE -> 0.5f
            GuidedBreathingPhase.INHALE -> 1f
            GuidedBreathingPhase.HOLD_IN -> 1f
            GuidedBreathingPhase.EXHALE -> 0.3f
            GuidedBreathingPhase.HOLD_OUT -> 0.3f
        },
        animationSpec = when (breathingPhase) {
            GuidedBreathingPhase.PREPARE -> tween(1000)
            GuidedBreathingPhase.INHALE -> tween(4000, easing = LinearEasing)
            GuidedBreathingPhase.HOLD_IN -> tween(100)
            GuidedBreathingPhase.EXHALE -> tween(6000, easing = LinearEasing)
            GuidedBreathingPhase.HOLD_OUT -> tween(100)
        }
    )
    
    // Breathing cycle management
    LaunchedEffect(isActive) {
        if (isActive) {
            while (timeRemaining > 0 && isActive) {
                // Complete breathing cycle: ~12 seconds
                
                // Inhale phase - 4 seconds
                breathingPhase = GuidedBreathingPhase.INHALE
                currentPhase = "Inspirez profondément..."
                delay(4000)
                
                if (!isActive) break
                
                // Hold inhale - 2 seconds
                breathingPhase = GuidedBreathingPhase.HOLD_IN
                currentPhase = "Retenez votre souffle..."
                delay(2000)
                
                if (!isActive) break
                
                // Exhale phase - 6 seconds
                breathingPhase = GuidedBreathingPhase.EXHALE
                currentPhase = "Expirez lentement..."
                delay(6000)
                
                if (!isActive) break
                
                // Hold exhale - brief pause
                breathingPhase = GuidedBreathingPhase.HOLD_OUT
                currentPhase = "Pause..."
                delay(1000)
                
                // Update counters
                currentCycle++
                timeRemaining -= 13 // Total cycle time
                
                if (timeRemaining <= 0) break
            }
            
            if (timeRemaining <= 0) {
                currentPhase = "Session terminée ! Bien joué !"
                delay(2000)
                onComplete()
            }
        } else {
            breathingPhase = GuidedBreathingPhase.PREPARE
            currentPhase = "Appuyez sur commencer quand vous êtes prêt"
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Respiration Guidée") },
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            
            // Stats Row
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${timeRemaining / 60}:${String.format("%02d", timeRemaining % 60)}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Temps",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp)
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$currentCycle",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Cycles",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Divider(
                        modifier = Modifier
                            .height(40.dp)
                            .width(1.dp)
                    )
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = when (breathingPhase) {
                                GuidedBreathingPhase.INHALE -> "4s"
                                GuidedBreathingPhase.HOLD_IN -> "2s" 
                                GuidedBreathingPhase.EXHALE -> "6s"
                                else -> "--"
                            },
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                        Text(
                            text = "Phase",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Breathing Animation
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier.size(220.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    ) {
                        drawGuidedBreathingCircle(animationValue, breathingPhase)
                    }
                    
                    // Phase indicator
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = when (breathingPhase) {
                                GuidedBreathingPhase.PREPARE -> "Prêt"
                                GuidedBreathingPhase.INHALE -> "Inspirez"
                                GuidedBreathingPhase.HOLD_IN -> "Retenez"
                                GuidedBreathingPhase.EXHALE -> "Expirez"
                                GuidedBreathingPhase.HOLD_OUT -> "Pause"
                            },
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        if (isActive) {
                            Text(
                                text = when (breathingPhase) {
                                    GuidedBreathingPhase.INHALE -> "4 sec"
                                    GuidedBreathingPhase.HOLD_IN -> "2 sec"
                                    GuidedBreathingPhase.EXHALE -> "6 sec"
                                    GuidedBreathingPhase.HOLD_OUT -> "1 sec"
                                    else -> ""
                                },
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Instruction card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    )
                ) {
                    Text(
                        text = currentPhase,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                    )
                }
            }
            
            // Control buttons
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledTonalButton(
                    onClick = { isActive = !isActive },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        if (isActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isActive) "Pause" else "Commencer")
                }
                
                OutlinedButton(
                    onClick = {
                        isActive = false
                        timeRemaining = 300
                        currentCycle = 0
                        breathingPhase = GuidedBreathingPhase.PREPARE
                        currentPhase = "Préparez-vous à commencer"
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Recommencer")
                }
            }
            
            // Instructions
            Text(
                text = "Suivez le rythme 4-2-6 : 4 secondes d'inspiration, 2 secondes de rétention, 6 secondes d'expiration. Laissez-vous guider par l'animation.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

private fun DrawScope.drawGuidedBreathingCircle(
    animationValue: Float,
    phase: GuidedBreathingPhase
) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val maxRadius = size.minDimension / 2 * 0.85f
    val currentRadius = maxRadius * animationValue
    
    // Background circle
    drawCircle(
        color = Color(0xFF3F51B5).copy(alpha = 0.1f),
        radius = maxRadius,
        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
    )
    
    // Main animated circle with gradient effect
    val mainColor = when (phase) {
        GuidedBreathingPhase.INHALE -> Color(0xFF4CAF50)
        GuidedBreathingPhase.HOLD_IN -> Color(0xFF2196F3)
        GuidedBreathingPhase.EXHALE -> Color(0xFF9C27B0)
        GuidedBreathingPhase.HOLD_OUT -> Color(0xFFFF9800)
        else -> Color(0xFF3F51B5)
    }
    
    drawCircle(
        color = mainColor.copy(alpha = 0.6f),
        radius = currentRadius,
        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
    )
    
    // Inner highlight
    drawCircle(
        color = mainColor.copy(alpha = 0.8f),
        radius = currentRadius * 0.7f,
        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
    )
    
    // Center dot
    drawCircle(
        color = mainColor,
        radius = currentRadius * 0.1f,
        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
    )
}

private enum class GuidedBreathingPhase {
    PREPARE, INHALE, HOLD_IN, EXHALE, HOLD_OUT
}