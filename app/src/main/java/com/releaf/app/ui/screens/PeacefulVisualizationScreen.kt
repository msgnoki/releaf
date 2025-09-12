package com.releaf.app.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.releaf.app.data.Technique
import kotlinx.coroutines.delay
import kotlin.math.*
import kotlin.random.Random

// Scènes de visualisation
data class VisualizationScene(
    val name: String,
    val description: String,
    val soundscape: String,
    val atmosphere: String,
    val icon: ImageVector,
    val color1: Color,
    val color2: Color,
    val environmentType: String,
    val guidance: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeacefulVisualizationScreen(
    technique: Technique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var exerciseStarted by remember { mutableStateOf(false) }
    var exerciseCompleted by remember { mutableStateOf(false) }
    var selectedSceneIndex by remember { mutableIntStateOf(0) }
    var currentGuidanceIndex by remember { mutableIntStateOf(0) }
    var currentGuidanceText by remember { mutableStateOf("") }
    var isTransitioning by remember { mutableStateOf(false) }
    
    // 5 scènes de visualisation
    val scenes = remember {
        listOf(
            VisualizationScene(
                name = "Sommet de montagne au lever du soleil",
                description = "Panorama majestueux avec lumière dorée",
                soundscape = "Sons de la nature alpine",
                atmosphere = "Paisible et inspirant",
                icon = Icons.Default.Landscape,
                color1 = Color(0xFF87CEEB), // Sky blue
                color2 = Color(0xFFFFE4B5), // Moccasin
                environmentType = "peaks",
                guidance = listOf(
                    "Vous êtes au sommet d'une montagne majestueuse. Le soleil se lève lentement à l'horizon, peignant le ciel de couleurs dorées.",
                    "Sentez l'air pur et frais remplir vos poumons. Chaque respiration vous apporte une sensation de clarté et de paix.",
                    "Observez les vallées qui s'étendent à perte de vue en contrebas. Vous êtes connecté à la grandeur de la nature.",
                    "Les premiers rayons du soleil réchauffent votre visage. Cette chaleur apporte une sensation de bien-être profond.",
                    "Vous ressentez un sentiment d'accomplissement et de sérénité. Cette paix vous accompagnera longtemps après cet exercice."
                )
            ),
            VisualizationScene(
                name = "Bosquet forestier tranquille",
                description = "Forêt verdoyante avec lumière filtrée",
                soundscape = "Chant des oiseaux, bruissement des feuilles",
                atmosphere = "Apaisant et naturel",
                icon = Icons.Default.Park,
                color1 = Color(0xFF228B22), // Forest green
                color2 = Color(0xFFF0E68C), // Khaki
                environmentType = "trees",
                guidance = listOf(
                    "Vous marchez dans une forêt ancienne et paisible. Les grands arbres créent une cathédrale naturelle au-dessus de vous.",
                    "La lumière dorée filtre à travers les feuilles, créant des motifs dansants sur le sol de mousse.",
                    "Vous entendez le doux chant des oiseaux et le murmure d'un ruisseau qui coule près d'ici.",
                    "L'air est pur et parfumé par les senteurs de la forêt. Chaque respiration vous connecte à cette énergie vitale.",
                    "Vous vous sentez en harmonie avec la nature qui vous entoure. Cette connexion nourrit votre âme profondément."
                )
            ),
            VisualizationScene(
                name = "Plage océanique paisible",
                description = "Rivage sablonneux avec vagues apaisantes",
                soundscape = "Sons des vagues, brise marine",
                atmosphere = "Relaxant et libérateur",
                icon = Icons.Default.Waves,
                color1 = Color(0xFF4682B4), // Steel blue
                color2 = Color(0xFFF5DEB3), // Wheat
                environmentType = "waves",
                guidance = listOf(
                    "Vous êtes sur une plage de sable fin, face à un océan d'un bleu cristallin. Les vagues arrivent et repartent en douceur.",
                    "Le rythme hypnotique des vagues synchronise votre respiration. Inspirez avec les vagues qui arrivent, expirez quand elles repartent.",
                    "Sentez le sable chaud sous vos pieds et la brise marine caresser votre peau. L'air marin purifie vos pensées.",
                    "Chaque vague emporte vos soucis vers l'horizon infini. Vous vous sentez libre et détendu.",
                    "L'océan vous offre sa paix éternelle. Cette sérénité reste avec vous, comme une ressource intérieure précieuse."
                )
            ),
            VisualizationScene(
                name = "Paradis de jardin serein",
                description = "Jardin luxuriant aux couleurs vives",
                soundscape = "Bourdonnement des abeilles, eau qui coule",
                atmosphere = "Harmonieux et coloré",
                icon = Icons.Default.LocalFlorist,
                color1 = Color(0xFF9370DB), // Medium purple
                color2 = Color(0xFFFF69B4), // Hot pink
                environmentType = "garden",
                guidance = listOf(
                    "Vous vous promenez dans un jardin paradisiaque rempli de fleurs aux couleurs éclatantes et de parfums enivrants.",
                    "Une fontaine cristalline chante doucement au centre du jardin. Son eau pure reflète les couleurs du ciel.",
                    "Papillons et abeilles dansent de fleur en fleur dans une harmonie parfaite avec la nature.",
                    "Vous vous asseyez près de la fontaine et fermez les yeux, laissant tous vos sens s'imprégner de cette beauté.",
                    "Ce jardin intérieur est un refuge que vous pouvez revisiter à tout moment. Il représente la beauté que vous portez en vous."
                )
            ),
            VisualizationScene(
                name = "Prairie étoilée nocturne",
                description = "Ciel nocturne parsemé d'étoiles brillantes",
                soundscape = "Silence paisible, brise nocturne",
                atmosphere = "Mystique et contemplatif",
                icon = Icons.Default.NightsStay,
                color1 = Color(0xFF191970), // Midnight blue
                color2 = Color(0xFFE6E6FA), // Lavender
                environmentType = "stars",
                guidance = listOf(
                    "Vous êtes allongé dans une prairie sous un ciel étoilé magnifique. Des milliers d'étoiles scintillent au-dessus de vous.",
                    "La Voie lactée s'étend comme un fleuve de lumière à travers le ciel nocturne. Vous contemplez l'immensité de l'univers.",
                    "Une brise douce caresse votre peau tandis que vous vous connectez à cette vastitude cosmique.",
                    "Vous réalisez que vous faites partie de cette grande danse cosmique. Cette connexion vous apporte une paix profonde.",
                    "Emportez avec vous cette sensation d'appartenance à quelque chose de plus grand. Vous n'êtes jamais vraiment seul."
                )
            )
        )
    }
    
    val currentScene = scenes[selectedSceneIndex]
    
    // Animation continue pour les environnements
    val infiniteTransition = rememberInfiniteTransition(label = "environment_animation")
    val animationValue by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000, easing = LinearEasing)
        ),
        label = "rotation"
    )
    
    // Gestion de la séquence de guidance
    LaunchedEffect(exerciseStarted, currentGuidanceIndex) {
        if (exerciseStarted && !exerciseCompleted && currentGuidanceIndex < currentScene.guidance.size) {
            isTransitioning = true
            delay(500)
            currentGuidanceText = currentScene.guidance[currentGuidanceIndex]
            isTransitioning = false
            delay(8000) // 8 secondes par guidance
            
            if (currentGuidanceIndex < currentScene.guidance.size - 1) {
                currentGuidanceIndex++
            } else {
                // Exercice terminé
                exerciseCompleted = true
                exerciseStarted = false
            }
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Visualisation paisible") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        if (!exerciseStarted && !exerciseCompleted) {
            // Écran de sélection de scène
            SceneSelectionScreen(
                technique = technique,
                scenes = scenes,
                selectedSceneIndex = selectedSceneIndex,
                onSceneSelected = { selectedSceneIndex = it },
                onStartExercise = {
                    exerciseStarted = true
                    currentGuidanceIndex = 0
                }
            )
        } else if (exerciseCompleted) {
            // Écran de completion
            CompletionScreen(
                onComplete = onComplete,
                onRestartExercise = {
                    exerciseCompleted = false
                    exerciseStarted = true
                    currentGuidanceIndex = 0
                }
            )
        } else {
            // Interface d'exercice immersive
            ImmersiveExerciseInterface(
                scene = currentScene,
                currentGuidanceText = currentGuidanceText,
                isTransitioning = isTransitioning,
                animationValue = animationValue,
                onSkipToNext = {
                    if (currentGuidanceIndex < currentScene.guidance.size - 1) {
                        currentGuidanceIndex++
                    } else {
                        exerciseCompleted = true
                        exerciseStarted = false
                    }
                },
                onChangeScene = {
                    selectedSceneIndex = (selectedSceneIndex + 1) % scenes.size
                    currentGuidanceIndex = 0
                },
                onStopExercise = {
                    exerciseStarted = false
                    currentGuidanceIndex = 0
                }
            )
        }
    }
}

