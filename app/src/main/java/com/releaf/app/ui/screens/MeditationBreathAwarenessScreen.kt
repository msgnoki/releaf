package com.releaf.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun MeditationBreathAwarenessScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedDuration by remember { mutableIntStateOf(5) } // 5 minutes par défaut
    var timeRemaining by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }
    var showReminders by remember { mutableStateOf(true) }
    var reminderCount by remember { mutableIntStateOf(0) }

    // Gestion des rappels doux
    LaunchedEffect(isRunning) {
        if (isRunning) {
            timeRemaining = selectedDuration * 60
            while (timeRemaining > 0 && isRunning) {
                delay(1000L)
                timeRemaining--
                
                // Rappel doux toutes les 2 minutes si activé
                if (showReminders && timeRemaining > 0 && (selectedDuration * 60 - timeRemaining) % 120 == 0 && reminderCount < 3) {
                    reminderCount++
                }
            }
            if (timeRemaining == 0) {
                isCompleted = true
                isRunning = false
            }
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFFFFF9C4).copy(alpha = 0.3f), Color.White)
                )
            )
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
                text = "Méditation Conscience du Souffle",
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
                    DurationSelectionCard(
                        selectedDuration = selectedDuration,
                        onDurationSelected = { selectedDuration = it }
                    )
                }

                item {
                    SettingsCard(
                        showReminders = showReminders,
                        onReminderToggle = { showReminders = it }
                    )
                }

                item {
                    InstructionsCard()
                }

                item {
                    StartButton(
                        onClick = { 
                            isRunning = true
                            reminderCount = 0
                        }
                    )
                }
            } else if (isRunning) {
                item {
                    MeditationProgressCard(
                        timeRemaining = timeRemaining,
                        totalDuration = selectedDuration * 60,
                        onPauseResume = { isRunning = !isRunning },
                        onStop = { 
                            isRunning = false
                            reminderCount = 0
                        },
                        isPaused = !isRunning
                    )
                }

                item {
                    BreathingIndicatorCard(isRunning = isRunning)
                }

                if (showReminders && reminderCount > 0) {
                    item {
                        GentleReminderCard(
                            onDismiss = { reminderCount = 0 }
                        )
                    }
                }
            } else if (isCompleted) {
                item {
                    CompletionCard(
                        duration = selectedDuration,
                        onRestart = { 
                            isCompleted = false
                            isRunning = true
                            reminderCount = 0
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
                    Icons.Default.SelfImprovement,
                    contentDescription = null,
                    tint = Color(0xFFFFC107),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Méditation de pleine conscience",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "Cette méditation vous apprend à porter attention à votre respiration naturelle pour cultiver la pleine conscience. Simple et accessible, elle développe votre capacité d'attention et de présence au moment présent.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun DurationSelectionCard(
    selectedDuration: Int,
    onDurationSelected: (Int) -> Unit
) {
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
                text = "Durée de la méditation",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(
                    3 to "Débutant",
                    5 to "Standard", 
                    10 to "Intermédiaire",
                    15 to "Avancé"
                ).forEach { (duration, label) ->
                    FilterChip(
                        onClick = { onDurationSelected(duration) },
                        label = { 
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "${duration} min",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        selected = selectedDuration == duration,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFFFC107).copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsCard(
    showReminders: Boolean,
    onReminderToggle: (Boolean) -> Unit
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
                text = "Options",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Rappels doux",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Switch(
                    checked = showReminders,
                    onCheckedChange = onReminderToggle,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFFFC107)
                    )
                )
            }
            
            if (showReminders) {
                Text(
                    text = "Des rappels subtils apparaîtront si votre esprit divague",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
    }
}

@Composable
private fun InstructionsCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Comment méditer",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            val instructions = listOf(
                "Installez-vous confortablement, dos droit mais détendu",
                "Fermez les yeux et portez attention à votre respiration naturelle",
                "Observez les sensations aux narines ou le mouvement du ventre",
                "Quand votre esprit divague, revenez doucement au souffle",
                "Pas de jugement, c'est normal que l'esprit s'évade"
            )

            instructions.forEach { instruction ->
                Row(
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        color = Color(0xFFFFC107),
                        modifier = Modifier.padding(end = 8.dp, top = 2.dp)
                    )
                    Text(
                        text = instruction,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        lineHeight = 20.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun MeditationProgressCard(
    timeRemaining: Int,
    totalDuration: Int,
    onPauseResume: () -> Unit,
    onStop: () -> Unit,
    isPaused: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFC107).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (isPaused) "En pause" else "En méditation",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "${timeRemaining / 60}:${String.format("%02d", timeRemaining % 60)}",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC107),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Barre de progression
            LinearProgressIndicator(
                progress = { 1f - (timeRemaining.toFloat() / totalDuration) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                color = Color(0xFFFFC107),
                trackColor = Color(0xFFFFC107).copy(alpha = 0.2f),
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
private fun BreathingIndicatorCard(isRunning: Boolean) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Indicateur de respiration animé
            val infiniteTransition = rememberInfiniteTransition(label = "breathing")
            val scale by infiniteTransition.animateFloat(
                initialValue = 0.8f,
                targetValue = 1.2f,
                animationSpec = infiniteRepeatable(
                    animation = tween(4000, easing = EaseInOut),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "scale"
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFFC107).copy(alpha = if (isRunning) 0.3f else 0.1f),
                                Color(0xFFFFC107).copy(alpha = if (isRunning) 0.1f else 0.05f)
                            )
                        )
                    )
                    .then(if (isRunning) Modifier else Modifier),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size((60 * if (isRunning) scale else 1f).dp)
                        .clip(CircleShape)
                        .background(Color(0xFFFFC107).copy(alpha = 0.5f))
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Portez attention à votre respiration naturelle",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun GentleReminderCard(onDismiss: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Lightbulb,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Revenez doucement au souffle",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onDismiss) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Fermer",
                    modifier = Modifier.size(16.dp)
                )
            }
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
            containerColor = Color(0xFFFFC107)
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
            text = "Commencer la méditation",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun CompletionCard(
    duration: Int,
    onRestart: () -> Unit
) {
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
                text = "Méditation terminée",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Vous avez médité pendant $duration minute${if (duration > 1) "s" else ""}. Prenez un moment pour intégrer cette expérience de pleine conscience.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFFFC107)
                )
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Recommencer")
            }
        }
    }
}