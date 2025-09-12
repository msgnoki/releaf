package com.releaf.app.ui.screens

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
import com.releaf.app.data.Technique
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyScanScreen(
    technique: Technique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isActive by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(600) } // 10 minutes
    var currentPhaseIndex by remember { mutableStateOf(0) }
    var currentPhase by remember { mutableStateOf("Appuyez sur commencer quand vous êtes prêt") }
    
    val phases = listOf(
        "Portez attention aux pieds et orteils" to 30,
        "Ressentez les jambes et les mollets" to 60,
        "Observez les hanches et le bassin" to 60,
        "Ressentez le dos et l'abdomen" to 120,
        "Portez attention à la poitrine et aux épaules" to 60,
        "Observez les bras et les mains" to 60,
        "Ressentez le cou et le visage" to 120,
        "Sentez l'ensemble du corps" to 120
    )
    
    // Timer effect
    LaunchedEffect(isActive) {
        if (isActive && currentPhaseIndex < phases.size) {
            var phaseTimeRemaining = phases[currentPhaseIndex].second
            currentPhase = phases[currentPhaseIndex].first
            
            while (phaseTimeRemaining > 0 && isActive && currentPhaseIndex < phases.size) {
                delay(1000)
                phaseTimeRemaining--
                timeRemaining--
                
                if (phaseTimeRemaining <= 0) {
                    currentPhaseIndex++
                    if (currentPhaseIndex < phases.size) {
                        phaseTimeRemaining = phases[currentPhaseIndex].second
                        currentPhase = phases[currentPhaseIndex].first
                    }
                }
            }
            
            if (currentPhaseIndex >= phases.size || timeRemaining <= 0) {
                currentPhase = "Séance terminée ! Prenez un moment pour ressentir votre corps complet"
                delay(3000)
                onComplete()
            }
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Body Scan") },
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
            
            // Timer and phase info
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
                            text = "${currentPhaseIndex + 1}/${phases.size}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "Zone corporelle",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
            
            // Main content area
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                // Body illustration icon
                Card(
                    modifier = Modifier
                        .size(120.dp),
                    shape = RoundedCornerShape(60.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Accessibility,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
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
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                if (isActive && currentPhaseIndex < phases.size) {
                    Text(
                        text = "Observez les sensations sans chercher à les modifier",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
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
                        timeRemaining = 600
                        currentPhaseIndex = 0
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
                text = "Allongez-vous confortablement et laissez-vous guider dans cette exploration consciente de votre corps.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}