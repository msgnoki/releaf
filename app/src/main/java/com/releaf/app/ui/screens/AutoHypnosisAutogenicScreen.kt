package com.releaf.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import kotlinx.coroutines.delay

@Composable
fun AutoHypnosisAutogenicScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentPhase by remember { mutableIntStateOf(0) }
    var timeRemaining by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }

    val phases = listOf(
        "Préparation et centrage" to 60,
        "Sensation de lourdeur" to 180,
        "Sensation de chaleur" to 180,
        "Régulation cardiaque" to 120,
        "Respiration calme" to 120,
        "Fraîcheur du front" to 120,
        "Retour graduel" to 60
    )

    LaunchedEffect(isRunning, currentPhase) {
        if (isRunning && currentPhase < phases.size) {
            timeRemaining = phases[currentPhase].second
            while (timeRemaining > 0 && isRunning) {
                delay(1000L)
                timeRemaining--
            }
            if (timeRemaining == 0 && currentPhase < phases.size - 1) {
                currentPhase++
            } else if (timeRemaining == 0) {
                isCompleted = true
                isRunning = false
            }
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Retour",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            Text(
                text = "Auto-hypnose Autogène",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.size(48.dp))
        }

        HorizontalDivider(
            modifier = Modifier.padding(horizontal = 16.dp),
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
        )

        // Content
        LazyColumn(
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (!isRunning && !isCompleted) {
                item {
                    IntroductionCard()
                }
                
                item {
                    PhaseOverviewCard(phases)
                }

                item {
                    StartButton(
                        onClick = { isRunning = true }
                    )
                }
            } else if (isRunning) {
                item {
                    ExerciseProgressCard(
                        currentPhase = currentPhase,
                        phases = phases,
                        timeRemaining = timeRemaining,
                        onPauseResume = { isRunning = !isRunning },
                        onStop = { 
                            isRunning = false
                            currentPhase = 0
                        },
                        isPaused = !isRunning
                    )
                }

                item {
                    PhaseInstructionCard(
                        phase = phases[currentPhase].first,
                        instructions = getPhaseInstructions(currentPhase)
                    )
                }
            } else if (isCompleted) {
                item {
                    CompletionCard(
                        onRestart = { 
                            isCompleted = false
                            currentPhase = 0
                            isRunning = true
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun IntroductionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    Icons.Default.Psychology,
                    contentDescription = null,
                    tint = Color(0xFF9C27B0),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Technique avancée",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "L'auto-hypnose autogène combine entraînement autogène et induction hypnotique pour une relaxation profonde. Cette technique vous guide à travers des sensations progressives de détente.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun PhaseOverviewCard(phases: List<Pair<String, Int>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Phases de l'exercice",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            phases.forEachIndexed { index, (phase, duration) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${index + 1}. $phase",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "${duration / 60}:${String.format("%02d", duration % 60)}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ExerciseProgressCard(
    currentPhase: Int,
    phases: List<Pair<String, Int>>,
    timeRemaining: Int,
    onPauseResume: () -> Unit,
    onStop: () -> Unit,
    isPaused: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF9C27B0).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Phase ${currentPhase + 1}/${phases.size}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = phases[currentPhase].first,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Text(
                text = "${timeRemaining / 60}:${String.format("%02d", timeRemaining % 60)}",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9C27B0),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onPauseResume,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPaused) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                    )
                ) {
                    Icon(
                        if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = if (isPaused) "Reprendre" else "Pause"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isPaused) "Reprendre" else "Pause")
                }

                OutlinedButton(onClick = onStop) {
                    Icon(Icons.Default.Stop, contentDescription = "Arrêter")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Arrêter")
                }
            }
        }
    }
}

@Composable
private fun PhaseInstructionCard(
    phase: String,
    instructions: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Instructions pour cette phase",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = instructions,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun StartButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF9C27B0)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Icon(
            Icons.Default.PlayArrow,
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Commencer l'auto-hypnose",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CompletionCard(onRestart: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Session d'auto-hypnose terminée",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Prenez quelques instants pour intégrer cette expérience de relaxation profonde.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF9C27B0)
                )
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Recommencer")
            }
        }
    }
}

private fun getPhaseInstructions(phase: Int): String {
    return when (phase) {
        0 -> "Installez-vous confortablement, fermez les yeux et prenez trois respirations profondes pour vous centrer."
        1 -> "Concentrez-vous sur vos bras et répétez mentalement : 'Mon bras droit est lourd... très lourd...' Laissez cette sensation de lourdeur s'étendre."
        2 -> "Portez attention à la circulation sanguine : 'Mon bras droit est chaud... agréablement chaud...' Ressentez cette chaleur douce."
        3 -> "Écoutez votre cœur : 'Mon cœur bat calmement et régulièrement...' Synchronisez-vous avec ce rythme apaisant."
        4 -> "Observez votre respiration : 'Ma respiration est calme et régulière...' Laissez-la se faire naturellement."
        5 -> "Ressentez votre front : 'Mon front est frais et détendu...' Cette fraîcheur apaise votre esprit."
        6 -> "Préparez-vous à revenir doucement en comptant de 1 à 5, retrouvant progressivement votre état d'éveil."
        else -> ""
    }
}