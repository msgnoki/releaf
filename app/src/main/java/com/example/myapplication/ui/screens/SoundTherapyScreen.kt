package com.example.myapplication.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.Technique
import com.example.myapplication.ui.viewmodels.SoundTherapyViewModel
import kotlinx.coroutines.delay

// Modèles de données pour la thérapie sonore
data class TherapyFrequency(
    val value: Int,
    val name: String,
    val description: String,
    val color: Color
)

data class BinauralBeat(
    val value: Int,
    val name: String,
    val description: String
)

data class ModulationPreset(
    val name: String,
    val description: String,
    val intensity: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoundTherapyScreen(
    technique: Technique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SoundTherapyViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedFrequency by remember { mutableStateOf<TherapyFrequency?>(null) }
    var selectedBinauralBeat by remember { mutableStateOf<BinauralBeat?>(null) }
    var selectedModulation by remember { mutableStateOf<ModulationPreset?>(null) }

    // Gestion des erreurs
    LaunchedEffect(uiState.error) {
        if (uiState.error != null) {
            // L'erreur sera affichée dans l'UI
            delay(5000) // Efface l'erreur après 5 secondes
            viewModel.clearError()
        }
    }
    
    // Cleanup à la sortie du composable
    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopAudio()
        }
    }

    // Fréquences thérapeutiques
    val frequencies = remember {
        listOf(
            TherapyFrequency(396, "Libération", "Libération de la culpabilité et de la peur", Color(0xFFE53E3E)),
            TherapyFrequency(417, "Changement", "Facilite le changement et défait les situations", Color(0xFFDD6B20)),
            TherapyFrequency(528, "Amour", "Transformation et réparation de l'ADN", Color(0xFF38A169)),
            TherapyFrequency(639, "Connexion", "Améliore la communication et les relations", Color(0xFF3182CE)),
            TherapyFrequency(741, "Expression", "Éveil et expression de soi", Color(0xFF805AD5)),
            TherapyFrequency(852, "Intuition", "Retour à l'ordre spirituel", Color(0xFFD53F8C)),
            TherapyFrequency(963, "Unité", "Connexion avec l'énergie universelle", Color(0xFF319795)),
            TherapyFrequency(174, "Fondation", "Base naturelle pour l'évolution", Color(0xFF4A5568))
        )
    }

    // Battements binauraux
    val binauralBeats = remember {
        listOf(
            BinauralBeat(0, "Aucun", "Tonalité pure sans battements"),
            BinauralBeat(2, "Delta", "Sommeil profond et guérison"),
            BinauralBeat(4, "Theta", "Méditation profonde et créativité"),
            BinauralBeat(8, "Alpha", "Relaxation et concentration"),
            BinauralBeat(15, "Beta", "Concentration active et éveil")
        )
    }

    // Presets de modulation
    val modulationPresets = remember {
        listOf(
            ModulationPreset("Aucune", "Son pur et stable", "Aucune"),
            ModulationPreset("Subtile", "Variation douce et apaisante", "Faible"),
            ModulationPreset("Modérée", "Ondulation relaxante", "Moyenne"),
            ModulationPreset("Intense", "Modulation profonde", "Forte"),
            ModulationPreset("Dynamique", "Variation complexe", "Maximum")
        )
    }

    fun formatDuration(ms: Long): String {
        val minutes = ms / 60000
        val seconds = (ms % 60000) / 1000
        return "${minutes}:${seconds.toString().padStart(2, '0')}"
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Thérapie sonore") },
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
                IntroductionSection(technique = technique)
            }

            // État de lecture actuel
            if (uiState.isPlaying && selectedFrequency != null) {
                item {
                    PlayingStateSection(
                        selectedFrequency = selectedFrequency!!,
                        sessionDuration = uiState.sessionDuration,
                        formatDuration = ::formatDuration
                    )
                }
            }

            // Sélection de fréquence
            item {
                FrequencySelectionSection(
                    frequencies = frequencies,
                    selectedFrequency = selectedFrequency,
                    onFrequencySelected = { selectedFrequency = it }
                )
            }

            // Paramètres audio
            item {
                AudioSettingsSection(
                    binauralBeats = binauralBeats,
                    modulationPresets = modulationPresets,
                    selectedBinauralBeat = selectedBinauralBeat,
                    selectedModulation = selectedModulation,
                    volume = uiState.volume,
                    onBinauralBeatSelected = { 
                        selectedBinauralBeat = it 
                        viewModel.setBinauralBeat(it.value)
                    },
                    onModulationSelected = { selectedModulation = it },
                    onVolumeChanged = { viewModel.setVolume(it) }
                )
            }

            // Contrôles de lecture
            item {
                PlaybackControlsSection(
                    selectedFrequency = selectedFrequency,
                    selectedBinauralBeat = selectedBinauralBeat,
                    uiState = uiState,
                    onStartSession = { freq, binaural -> viewModel.startFrequency(freq, binaural) },
                    onStopSession = { viewModel.stopAudio() }
                )
            }

            // Section éducative
            item {
                EducationalSection()
            }

            // Message d'erreur
            if (uiState.error != null) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFEF4444).copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = Color(0xFFEF4444),
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = uiState.error!!,
                                color = Color(0xFFEF4444),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Bouton terminer (après une utilisation suffisante)
            if (uiState.sessionDuration >= 60000) { // 1 minute minimum
                item {
                    Button(
                        onClick = onComplete,
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF805AD5)
                        )
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Terminer la séance")
                    }
                }
            }
        }
    }
}