@Composable
private fun SceneSelectionScreen(
    technique: Technique,
    scenes: List<VisualizationScene>,
    selectedSceneIndex: Int,
    onSceneSelected: (Int) -> Unit,
    onStartExercise: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Landscape,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
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
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Choisissez votre environnement de visualisation :",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
        
        items(scenes.size) { index ->
            val scene = scenes[index]
            val isSelected = selectedSceneIndex == index
            
            Card(
                onClick = { onSceneSelected(index) },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        scene.icon,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp),
                        tint = if (isSelected) scene.color1 else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = scene.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        
                        Text(
                            text = scene.description,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = scene.soundscape,
                            fontSize = 12.sp,
                            color = scene.color1,
                            fontWeight = FontWeight.Medium
                        )
                        
                        Text(
                            text = scene.atmosphere,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = onStartExercise,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = scenes[selectedSceneIndex].color1
                )
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Commencer ${scenes[selectedSceneIndex].name}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun ImmersiveExerciseInterface(
    scene: VisualizationScene,
    currentGuidanceText: String,
    isTransitioning: Boolean,
    animationValue: Float,
    onSkipToNext: () -> Unit,
    onChangeScene: () -> Unit,
    onStopExercise: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Header avec informations de la scène
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = scene.color1.copy(alpha = 0.1f)
            )
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    scene.icon,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = scene.color1
                )
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = scene.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "${scene.soundscape} • ${scene.atmosphere}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "∞",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = scene.color1
                    )
                    Text(
                        text = "Moments paisibles",
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        
        // Zone d'environnement immersif
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Black)
        ) {
            // Environnement animé
            Canvas(
                modifier = Modifier.fillMaxSize()
            ) {
                drawImmersiveEnvironment(scene, animationValue, size)
            }
            
            // Texte de guidance flottant
            if (currentGuidanceText.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .then(
                                if (isTransitioning) {
                                    Modifier.blur(4.dp)
                                } else {
                                    Modifier
                                }
                            ),
                        colors = CardDefaults.cardColors(
                            containerColor = Color.Black.copy(alpha = 0.7f),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text(
                            text = currentGuidanceText,
                            fontSize = 18.sp,
                            lineHeight = 26.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(24.dp),
                            color = Color.White
                        )
                    }
                }
            }
        }
        
        // Contrôles
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = onSkipToNext,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.SkipNext, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Skip")
            }
            
            Button(
                onClick = onChangeScene,
                colors = ButtonDefaults.buttonColors(
                    containerColor = scene.color1
                ),
                modifier = Modifier.weight(1f)
            ) {
                Icon(scene.icon, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Changer")
            }
            
            OutlinedButton(
                onClick = onStopExercise,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Stop, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Arrêter")
            }
        }
    }
}

