package com.example.myapplication.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Technique
import com.example.myapplication.physics.StressBallPhysics
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.*
import kotlin.random.Random

// État de l'interaction avec la balle
data class BallInteractionState(
    var isDragging: Boolean = false,
    var bounces: Int = 0,
    var compressions: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StressBallScreen(
    technique: Technique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var ballInteraction by remember { mutableStateOf(BallInteractionState()) }
    var containerSize by remember { mutableStateOf(Pair(0f, 0f)) }
    var sessionDuration by remember { mutableLongStateOf(0L) }
    var isActive by remember { mutableStateOf(false) }

    val ballRadius = 50f // Rayon en pixels Box2D
    val ballRadiusDp = 50.dp
    
    val coroutineScope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current
    
    // Moteur physique Box2D
    val physics = remember(containerSize) {
        if (containerSize.first > 0 && containerSize.second > 0) {
            StressBallPhysics(containerSize.first, containerSize.second, ballRadius)
        } else null
    }
    
    // État de la balle pour le rendu
    var ballPosition by remember { mutableStateOf(Pair(0f, 0f)) }
    var ballRotation by remember { mutableFloatStateOf(0f) }
    var squashX by remember { mutableFloatStateOf(1f) }
    var squashY by remember { mutableFloatStateOf(1f) }

    // Timer pour la durée de session
    LaunchedEffect(isActive) {
        if (isActive) {
            while (isActive) {
                delay(1000)
                sessionDuration += 1000
            }
        }
    }

    // Boucle de physique Box2D
    LaunchedEffect(physics) {
        physics?.let { physicsEngine ->
            while (true) {
                delay(16) // ~60fps
                
                if (!ballInteraction.isDragging) {
                    // Mise à jour de la physique
                    physicsEngine.step(0.016f)
                    
                    // Récupération de l'état de la balle
                    ballPosition = physicsEngine.getBallPosition()
                    ballRotation = physicsEngine.getBallRotation()
                    squashX = physicsEngine.squashX
                    squashY = physicsEngine.squashY
                    
                    // Détection des rebonds pour haptic feedback
                    val velocity = physicsEngine.getBallVelocity()
                    val velocityMagnitude = sqrt(velocity.first * velocity.first + velocity.second * velocity.second)
                    
                    if (velocityMagnitude > 20f && squashX < 0.9f || squashY < 0.9f) {
                        ballInteraction = ballInteraction.copy(bounces = ballInteraction.bounces + 1)
                        hapticFeedback.performHapticFeedback(androidx.compose.ui.hapticfeedback.HapticFeedbackType.LongPress)
                    }
                }
            }
        }
    }

    fun formatDuration(ms: Long): String {
        val minutes = ms / 60000
        val seconds = (ms % 60000) / 1000
        return "${minutes}:${seconds.toString().padStart(2, '0')}"
    }

    fun startSession() {
        isActive = true
        sessionDuration = 0L
    }

    val resetBall = {
        physics?.resetBall()
        ballInteraction = ballInteraction.copy(
            bounces = 0,
            compressions = 0
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Balle anti-stress") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )

        // Zone de jeu interactive
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xFF8A9AA8),
                            Color(0xFF64727C),
                            Color(0xFF545F68)
                        ),
                        radius = 800f
                    )
                )
                .onSizeChanged { size ->
                    containerSize = Pair(size.width.toFloat(), size.height.toFloat())
                }
        ) {
            // Pattern de fond subtil
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.White.copy(alpha = 0.1f),
                                Color.Transparent
                            ),
                            center = Offset(containerSize.first * 0.3f, containerSize.second * 0.3f),
                            radius = 200f
                        )
                    )
            )

            // La balle interactive avec Box2D
            if (containerSize.first > 0 && containerSize.second > 0 && physics != null) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(Unit) {
                            detectDragGestures(
                                onDragStart = {
                                    ballInteraction = ballInteraction.copy(
                                        isDragging = true,
                                        compressions = ballInteraction.compressions + 1
                                    )
                                    physics.stopBall()
                                    if (!isActive) startSession()
                                },
                                onDragEnd = {
                                    ballInteraction = ballInteraction.copy(isDragging = false)
                                },
                                onDrag = { change, _ ->
                                    val deltaX = change.position.x - change.previousPosition.x
                                    val deltaY = change.position.y - change.previousPosition.y
                                    
                                    // Application de la force Box2D
                                    physics.applyForce(deltaX * 10f, deltaY * 10f)
                                    
                                    // Mise à jour immédiate de la position pendant le drag
                                    if (ballInteraction.isDragging) {
                                        val currentPos = physics.getBallPosition()
                                        val newX = (currentPos.first + deltaX).coerceIn(ballRadius, containerSize.first - ballRadius)
                                        val newY = (currentPos.second + deltaY).coerceIn(ballRadius, containerSize.second - ballRadius)
                                        physics.setBallPosition(newX, newY)
                                    }
                                }
                            )
                        }
                ) {
                    // Rendu de la balle avec Box2D
                    drawStressBall(
                        center = Offset(ballPosition.first, ballPosition.second),
                        radius = ballRadius,
                        rotation = ballRotation,
                        squashX = squashX,
                        squashY = squashY
                    )
            }
        }

            // Instructions flottantes
            if (!isActive && sessionDuration == 0L) {
                Card(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.7f)
                    )
                ) {
                    Text(
                        text = "Faites glisser la balle pour commencer",
                        modifier = Modifier.padding(12.dp),
                        color = Color.White,
                        fontSize = 14.sp
                    )
                }
            }

            // Statistiques en temps réel
            if (isActive) {
                Card(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.Black.copy(alpha = 0.7f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(12.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(
                            text = formatDuration(sessionDuration),
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${ballInteraction.bounces} rebonds",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                        Text(
                            text = "${ballInteraction.compressions} pressions",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Contrôles et informations
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                IntroductionSection(
                    technique = technique,
                    isActive = isActive,
                    sessionDuration = sessionDuration,
                    ballInteraction = ballInteraction,
                    onReset = resetBall,
                    formatDuration = ::formatDuration
                )
            }

            item {
                EducationalSection()
            }

            // Bouton terminer (après utilisation suffisante)
            if (sessionDuration >= 30000) { // 30 secondes minimum
                item {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4CAF50)
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

// Fonction pour gérer les collisions et rebonds
private fun checkBoundsAndCollide(
    x: Float, y: Float,
    velX: Float, velY: Float,
    containerSize: Pair<Int, Int>,
    ballRadius: Float
): Tuple5<Float, Float, Float, Float, Boolean> {
    var newX = x
    var newY = y
    var newVelX = velX
    var newVelY = velY
    var bounced = false
    
    val bounceDamping = 0.7f
    
    // Collision avec les bords
    if (newX - ballRadius < 0) {
        newX = ballRadius
        newVelX = -newVelX * bounceDamping
        bounced = true
    } else if (newX + ballRadius > containerSize.first) {
        newX = containerSize.first - ballRadius
        newVelX = -newVelX * bounceDamping
        bounced = true
    }
    
    if (newY - ballRadius < 0) {
        newY = ballRadius
        newVelY = -newVelY * bounceDamping
        bounced = true
    } else if (newY + ballRadius > containerSize.second) {
        newY = containerSize.second - ballRadius
        newVelY = -newVelY * bounceDamping
        bounced = true
    }
    
    return Tuple5(newX, newY, newVelX, newVelY, bounced)
}

// Classe utilitaire pour retourner 5 valeurs
private data class Tuple5<A, B, C, D, E>(
    val first: A,
    val second: B,
    val third: C,
    val fourth: D,
    val fifth: E
)

@Composable
private fun IntroductionSection(
    technique: Technique,
    isActive: Boolean,
    sessionDuration: Long,
    ballInteraction: BallInteractionState,
    onReset: () -> Unit,
    formatDuration: (Long) -> String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Sports,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF4CAF50)
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

        if (isActive && sessionDuration > 0) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF4CAF50).copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Session en cours",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF4CAF50)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        StatItem(
                            value = formatDuration(sessionDuration),
                            label = "Durée",
                            color = Color(0xFF4CAF50)
                        )
                        StatItem(
                            value = ballInteraction.bounces.toString(),
                            label = "Rebonds",
                            color = Color(0xFF2196F3)
                        )
                        StatItem(
                            value = ballInteraction.compressions.toString(),
                            label = "Pressions",
                            color = Color(0xFFFF9800)
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun StatItem(
    value: String,
    label: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
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
                        Icons.Default.Psychology,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Libération physique du stress",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "L'engagement tactile avec des objets de compression aide à canaliser l'énergie nerveuse et l'anxiété. L'action répétitive de presser et relâcher active votre système nerveux parasympathique, favorisant la relaxation.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        // Conseils d'utilisation
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF2196F3).copy(alpha = 0.1f)
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
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Techniques d'utilisation",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                val techniques = listOf(
                    "Faites glisser doucement pour un effet calmant",
                    "Lancez avec force pour évacuer les tensions",
                    "Observez les rebonds pour vous concentrer sur le moment présent",
                    "Utilisez les mouvements répétitifs comme une méditation active",
                    "Prenez des respirations profondes en manipulant la balle"
                )

                techniques.forEach { technique ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Circle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier
                                .size(6.dp)
                                .padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = technique,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }

        // Bénéfices
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFFF9800).copy(alpha = 0.1f)
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Icon(
                        Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = Color(0xFFFF9800),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bénéfices thérapeutiques",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                val benefits = listOf(
                    "Réduction immédiate de la tension musculaire",
                    "Amélioration de la concentration et de l'attention",
                    "Libération d'endorphines naturelles",
                    "Diminution du cortisol (hormone du stress)"
                )

                benefits.forEach { benefit ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            tint = Color(0xFFFF9800),
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
    }
}

/**
 * Fonction de rendu Box2D pour la balle anti-stress
 */
private fun DrawScope.drawStressBall(
    center: Offset,
    radius: Float,
    rotation: Float,
    squashX: Float,
    squashY: Float
) {
    // Transformation avec déformation
    scale(scaleX = squashX, scaleY = squashY, pivot = center) {
        // Ombre de la balle
        drawCircle(
            color = androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.2f),
            radius = radius + 5f,
            center = center.copy(x = center.x + 3f, y = center.y + 3f)
        )
        
        // Corps principal de la balle avec gradient
        val ballGradient = Brush.radialGradient(
            colors = listOf(
                androidx.compose.ui.graphics.Color(0xFF4CAF50),
                androidx.compose.ui.graphics.Color(0xFF2E7D32),
                androidx.compose.ui.graphics.Color(0xFF1B5E20)
            ),
            center = Offset(center.x - radius * 0.3f, center.y - radius * 0.3f),
            radius = radius * 1.2f
        )
        
        drawCircle(
            brush = ballGradient,
            radius = radius,
            center = center
        )
        
        // Surbrillance
        val highlightOffset = Offset(
            center.x - radius * 0.4f,
            center.y - radius * 0.4f
        )
        
        drawCircle(
            color = androidx.compose.ui.graphics.Color.White.copy(alpha = 0.4f),
            radius = radius * 0.3f,
            center = highlightOffset
        )
        
        // Ombre interne pour l'effet 3D
        val innerShadowGradient = Brush.radialGradient(
            colors = listOf(
                androidx.compose.ui.graphics.Color.Transparent,
                androidx.compose.ui.graphics.Color.Black.copy(alpha = 0.3f)
            ),
            center = Offset(center.x + radius * 0.3f, center.y + radius * 0.3f),
            radius = radius * 0.8f
        )
        
        drawCircle(
            brush = innerShadowGradient,
            radius = radius,
            center = center
        )
    }
}