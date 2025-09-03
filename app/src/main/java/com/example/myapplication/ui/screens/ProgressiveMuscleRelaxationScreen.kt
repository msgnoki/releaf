package com.example.myapplication.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Technique
import kotlinx.coroutines.delay

// Phases de l'exercice
enum class PMRPhase {
    PREPARE, TENSE, RELAX
}

// Groupe musculaire
data class MuscleGroup(
    val key: String,
    val name: String,
    val shortName: String,
    val icon: ImageVector,
    val tensionInstructions: List<String>,
    val relaxationInstructions: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressiveMuscleRelaxationScreen(
    technique: Technique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var exerciseStarted by remember { mutableStateOf(false) }
    var exerciseCompleted by remember { mutableStateOf(false) }
    var currentGroupIndex by remember { mutableIntStateOf(0) }
    var currentPhase by remember { mutableStateOf(PMRPhase.PREPARE) }
    var isPaused by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableLongStateOf(0L) }
    var timerProgress by remember { mutableFloatStateOf(0f) }
    var prepareCountdown by remember { mutableIntStateOf(3) }
    var selectedTab by remember { mutableStateOf("tension") }
    
    // 19 groupes musculaires
    val muscleGroups = remember {
        listOf(
            MuscleGroup("right_hand", "Main droite", "Main droite", Icons.Default.PanTool,
                listOf("Fermez votre poing droit", "Serrez fort", "Sentez la tension dans vos doigts et votre poignet"),
                listOf("Ouvrez votre main", "Laissez-la complètement détendue", "Remarquez la sensation chaude et lourde")),
            MuscleGroup("left_hand", "Main gauche", "Main gauche", Icons.Default.PanTool,
                listOf("Fermez votre poing gauche", "Serrez fort", "Sentez la tension"),
                listOf("Ouvrez votre main", "Laissez-la se détendre", "Ressentez le soulagement et la lourdeur")),
            MuscleGroup("right_upper_arm", "Bras droit", "Bras droit", Icons.Default.FitnessCenter,
                listOf("Pliez votre bras droit", "Amenez votre poing vers l'épaule", "Contractez votre biceps"),
                listOf("Baissez votre bras sur le côté", "Laissez le muscle se détendre", "Ressentez le soulagement et la lourdeur")),
            MuscleGroup("left_upper_arm", "Bras gauche", "Bras gauche", Icons.Default.FitnessCenter,
                listOf("Pliez votre bras gauche", "Amenez votre poing vers l'épaule", "Contractez votre biceps"),
                listOf("Baissez votre bras sur le côté", "Laissez le muscle se détendre", "Ressentez la différence")),
            MuscleGroup("forehead", "Front", "Front", Icons.Default.Face,
                listOf("Froncez vos sourcils", "Plissez votre front", "Créez des rides profondes"),
                listOf("Détendez votre front", "Lissez votre peau", "Sentez la relaxation se répandre")),
            MuscleGroup("eyes_nose", "Yeux et nez", "Yeux/Nez", Icons.Default.Visibility,
                listOf("Fermez fort vos yeux", "Plissez votre nez", "Sentez la tension autour des yeux"),
                listOf("Ouvrez doucement les yeux", "Relâchez complètement", "Ressentez la détente")),
            MuscleGroup("mouth_jaw", "Bouche et mâchoire", "Mâchoire", Icons.Default.Mic,
                listOf("Mordez fort", "Serrez les dents", "Sentez les muscles de la mâchoire travailler"),
                listOf("Ouvrez légèrement la bouche", "Laissez votre mâchoire pendre", "Remarquez la relaxation")),
            MuscleGroup("neck_throat", "Cou et gorge", "Cou", Icons.Default.Person,
                listOf("Penchez votre tête en arrière", "Appuyez contre le support", "Tendez les muscles du cou"),
                listOf("Ramenez votre tête en position neutre", "Détendez complètement", "Sentez la lourdeur")),
            MuscleGroup("shoulders", "Épaules", "Épaules", Icons.Default.KeyboardArrowUp,
                listOf("Haussez vos épaules", "Amenez-les vers vos oreilles", "Tenez la position fermement"),
                listOf("Laissez tomber vos épaules", "Relâchez complètement", "Sentez-les s'affaisser naturellement")),
            MuscleGroup("upper_back_chest", "Dos et poitrine", "Dos/Poitrine", Icons.Default.FitnessCenter,
                listOf("Cambrez votre dos", "Serrez vos omoplates", "Poussez votre poitrine vers l'avant"),
                listOf("Relâchez votre dos", "Laissez vos épaules revenir", "Sentez la détente profonde")),
            MuscleGroup("abdomen", "Abdomen", "Ventre", Icons.Default.FitnessCenter,
                listOf("Contractez vos abdominaux", "Durcissez votre ventre", "Rentrez votre nombril"),
                listOf("Relâchez complètement", "Laissez votre ventre se détendre", "Respirez naturellement")),
            MuscleGroup("lower_back", "Bas du dos", "Bas du dos", Icons.Default.FitnessCenter,
                listOf("Cambrez le bas de votre dos", "Poussez vers l'arrière", "Créez une tension lombaire"),
                listOf("Relâchez doucement", "Laissez votre dos s'affaisser", "Sentez le soulagement")),
            MuscleGroup("hips_buttocks", "Hanches et fessiers", "Fessiers", Icons.Default.Weekend,
                listOf("Contractez vos fessiers", "Serrez fermement", "Soulevez légèrement vos hanches"),
                listOf("Relâchez complètement", "Laissez-vous retomber", "Sentez la lourdeur et la chaleur")),
            MuscleGroup("right_thigh", "Cuisse droite", "Cuisse droite", Icons.Default.Accessibility,
                listOf("Contractez votre cuisse droite", "Poussez votre genou vers le bas", "Tendez tout le muscle"),
                listOf("Relâchez votre cuisse", "Laissez votre jambe se détendre", "Remarquez la différence")),
            MuscleGroup("left_thigh", "Cuisse gauche", "Cuisse gauche", Icons.Default.Accessibility,
                listOf("Contractez votre cuisse gauche", "Poussez votre genou vers le bas", "Tendez fermement"),
                listOf("Relâchez complètement", "Laissez la jambe reposer", "Sentez la relaxation")),
            MuscleGroup("right_calf_shin", "Mollet droit", "Mollet droit", Icons.Default.DirectionsWalk,
                listOf("Pointez votre pied droit vers le bas", "Contractez votre mollet", "Tendez fermement"),
                listOf("Relâchez votre pied", "Laissez votre mollet se détendre", "Sentez la lourdeur")),
            MuscleGroup("left_calf_shin", "Mollet gauche", "Mollet gauche", Icons.Default.DirectionsWalk,
                listOf("Pointez votre pied gauche vers le bas", "Contractez votre mollet", "Maintenez la tension"),
                listOf("Relâchez votre pied", "Détendez complètement", "Ressentez le soulagement")),
            MuscleGroup("right_foot", "Pied droit", "Pied droit", Icons.Default.DirectionsWalk,
                listOf("Pliez vos orteils droits", "Contractez votre pied", "Créez une tension dans la voûte"),
                listOf("Relâchez vos orteils", "Laissez votre pied se détendre", "Sentez-le devenir lourd")),
            MuscleGroup("left_foot", "Pied gauche", "Pied gauche", Icons.Default.DirectionsWalk,
                listOf("Pliez vos orteils gauches", "Contractez votre pied", "Tendez la voûte plantaire"),
                listOf("Relâchez complètement", "Laissez votre pied reposer", "Remarquez la sensation de détente"))
        )
    }
    
    val currentGroup = muscleGroups[currentGroupIndex]
    
    // Gestion des timers
    LaunchedEffect(currentPhase, exerciseStarted, isPaused) {
        when (currentPhase) {
            PMRPhase.PREPARE -> {
                if (exerciseStarted && !isPaused) {
                    prepareCountdown = 3
                    repeat(3) {
                        delay(1000)
                        if (currentPhase == PMRPhase.PREPARE && !isPaused) {
                            prepareCountdown--
                        }
                    }
                    if (currentPhase == PMRPhase.PREPARE && !isPaused) {
                        currentPhase = PMRPhase.TENSE
                        selectedTab = "tension"
                    }
                }
            }
            PMRPhase.TENSE, PMRPhase.RELAX -> {
                if (!isPaused) {
                    val duration = 10000L // 10 secondes
                    timeRemaining = duration
                    
                    val startTime = System.currentTimeMillis()
                    while (timeRemaining > 0 && currentPhase != PMRPhase.PREPARE && !isPaused) {
                        delay(50)
                        val elapsed = System.currentTimeMillis() - startTime
                        timeRemaining = maxOf(0, duration - elapsed)
                        timerProgress = (elapsed.toFloat() / duration) * 100f
                    }
                    
                    if (!isPaused && timeRemaining <= 0) {
                        when (currentPhase) {
                            PMRPhase.TENSE -> {
                                currentPhase = PMRPhase.RELAX
                                selectedTab = "relaxation"
                            }
                            PMRPhase.RELAX -> {
                                if (currentGroupIndex < muscleGroups.size - 1) {
                                    currentGroupIndex++
                                    currentPhase = PMRPhase.PREPARE
                                    selectedTab = "tension"
                                } else {
                                    exerciseCompleted = true
                                    exerciseStarted = false
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Relaxation musculaire progressive") },
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
            // Écran de démarrage
            StartScreen(
                technique = technique,
                onStartExercise = {
                    exerciseStarted = true
                    currentPhase = PMRPhase.PREPARE
                }
            )
        } else if (exerciseCompleted) {
            // Écran de completion
            CompletionScreen(onComplete = onComplete)
        } else {
            // Interface d'exercice
            ExerciseInterface(
                currentGroup = currentGroup,
                currentGroupIndex = currentGroupIndex,
                totalGroups = muscleGroups.size,
                currentPhase = currentPhase,
                timeRemaining = timeRemaining,
                timerProgress = timerProgress,
                prepareCountdown = prepareCountdown,
                selectedTab = selectedTab,
                isPaused = isPaused,
                onTabSelected = { selectedTab = it },
                onPause = { isPaused = true },
                onResume = { isPaused = false },
                onStop = {
                    exerciseStarted = false
                    currentGroupIndex = 0
                    currentPhase = PMRPhase.PREPARE
                },
                onPrevious = {
                    if (currentGroupIndex > 0) {
                        currentGroupIndex--
                        currentPhase = PMRPhase.PREPARE
                        selectedTab = "tension"
                    }
                },
                onNext = {
                    if (currentGroupIndex < muscleGroups.size - 1) {
                        currentGroupIndex++
                        currentPhase = PMRPhase.PREPARE
                        selectedTab = "tension"
                    } else {
                        exerciseCompleted = true
                        exerciseStarted = false
                    }
                }
            )
        }
    }
}

@Composable
private fun StartScreen(
    technique: Technique,
    onStartExercise: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = technique.name,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = technique.description,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "19 groupes musculaires",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Tension et relâchement systématiques",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = onStartExercise,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Commencer l'exercice", fontSize = 16.sp)
        }
    }
}

@Composable
private fun ExerciseInterface(
    currentGroup: MuscleGroup,
    currentGroupIndex: Int,
    totalGroups: Int,
    currentPhase: PMRPhase,
    timeRemaining: Long,
    timerProgress: Float,
    prepareCountdown: Int,
    selectedTab: String,
    isPaused: Boolean,
    onTabSelected: (String) -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // En-tête avec compteur
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF4F46E5).copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Relaxation musculaire progressive",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Tension et relâchement systématiques",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "${currentGroupIndex + 1}",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4F46E5)
                        )
                        Text(
                            text = "sur $totalGroups groupes",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LinearProgressIndicator(
                    progress = { (currentGroupIndex + 1).toFloat() / totalGroups },
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFF4F46E5)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // État actuel et timer
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Icône du groupe musculaire
            Icon(
                currentGroup.icon,
                contentDescription = currentGroup.name,
                modifier = Modifier.size(80.dp),
                tint = when (currentPhase) {
                    PMRPhase.PREPARE -> MaterialTheme.colorScheme.onSurfaceVariant
                    PMRPhase.TENSE -> Color(0xFFDC2626)
                    PMRPhase.RELAX -> Color(0xFF16A34A)
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = currentGroup.name,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Phase actuelle
            Text(
                text = when (currentPhase) {
                    PMRPhase.PREPARE -> "Préparez-vous"
                    PMRPhase.TENSE -> "Tendre"
                    PMRPhase.RELAX -> "Relâcher"
                },
                fontSize = 18.sp,
                color = when (currentPhase) {
                    PMRPhase.PREPARE -> MaterialTheme.colorScheme.onSurfaceVariant
                    PMRPhase.TENSE -> Color(0xFFDC2626)
                    PMRPhase.RELAX -> Color(0xFF16A34A)
                }
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Timer circulaire
            Box(
                modifier = Modifier.size(80.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawPMRTimer(currentPhase, timerProgress)
                }
                
                Text(
                    text = when (currentPhase) {
                        PMRPhase.PREPARE -> "$prepareCountdown"
                        else -> "${timeRemaining / 1000}"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Onglets d'instructions
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column {
                // En-têtes des onglets
                Row(modifier = Modifier.fillMaxWidth()) {
                    TabButton(
                        text = "Étapes de tension :",
                        isSelected = selectedTab == "tension" || currentPhase == PMRPhase.TENSE,
                        color = Color(0xFFDC2626),
                        onClick = { onTabSelected("tension") },
                        modifier = Modifier.weight(1f)
                    )
                    TabButton(
                        text = "Étapes de relaxation :",
                        isSelected = selectedTab == "relaxation" || currentPhase == PMRPhase.RELAX,
                        color = Color(0xFF16A34A),
                        onClick = { onTabSelected("relaxation") },
                        modifier = Modifier.weight(1f)
                    )
                }
                
                Divider()
                
                // Contenu des onglets
                Column(modifier = Modifier.padding(16.dp)) {
                    val instructions = if (selectedTab == "tension" || currentPhase == PMRPhase.TENSE) {
                        currentGroup.tensionInstructions
                    } else {
                        currentGroup.relaxationInstructions
                    }
                    
                    val color = if (selectedTab == "tension" || currentPhase == PMRPhase.TENSE) {
                        Color(0xFFDC2626)
                    } else {
                        Color(0xFF16A34A)
                    }
                    
                    instructions.forEachIndexed { index, instruction ->
                        Row(
                            verticalAlignment = Alignment.Top,
                            modifier = Modifier.padding(vertical = 4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Canvas(modifier = Modifier.fillMaxSize()) {
                                    drawCircle(color.copy(alpha = 0.2f))
                                }
                                Text(
                                    text = "${index + 1}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = color
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            Text(
                                text = instruction,
                                fontSize = 14.sp,
                                color = color,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Contrôles
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (currentPhase != PMRPhase.PREPARE) {
                Button(
                    onClick = if (isPaused) onResume else onPause,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPaused) Color(0xFF16A34A) else Color(0xFFF59E0B)
                    ),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        if (isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isPaused) "Reprendre" else "Pause")
                }
            }
            
            OutlinedButton(
                onClick = onStop,
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.Stop, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Arrêter")
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        // Navigation
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onPrevious,
                    enabled = currentGroupIndex > 0
                ) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Retour")
                }
                
                // Points de progression
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(minOf(totalGroups, 10)) { index ->
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                        ) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                drawCircle(
                                    color = if (index <= currentGroupIndex) {
                                        androidx.compose.ui.graphics.Color(0xFF4F46E5)
                                    } else {
                                        androidx.compose.ui.graphics.Color(0xFFE5E7EB)
                                    }
                                )
                            }
                        }
                    }
                    if (totalGroups > 10) {
                        Text("...", color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
                
                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4F46E5)
                    )
                ) {
                    Text(if (currentGroupIndex == totalGroups - 1) "Terminer" else "Suivant")
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        if (currentGroupIndex == totalGroups - 1) Icons.Default.Check else Icons.Default.ArrowForward,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
private fun TabButton(
    text: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) color.copy(alpha = 0.1f) else Color.Transparent,
            contentColor = if (isSelected) color else MaterialTheme.colorScheme.onSurfaceVariant
        ),
        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp, bottomStart = 0.dp, bottomEnd = 0.dp)
    ) {
        Text(
            text = text,
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun CompletionScreen(
    onComplete: () -> Unit
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
            text = "Exercice terminé !",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Félicitations ! Vous avez complété les 19 groupes musculaires. Votre corps devrait maintenant se sentir détendu et relâché.",
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Bolt, contentDescription = null, tint = Color(0xFF3B82F6), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Tension relâchée", fontSize = 12.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Corps détendu", fontSize = 12.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
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

private fun DrawScope.drawPMRTimer(phase: PMRPhase, progress: Float) {
    val strokeWidth = 6.dp.toPx()
    val color = when (phase) {
        PMRPhase.PREPARE -> androidx.compose.ui.graphics.Color.Gray
        PMRPhase.TENSE -> androidx.compose.ui.graphics.Color.Red
        PMRPhase.RELAX -> androidx.compose.ui.graphics.Color.Green
    }
    
    // Cercle de fond
    drawCircle(
        color = androidx.compose.ui.graphics.Color.LightGray,
        style = Stroke(width = strokeWidth)
    )
    
    // Progress arc
    if (phase != PMRPhase.PREPARE) {
        val sweepAngle = (progress / 100f) * 360f
        drawArc(
            color = color,
            startAngle = -90f,
            sweepAngle = sweepAngle,
            useCenter = false,
            style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
        )
    }
}