@Composable
private fun CompletionScreen(
    onComplete: () -> Unit,
    onRestartExercise: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.CheckCircle,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color(0xFF16A34A)
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Visualisation terminée !",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Vous avez complété votre voyage de visualisation paisible. Votre esprit est maintenant calme et centré.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 22.sp
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Psychology, tint = Color(0xFF3B82F6), modifier = Modifier.size(16.dp), contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Esprit calmé", fontSize = 12.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp), contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Stress réduit", fontSize = 12.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocalFlorist, tint = Color(0xFF16A34A), modifier = Modifier.size(16.dp), contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Paix intérieure", fontSize = 12.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Column {
            Button(
                onClick = onRestartExercise,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Visiter un autre lieu", fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedButton(
                onClick = onComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("Terminer", fontSize = 16.sp)
            }
        }
    }
}

// Fonction pour dessiner les environnements immersifs
private fun DrawScope.drawImmersiveEnvironment(
    scene: VisualizationScene,
    animationValue: Float,
    canvasSize: androidx.compose.ui.geometry.Size
) {
    when (scene.environmentType) {
        "peaks" -> drawMountainPeaks(scene, animationValue, canvasSize)
        "trees" -> drawForestTrees(scene, animationValue, canvasSize)
        "waves" -> drawOceanWaves(scene, animationValue, canvasSize)
        "garden" -> drawGarden(scene, animationValue, canvasSize)
        "stars" -> drawStarryNight(scene, animationValue, canvasSize)
    }
}

