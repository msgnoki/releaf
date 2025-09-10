package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Technique
import com.example.myapplication.ui.components.BreathingAnimation
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Breathing478Screen(
    technique: Technique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isActive by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(240) } // 4 minutes for 4-7-8 breathing
    var currentPhase by remember { mutableStateOf("Appuyez sur commencer quand vous êtes prêt") }
    var completedCycles by remember { mutableStateOf(0) }
    
    // Timer effect
    LaunchedEffect(isActive) {
        if (isActive) {
            while (timeRemaining > 0 && isActive) {
                delay(1000)
                timeRemaining--
                
                // Count cycles (each cycle is 19 seconds: 4s inhale + 7s hold + 8s exhale)
                if (timeRemaining % 19 == 0) {
                    completedCycles = (240 - timeRemaining) / 19
                }
            }
            
            if (timeRemaining <= 0) {
                currentPhase = "Exercice terminé ! Profitez de cette sensation de calme"
                delay(2000)
                onComplete()
            }
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Respiration 4-7-8") },
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
            
            // Timer and cycle info
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
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Temps restant",
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
                            text = "$completedCycles",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Cycles",
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
                BreathingAnimation(
                    isActive = isActive,
                    onPhaseChange = { phase -> currentPhase = phase }
                )
                
                Spacer(modifier = Modifier.height(32.dp))
                
                // Current phase instruction
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
                        timeRemaining = 240
                        completedCycles = 0
                        currentPhase = "Appuyez sur commencer quand vous êtes prêt"
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
            
            // Instructions
            Text(
                text = "Inspirez 4s, retenez 7s, expirez 8s. Technique du Dr. Weil parfaite pour l'endormissement et l'apaisement.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}