@Composable
private fun IntroductionSection(technique: Technique) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.GraphicEq,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF805AD5)
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
    }
}

@Composable
private fun PlayingStateSection(
    selectedFrequency: TherapyFrequency,
    sessionDuration: Long,
    formatDuration: (Long) -> String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF805AD5).copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animation des ondes sonores
            val infiniteTransition = rememberInfiniteTransition(label = "sound_waves")
            val waveAnimation by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing)
                ),
                label = "wave_animation"
            )

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size((40 + index * 20).dp)
                            .background(
                                brush = Brush.radialGradient(
                                    listOf(
                                        selectedFrequency.color.copy(alpha = (1f - waveAnimation) * 0.3f),
                                        Color.Transparent
                                    )
                                ),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }

                Icon(
                    Icons.Default.GraphicEq,
                    contentDescription = null,
                    tint = selectedFrequency.color,
                    modifier = Modifier.size(32.dp)
                )
            }

            Text(
                text = "${selectedFrequency.value} Hz - ${selectedFrequency.name}",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = selectedFrequency.color
            )

            Text(
                text = selectedFrequency.description,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.AccessTime,
                    contentDescription = null,
                    tint = Color(0xFF805AD5),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Durée: ${formatDuration(sessionDuration)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Laissez les vibrations apaisantes vous envelopper. Concentrez-vous sur votre respiration et laissez les fréquences vous guider vers une relaxation profonde.",
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun FrequencySelectionSection(
    frequencies: List<TherapyFrequency>,
    selectedFrequency: TherapyFrequency?,
    onFrequencySelected: (TherapyFrequency) -> Unit
) {
    Column {
        Text(
            text = "Sélectionnez votre fréquence thérapeutique",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row {
            repeat(2) { column ->
                Column(modifier = Modifier.weight(1f)) {
                    frequencies.chunked(4)[column].forEach { frequency ->
                        val isSelected = selectedFrequency == frequency
                        
                        Card(
                            onClick = { onFrequencySelected(frequency) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(
                                    end = if (column == 0) 4.dp else 0.dp,
                                    start = if (column == 1) 4.dp else 0.dp,
                                    bottom = 8.dp
                                ),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSelected) {
                                    frequency.color.copy(alpha = 0.2f)
                                } else {
                                    MaterialTheme.colorScheme.surface
                                }
                            ),
                            border = if (isSelected) {
                                BorderStroke(2.dp, frequency.color)
                            } else null
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                ) {
                                    Text(
                                        text = "${frequency.value} Hz",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isSelected) frequency.color else MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    if (isSelected) {
                                        Icon(
                                            Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = frequency.color,
                                            modifier = Modifier.size(16.dp)
                                        )
                                    }
                                }
                                
                                Text(
                                    text = frequency.name,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isSelected) frequency.color else MaterialTheme.colorScheme.onSurface
                                )
                                
                                Text(
                                    text = frequency.description,
                                    fontSize = 10.sp,
                                    color = if (isSelected) frequency.color.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 12.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AudioSettingsSection(
    binauralBeats: List<BinauralBeat>,
    modulationPresets: List<ModulationPreset>,
    selectedBinauralBeat: BinauralBeat?,
    selectedModulation: ModulationPreset?,
    volume: Float,
    onBinauralBeatSelected: (BinauralBeat) -> Unit,
    onModulationSelected: (ModulationPreset) -> Unit,
    onVolumeChanged: (Float) -> Unit
) {
    Column {
        Text(
            text = "Paramètres audio",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Battements binauraux
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF10B981).copy(alpha = 0.1f)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Battements binauraux",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF10B981)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    binauralBeats.take(3).forEach { beat ->
                        val isSelected = selectedBinauralBeat == beat
                        
                        Surface(
                            onClick = { onBinauralBeatSelected(beat) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            color = if (isSelected) Color(0xFF10B981).copy(alpha = 0.2f) else Color.Transparent,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(6.dp)) {
                                Text(
                                    text = "${beat.value} Hz - ${beat.name}",
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = beat.description,
                                    fontSize = 8.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // Modulation
            Card(
                modifier = Modifier.weight(1f),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF3B82F6).copy(alpha = 0.1f)
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = "Modulation",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3B82F6)
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    modulationPresets.take(3).forEach { preset ->
                        val isSelected = selectedModulation == preset
                        
                        Surface(
                            onClick = { onModulationSelected(preset) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp),
                            color = if (isSelected) Color(0xFF3B82F6).copy(alpha = 0.2f) else Color.Transparent,
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Column(modifier = Modifier.padding(6.dp)) {
                                Text(
                                    text = "${preset.name} (${preset.intensity})",
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                                    color = if (isSelected) Color(0xFF3B82F6) else MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = preset.description,
                                    fontSize = 8.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Contrôle du volume
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
                        Icons.Default.VolumeUp,
                        contentDescription = null,
                        tint = Color(0xFF805AD5)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Volume: ${volume.toInt()}%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Slider(
                    value = volume,
                    onValueChange = onVolumeChanged,
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = Color(0xFF805AD5),
                        activeTrackColor = Color(0xFF805AD5)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun PlaybackControlsSection(
    selectedFrequency: TherapyFrequency?,
    selectedBinauralBeat: BinauralBeat?,
    uiState: com.example.myapplication.ui.viewmodels.SoundTherapyState,
    onStartSession: (Int, Int) -> Unit,
    onStopSession: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                if (uiState.isPlaying) {
                    onStopSession()
                } else {
                    selectedFrequency?.let { freq ->
                        val binauralHz = selectedBinauralBeat?.value ?: 0
                        onStartSession(freq.value, binauralHz)
                    }
                }
            },
            enabled = selectedFrequency != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (uiState.isPlaying) Color(0xFFEF4444) else Color(0xFF805AD5)
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Icon(
                if (uiState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (uiState.isPlaying) "Arrêter la séance" else "Démarrer la thérapie sonore",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (selectedFrequency == null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Sélectionnez une fréquence pour commencer",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
                        Icons.Default.Info,
                        contentDescription = null,
                        tint = Color(0xFF805AD5),
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Comment fonctionne la thérapie sonore",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                Text(
                    text = "Les fréquences thérapeutiques influencent les ondes cérébrales et favorisent des états de conscience spécifiques. Chaque fréquence a des propriétés uniques qui peuvent aider à la guérison, à la relaxation et à l'équilibrage énergétique.",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }
        }

        // Conseils d'utilisation
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

                val tips = listOf(
                    "Utilisez des écouteurs pour une meilleure expérience, surtout avec les battements binauraux",
                    "Maintenez un volume confortable - les fréquences sont efficaces même à faible volume",
                    "Pratiquez régulièrement pour des bénéfices durables sur votre bien-être",
                    "Choisissez la fréquence qui correspond à votre intention du moment",
                    "Accordez-vous 10-20 minutes minimum pour ressentir les effets complets"
                )

                tips.forEach { tip ->
                    Row(
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Circle,
                            contentDescription = null,
                            tint = Color(0xFF4F46E5),
                            modifier = Modifier
                                .size(6.dp)
                                .padding(top = 6.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = tip,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}