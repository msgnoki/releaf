package com.example.myapplication.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Technique
import kotlinx.coroutines.delay

// Modèle de données pour les bulles
data class Bubble(
    val id: Int,
    val isPopped: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StressReliefBubblesScreen(
    technique: Technique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    // État des bulles
    var bubbles by remember {
        mutableStateOf((1..64).map { Bubble(it) })
    }
    var poppedCount by remember { mutableIntStateOf(0) }
    var totalPopped by remember { mutableIntStateOf(0) }

    val hapticFeedback = LocalHapticFeedback.current

    fun popBubble(bubbleId: Int) {
        val bubbleIndex = bubbles.indexOfFirst { it.id == bubbleId }
        if (bubbleIndex != -1 && !bubbles[bubbleIndex].isPopped) {
            bubbles = bubbles.toMutableList().apply {
                set(bubbleIndex, get(bubbleIndex).copy(isPopped = true))
            }
            poppedCount++
            totalPopped++
            
            // Feedback haptique
            hapticFeedback.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
        }
    }

    fun resetBubbles() {
        bubbles = (1..64).map { Bubble(it) }
        poppedCount = 0
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Bulles anti-stress") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Introduction
            item {
                IntroductionSection(
                    technique = technique,
                    poppedCount = poppedCount,
                    totalPopped = totalPopped
                )
            }

            // Grille de bulles
            item {
                BubbleGridSection(
                    bubbles = bubbles,
                    onBubblePop = ::popBubble
                )
            }

            // Bouton de reset
            item {
                ResetButtonSection(
                    onReset = ::resetBubbles,
                    poppedCount = poppedCount
                )
            }

            // Section éducative
            item {
                EducationalSection()
            }

            // Bouton terminer
            if (totalPopped >= 20) {
                item {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF14B8A6)
                        )
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Terminer l'exercice")
                    }
                }
            }
        }
    }
}

@Composable
private fun IntroductionSection(
    technique: Technique,
    poppedCount: Int,
    totalPopped: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Circle,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF14B8A6)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = technique.name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = technique.description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Statistiques
        Row(
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            StatCard(
                value = poppedCount.toString(),
                label = "Cette session",
                color = Color(0xFF14B8A6)
            )
            StatCard(
                value = totalPopped.toString(),
                label = "Total éclaté",
                color = Color(0xFF06B6D4)
            )
        }
    }
}

@Composable
private fun StatCard(
    value: String,
    label: String,
    color: Color
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun BubbleGridSection(
    bubbles: List<Bubble>,
    onBubblePop: (Int) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF14B8A6).copy(alpha = 0.05f)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Éclatez les bulles pour relâcher le stress",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(8),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.height(320.dp)
            ) {
                items(bubbles) { bubble ->
                    BubbleItem(
                        bubble = bubble,
                        onPop = { onBubblePop(bubble.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun BubbleItem(
    bubble: Bubble,
    onPop: () -> Unit
) {
    // Animation de scale lors du clic
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 1.15f else 1f,
        animationSpec = tween(100),
        label = "bubble_scale"
    )

    val gradientColors = if (bubble.isPopped) {
        listOf(
            Color(0xFF14B8A6).copy(alpha = 0.3f),
            Color(0xFF06B6D4).copy(alpha = 0.3f)
        )
    } else {
        listOf(
            Color(0xFF6AE7FF),
            Color(0xFF408BB7)
        )
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .scale(scale)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(gradientColors),
                shape = CircleShape
            )
            .clickable(
                enabled = !bubble.isPopped,
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClickLabel = if (!bubble.isPopped) "Éclater la bulle" else null
            ) {
                if (!bubble.isPopped) {
                    isPressed = true
                    onPop()
                }
            }
    ) {
        // Animation de "pop" avec délai pour l'effet visuel
        LaunchedEffect(isPressed) {
            if (isPressed) {
                delay(100)
                isPressed = false
            }
        }

        // Effet visuel pour les bulles éclatées
        if (bubble.isPopped) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            listOf(
                                Color.Transparent,
                                Color(0xFF14B8A6).copy(alpha = 0.2f)
                            )
                        ),
                        shape = CircleShape
                    )
            )
        }
    }
}

@Composable
private fun ResetButtonSection(
    onReset: () -> Unit,
    poppedCount: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (poppedCount > 0) {
            Text(
                text = "Excellent ! Vous avez éclaté $poppedCount bulle${if (poppedCount > 1) "s" else ""}",
                fontSize = 14.sp,
                color = Color(0xFF10B981),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 12.dp)
            )
        }

        Button(
            onClick = onReset,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF14B8A6)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Réinitialiser les bulles",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun EducationalSection() {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Comment ça fonctionne
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        Icons.Default.TouchApp,
                        contentDescription = null,
                        tint = Color(0xFF14B8A6),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Comment fonctionne le soulagement tactile du stress",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "La stimulation tactile à travers des mouvements répétitifs de popping fournit un retour sensoriel immédiat qui aide à rediriger l'énergie anxieuse. L'action satisfaisante engage votre système nerveux dans une activité calmante et concentrée.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        // Quand l'utiliser
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF3B82F6).copy(alpha = 0.1f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Quand utiliser",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                val useCases = listOf(
                    "Pendant les moments anxieux lorsque vous avez besoin d'une distraction immédiate",
                    "Quand vous vous sentez agité ou nerveux",
                    "Pendant les appels téléphoniques ou les périodes d'attente",
                    "Comme une réinitialisation rapide du stress entre les tâches"
                )

                useCases.forEach { useCase ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Circle,
                            contentDescription = null,
                            tint = Color(0xFF14B8A6),
                            modifier = Modifier
                                .size(6.dp)
                                .padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = useCase,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Ce que vous remarquerez
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF10B981).copy(alpha = 0.1f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Ce que vous remarquerez",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                val benefits = listOf(
                    "Une sensation immédiate de soulagement et de satisfaction",
                    "Énergie nerveuse redirigée vers une activité calmante",
                    "Amélioration de la concentration et de l'attention",
                    "Réduction rapide du stress et de la tension"
                )

                benefits.forEach { benefit ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier
                                .size(16.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = benefit,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }

        // Conseils
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4F46E5).copy(alpha = 0.1f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Icon(
                        Icons.Default.Lightbulb,
                        contentDescription = null,
                        tint = Color(0xFF4F46E5),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Conseils pour une utilisation optimale",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TipCard(
                        title = "Soyez attentif",
                        description = "Concentrez-vous sur la sensation de chaque 'pop' pour maximiser l'effet relaxant.",
                        modifier = Modifier.weight(1f)
                    )
                    TipCard(
                        title = "Prenez des pauses",
                        description = "Utilisez cet outil comme une pause courte entre les activités stressantes.",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TipCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF4F46E5),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            fontSize = 10.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 12.sp
        )
    }
}