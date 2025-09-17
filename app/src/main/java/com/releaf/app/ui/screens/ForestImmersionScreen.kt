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
fun ForestImmersionScreen(
    onBackClick: () -> Unit,
    onComplete: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentScene by remember { mutableIntStateOf(0) }
    var timeRemaining by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var isCompleted by remember { mutableStateOf(false) }
    var selectedDuration by remember { mutableIntStateOf(8) } // 8 minutes par défaut

    val scenes = listOf(
        "Approche de la forêt" to 60,
        "Entrée sous la canopée" to 90,
        "Sentier forestier" to 150,
        "Découverte de la clairière" to 180,
        "Ruisseau cristallin" to 120,
        "Repos en nature" to 180,
        "Retour apaisé" to 60
    )

    // Ajuster les durées selon la sélection
    val adjustedScenes = scenes.map { (name, baseDuration) ->
        name to (baseDuration * selectedDuration / 8)
    }

    LaunchedEffect(isRunning, currentScene) {
        if (isRunning && currentScene < adjustedScenes.size) {
            timeRemaining = adjustedScenes[currentScene].second
            while (timeRemaining > 0 && isRunning) {
                delay(1000L)
                timeRemaining--
            }
            if (timeRemaining == 0 && currentScene < adjustedScenes.size - 1) {
                currentScene++
            } else if (timeRemaining == 0) {
                isCompleted = true
                isRunning = false
            }
        }
    }
    
    LaunchedEffect(isCompleted) {
        if (isCompleted) {
            onComplete()
        }
    }

    // Background dynamique selon la scène
    val backgroundColor = remember(currentScene, isRunning) {
        when {
            !isRunning -> Color(0xFFF5F5DC) // Beige initial
            currentScene <= 1 -> Color(0xFFE8F5E8) // Vert très clair
            currentScene <= 3 -> Color(0xFF81C784) // Vert forêt
            currentScene == 4 -> Color(0xFF4FC3F7) // Bleu ruisseau
            else -> Color(0xFFAED581) // Vert clairière
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(backgroundColor.copy(alpha = 0.3f), Color.White)
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
                text = "Immersion Forêt",
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
                    PreparationCard()
                }

                item {
                    StartButton(
                        onClick = { isRunning = true }
                    )
                }
            } else if (isRunning) {
                item {
                    ImmersionProgressCard(
                        currentScene = currentScene,
                        scenes = adjustedScenes,
                        timeRemaining = timeRemaining,
                        onPauseResume = { isRunning = !isRunning },
                        onStop = { 
                            isRunning = false
                            currentScene = 0
                        },
                        isPaused = !isRunning
                    )
                }

                item {
                    SceneVisualizationCard(
                        scene = adjustedScenes[currentScene].first,
                        description = getSceneDescription(currentScene),
                        currentScene = currentScene
                    )
                }
            } else if (isCompleted) {
                item {
                    CompletionCard(
                        onRestart = { 
                            isCompleted = false
                            currentScene = 0
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
                    Icons.Default.Park,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Évasion en nature",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Text(
                text = "Cette immersion vous transporte mentalement dans une forêt apaisante. Vous découvrirez un sentier forestier, une clairière baignée de lumière et un ruisseau cristallin. Idéal pour réduire le stress urbain et vous reconnecter avec la nature.",
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
                text = "Durée de l'immersion",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(3, 8, 15).forEach { duration ->
                    FilterChip(
                        onClick = { onDurationSelected(duration) },
                        label = { 
                            Text(
                                text = "${duration} min",
                                fontSize = 14.sp
                            )
                        },
                        selected = selectedDuration == duration,
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF4CAF50).copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    }
}

@Composable
private fun PreparationCard() {
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
                text = "Préparation",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = "• Installez-vous confortablement dans un endroit calme\n• Utilisez un casque pour une meilleure immersion\n• Fermez les yeux ou gardez-les mi-clos\n• Laissez votre imagination créer les détails",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun ImmersionProgressCard(
    currentScene: Int,
    scenes: List<Pair<String, Int>>,
    timeRemaining: Int,
    onPauseResume: () -> Unit,
    onStop: () -> Unit,
    isPaused: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Scène ${currentScene + 1}/${scenes.size}",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Text(
                text = scenes[currentScene].first,
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
                color = Color(0xFF4CAF50),
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
private fun SceneVisualizationCard(
    scene: String,
    description: String,
    currentScene: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icône de scène animée
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        when (currentScene) {
                            0, 1 -> Color(0xFF81C784).copy(alpha = 0.2f)
                            2, 3 -> Color(0xFF4CAF50).copy(alpha = 0.2f)
                            4 -> Color(0xFF4FC3F7).copy(alpha = 0.2f)
                            else -> Color(0xFFAED581).copy(alpha = 0.2f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    when (currentScene) {
                        0, 1 -> Icons.Default.Forest
                        2, 3 -> Icons.Default.Park
                        4 -> Icons.Default.Water
                        else -> Icons.Default.WbSunny
                    },
                    contentDescription = null,
                    tint = when (currentScene) {
                        0, 1 -> Color(0xFF81C784)
                        2, 3 -> Color(0xFF4CAF50)
                        4 -> Color(0xFF4FC3F7)
                        else -> Color(0xFFFFB74D)
                    },
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = description,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                lineHeight = 24.sp,
                fontWeight = FontWeight.Medium
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
            containerColor = Color(0xFF4CAF50)
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
            text = "Commencer l'immersion",
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
                text = "Immersion terminée",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Gardez cette sensation de paix et de connexion avec la nature.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onRestart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4CAF50)
                )
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Recommencer")
            }
        }
    }
}

private fun getSceneDescription(scene: Int): String {
    return when (scene) {
        0 -> "Vous vous dirigez vers une forêt luxuriante. L'air devient plus frais, les bruits de la ville s'estompent peu à peu..."
        1 -> "Vous pénétrez sous la canopée verdoyante. La lumière filtrée danse à travers les feuilles, créant un jeu d'ombres apaisantes..."
        2 -> "Vos pas foulent doucement la mousse et les feuilles mortes sur le sentier forestier. Les chants d'oiseaux vous accompagnent..."
        3 -> "Une magnifique clairière s'ouvre devant vous, baignée d'une lumière dorée et chaleureuse. L'endroit respire la sérénité..."
        4 -> "Un ruisseau cristallin coule doucement. Le murmure de l'eau et la fraîcheur apaisante vous invitent à la détente..."
        5 -> "Vous vous installez confortablement dans ce havre de paix. Tous vos sens sont en harmonie avec la nature environnante..."
        6 -> "Il est temps de revenir, en gardant précieusement cette sensation de calme et de connexion avec la nature..."
        else -> ""
    }
}