private fun DrawScope.drawMountainPeaks(
    scene: VisualizationScene,
    animationValue: Float,
    canvasSize: androidx.compose.ui.geometry.Size
) {
    // Dégradé de ciel (lever de soleil)
    val skyBrush = Brush.verticalGradient(
        colors = listOf(scene.color2, scene.color1, Color(0xFF87CEEB)),
        startY = 0f,
        endY = canvasSize.height * 0.6f
    )
    drawRect(skyBrush)
    
    // Soleil
    val sunX = canvasSize.width * (0.7f + sin(animationValue) * 0.1f)
    val sunY = canvasSize.height * (0.2f + cos(animationValue * 0.5f) * 0.05f)
    drawCircle(
        Color(0xFFFFD700),
        radius = 30f,
        center = androidx.compose.ui.geometry.Offset(sunX, sunY)
    )
    
    // Montagnes en couches
    for (i in 0..3) {
        val alpha = 1f - (i * 0.2f)
        val mountainColor = scene.color1.copy(alpha = alpha)
        val mountainHeight = canvasSize.height * (0.4f + i * 0.1f)
        val offsetY = canvasSize.height - mountainHeight + sin(animationValue + i) * 5f
        
        val mountainPath = androidx.compose.ui.graphics.Path().apply {
            moveTo(0f, canvasSize.height)
            for (x in 0..canvasSize.width.toInt() step 20) {
                val peakHeight = sin((x + animationValue * 10 + i * 100) * 0.01f) * 50f
                lineTo(x.toFloat(), offsetY + peakHeight)
            }
            lineTo(canvasSize.width, canvasSize.height)
            close()
        }
        
        drawPath(mountainPath, mountainColor)
    }
}

private fun DrawScope.drawForestTrees(
    scene: VisualizationScene,
    animationValue: Float,
    canvasSize: androidx.compose.ui.geometry.Size
) {
    // Arrière-plan forestier
    val forestBrush = Brush.verticalGradient(
        colors = listOf(scene.color2, scene.color1),
        startY = 0f,
        endY = canvasSize.height
    )
    drawRect(forestBrush)
    
    // Rayons de soleil
    for (i in 0..5) {
        val rayAngle = (animationValue + i * 30f) * 0.01f
        val rayX = canvasSize.width * (0.2f + i * 0.15f)
        rotate(rayAngle, pivot = androidx.compose.ui.geometry.Offset(rayX, 0f)) {
            drawRect(
                Color.Yellow.copy(alpha = 0.1f),
                topLeft = androidx.compose.ui.geometry.Offset(rayX - 2f, 0f),
                size = androidx.compose.ui.geometry.Size(4f, canvasSize.height * 0.6f)
            )
        }
    }
    
    // Arbres animés
    for (i in 0..15) {
        val treeX = canvasSize.width * (i / 15f)
        val treeHeight = 100f + sin(animationValue * 0.5f + i) * 20f
        val sway = sin(animationValue * 2f + i * 0.5f) * 3f
        
        // Tronc
        drawRect(
            Color(0xFF8B4513),
            topLeft = androidx.compose.ui.geometry.Offset(treeX - 5f + sway, canvasSize.height - treeHeight),
            size = androidx.compose.ui.geometry.Size(10f, treeHeight)
        )
        
        // Feuillage
        drawCircle(
            scene.color1,
            radius = 25f + sin(animationValue + i) * 5f,
            center = androidx.compose.ui.geometry.Offset(treeX + sway, canvasSize.height - treeHeight - 15f)
        )
    }
}

private fun DrawScope.drawOceanWaves(
    scene: VisualizationScene,
    animationValue: Float,
    canvasSize: androidx.compose.ui.geometry.Size
) {
    // Ciel océanique
    val skyBrush = Brush.verticalGradient(
        colors = listOf(scene.color2, scene.color1),
        startY = 0f,
        endY = canvasSize.height * 0.6f
    )
    drawRect(skyBrush)
    
    // Vagues animées
    for (layer in 0..4) {
        val waveColor = scene.color1.copy(alpha = 0.6f - layer * 0.1f)
        val waveHeight = 50f + layer * 20f
        val waveSpeed = 1f + layer * 0.3f
        val baseY = canvasSize.height * (0.6f + layer * 0.08f)
        
        val wavePath = androidx.compose.ui.graphics.Path().apply {
            moveTo(0f, canvasSize.height)
            for (x in 0..canvasSize.width.toInt() step 10) {
                val waveY = baseY + sin((x * 0.01f) + (animationValue * waveSpeed)) * waveHeight
                if (x == 0) moveTo(x.toFloat(), waveY) else lineTo(x.toFloat(), waveY)
            }
            lineTo(canvasSize.width, canvasSize.height)
            close()
        }
        
        drawPath(wavePath, waveColor)
    }
    
    // Mouettes
    for (i in 0..3) {
        val birdX = canvasSize.width * ((animationValue * 0.1f + i * 0.25f) % 1f)
        val birdY = canvasSize.height * (0.2f + sin(animationValue + i) * 0.1f)
        
        // Simple représentation de mouette
        drawLine(
            Color.White,
            start = androidx.compose.ui.geometry.Offset(birdX - 8f, birdY),
            end = androidx.compose.ui.geometry.Offset(birdX + 8f, birdY),
            strokeWidth = 2f
        )
    }
}

private fun DrawScope.drawGarden(
    scene: VisualizationScene,
    animationValue: Float,
    canvasSize: androidx.compose.ui.geometry.Size
) {
    // Arrière-plan de jardin
    val gardenBrush = Brush.radialGradient(
        colors = listOf(scene.color2, scene.color1),
        radius = canvasSize.width
    )
    drawRect(gardenBrush)
    
    // Fleurs animées
    for (i in 0..20) {
        val flowerX = (canvasSize.width * (i / 20f)) + sin(animationValue + i) * 10f
        val flowerY = (canvasSize.height * (0.3f + (i % 3) * 0.2f)) + cos(animationValue * 0.7f + i) * 5f
        val petalSize = 15f + sin(animationValue * 2f + i) * 5f
        
        // Pétales
        for (petal in 0..5) {
            val petalAngle = (petal * 60f) + (animationValue * 10f)
            val petalX = flowerX + cos(petalAngle * PI / 180f).toFloat() * petalSize
            val petalY = flowerY + sin(petalAngle * PI / 180f).toFloat() * petalSize
            
            drawCircle(
                scene.color2.copy(alpha = 0.7f),
                radius = 8f,
                center = androidx.compose.ui.geometry.Offset(petalX, petalY)
            )
        }
        
        // Centre de fleur
        drawCircle(
            Color.Yellow,
            radius = 5f,
            center = androidx.compose.ui.geometry.Offset(flowerX, flowerY)
        )
    }
    
    // Fontaine au centre
    val fountainX = canvasSize.width * 0.5f
    val fountainY = canvasSize.height * 0.7f
    
    drawCircle(
        scene.color1,
        radius = 40f,
        center = androidx.compose.ui.geometry.Offset(fountainX, fountainY)
    )
    
    // Gouttes d'eau animées
    for (i in 0..10) {
        val dropX = fountainX + sin(animationValue * 3f + i) * 25f
        val dropY = fountainY - abs(cos(animationValue * 3f + i)) * 30f
        
        drawCircle(
            Color.Cyan.copy(alpha = 0.6f),
            radius = 2f,
            center = androidx.compose.ui.geometry.Offset(dropX, dropY)
        )
    }
}

private fun DrawScope.drawStarryNight(
    scene: VisualizationScene,
    animationValue: Float,
    canvasSize: androidx.compose.ui.geometry.Size
) {
    // Ciel nocturne dégradé
    val nightBrush = Brush.verticalGradient(
        colors = listOf(Color.Black, scene.color1, Color.Black),
        startY = 0f,
        endY = canvasSize.height
    )
    drawRect(nightBrush)
    
    // Lune
    val moonX = canvasSize.width * 0.8f
    val moonY = canvasSize.height * 0.2f
    drawCircle(
        scene.color2,
        radius = 40f,
        center = androidx.compose.ui.geometry.Offset(moonX, moonY)
    )
    
    // Étoiles scintillantes
    val random = Random(42) // Seed fixe pour cohérence
    for (i in 0..150) {
        val starX = random.nextFloat() * canvasSize.width
        val starY = random.nextFloat() * canvasSize.height * 0.7f
        val twinkle = sin(animationValue * (2f + i * 0.1f)) * 0.5f + 0.5f
        val starSize = (1f + random.nextFloat() * 3f) * twinkle
        
        drawCircle(
            Color.White.copy(alpha = twinkle),
            radius = starSize,
            center = androidx.compose.ui.geometry.Offset(starX, starY)
        )
    }
    
    // Voie lactée
    val milkyWayBrush = Brush.linearGradient(
        colors = listOf(Color.Transparent, scene.color2.copy(alpha = 0.3f), Color.Transparent),
        start = androidx.compose.ui.geometry.Offset(0f, canvasSize.height * 0.2f),
        end = androidx.compose.ui.geometry.Offset(canvasSize.width, canvasSize.height * 0.6f)
    )
    
    val milkyWayPath = androidx.compose.ui.graphics.Path().apply {
        moveTo(0f, canvasSize.height * 0.3f)
        quadraticBezierTo(
            canvasSize.width * 0.5f, canvasSize.height * 0.1f,
            canvasSize.width, canvasSize.height * 0.4f
        )
        lineTo(canvasSize.width, canvasSize.height * 0.6f)
        quadraticBezierTo(
            canvasSize.width * 0.5f, canvasSize.height * 0.3f,
            0f, canvasSize.height * 0.5f
        )
        close()
    }
    
    drawPath(milkyWayPath, milkyWayBrush)
}