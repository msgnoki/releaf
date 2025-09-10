package com.example.myapplication.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.myapplication.R
import com.example.myapplication.data.Technique
import kotlinx.coroutines.delay

// Modèle de données pour les étiquettes de pensées
data class ThoughtLabel(
    val id: String,
    val name: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val backgroundColor: Color
)

// Modèle pour une pensée étiquetée
data class LabeledThought(
    val text: String,
    val labels: List<String>,
    val timestamp: Long
)

// Modèle pour les insights de session
data class SessionInsight(
    val type: String,
    val title: String,
    val description: String,
    val icon: ImageVector
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThoughtLabelingScreen(
    technique: Technique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var exerciseStarted by remember { mutableStateOf(false) }
    var exerciseCompleted by remember { mutableStateOf(false) }
    var thoughtInput by remember { mutableStateOf("") }
    var currentThought by remember { mutableStateOf("") }
    var selectedLabels by remember { mutableStateOf<List<String>>(emptyList()) }
    var labeledThoughts by remember { mutableStateOf<List<LabeledThought>>(emptyList()) }
    var elapsedTime by remember { mutableLongStateOf(0L) }

    // Timer pour le temps écoulé
    LaunchedEffect(exerciseStarted) {
        if (exerciseStarted && !exerciseCompleted) {
            while (exerciseStarted && !exerciseCompleted) {
                delay(1000)
                elapsedTime += 1000
            }
        }
    }

    // Étiquettes de pensées disponibles
    val thoughtLabels = remember {
        listOf(
            ThoughtLabel("worry", "Inquiétude", "Pensées anxieuses sur l'avenir", Icons.Default.Cloud, Color(0xFFF59E0B), Color(0xFFFEF3C7)),
            ThoughtLabel("catastrophic", "Catastrophique", "Imaginer le pire scénario possible", Icons.Default.Warning, Color(0xFFEF4444), Color(0xFFFEE2E2)),
            ThoughtLabel("self-doubt", "Doute de soi", "Remise en question de ses capacités", Icons.Default.Help, Color(0xFFF97316), Color(0xFFFED7AA)),
            ThoughtLabel("perfectionist", "Perfectionnisme", "Besoin que tout soit parfait", Icons.Default.Star, Color(0xFFEAB308), Color(0xFFFEF3C7)),
            ThoughtLabel("comparison", "Comparaison", "Se comparer aux autres", Icons.Default.Balance, Color(0xFF8B5CF6), Color(0xFFF3E8FF)),
            ThoughtLabel("rumination", "Rumination", "Ressasser les mêmes pensées", Icons.Default.Refresh, Color(0xFF3B82F6), Color(0xFFDBEAFE)),
            ThoughtLabel("control", "Contrôle", "Vouloir tout contrôler", Icons.Default.Settings, Color(0xFF4F46E5), Color(0xFFE0E7FF)),
            ThoughtLabel("rejection", "Rejet", "Peur d'être rejeté ou jugé", Icons.Default.Favorite, Color(0xFFEC4899), Color(0xFFFCE7F3)),
            ThoughtLabel("performance", "Performance", "Anxiété de performance", Icons.Default.EmojiEvents, Color(0xFF10B981), Color(0xFFD1FAE5)),
            ThoughtLabel("health", "Santé", "Préoccupations sur la santé", Icons.Default.LocalHospital, Color(0xFFEF4444), Color(0xFFFEE2E2)),
            ThoughtLabel("social", "Social", "Anxiété sociale", Icons.Default.Groups, Color(0xFF14B8A6), Color(0xFFCCFBF1)),
            ThoughtLabel("financial", "Financier", "Soucis d'argent", Icons.Default.AttachMoney, Color(0xFF059669), Color(0xFFD1FAE5))
        )
    }

    // Insights de session calculés
    val sessionInsights = remember(labeledThoughts) {
        if (labeledThoughts.isEmpty()) emptyList()
        else {
            val labelCounts = mutableMapOf<String, Int>()
            labeledThoughts.forEach { thought ->
                thought.labels.forEach { labelId ->
                    labelCounts[labelId] = labelCounts.getOrDefault(labelId, 0) + 1
                }
            }

            val insights = mutableListOf<SessionInsight>()
            
            // Pattern le plus fréquent
            val mostCommonLabel = labelCounts.maxByOrNull { it.value }
            if (mostCommonLabel != null) {
                val label = thoughtLabels.find { it.id == mostCommonLabel.key }
                if (label != null) {
                    insights.add(
                        SessionInsight(
                            "pattern",
                            "Pattern dominant identifié",
                            "Vos pensées contiennent souvent des éléments de '${label.name}'",
                            Icons.Default.BarChart
                        )
                    )
                }
            }

            // Insight sur la prise de conscience
            if (labeledThoughts.size >= 3) {
                insights.add(
                    SessionInsight(
                        "awareness",
                        "Conscience renforcée",
                        "Vous avez développé une meilleure conscience de vos patterns de pensée",
                        Icons.Default.Psychology
                    )
                )
            }

            insights
        }
    }

    fun formatTime(ms: Long): String {
        val minutes = ms / 60000
        val seconds = (ms % 60000) / 1000
        return "${minutes}:${seconds.toString().padStart(2, '0')}"
    }

    fun getLabelById(id: String) = thoughtLabels.find { it.id == id }

    fun startExercise() {
        exerciseStarted = true
        exerciseCompleted = false
        elapsedTime = 0L
        labeledThoughts = emptyList()
        thoughtInput = ""
        currentThought = ""
        selectedLabels = emptyList()
    }

    fun setCurrentThought() {
        if (thoughtInput.trim().isNotEmpty()) {
            currentThought = thoughtInput.trim()
            thoughtInput = ""
            selectedLabels = emptyList()
        }
    }

    fun clearCurrentThought() {
        currentThought = ""
        selectedLabels = emptyList()
    }

    fun toggleLabel(labelId: String) {
        selectedLabels = if (selectedLabels.contains(labelId)) {
            selectedLabels - labelId
        } else {
            selectedLabels + labelId
        }
    }

    fun saveThought() {
        if (currentThought.isNotEmpty() && selectedLabels.isNotEmpty()) {
            val newThought = LabeledThought(
                text = currentThought,
                labels = selectedLabels,
                timestamp = System.currentTimeMillis()
            )
            labeledThoughts = labeledThoughts + newThought
            clearCurrentThought()
        }
    }

    fun resetExercise() {
        exerciseStarted = false
        exerciseCompleted = false
        thoughtInput = ""
        currentThought = ""
        selectedLabels = emptyList()
        labeledThoughts = emptyList()
        elapsedTime = 0L
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text(stringResource(R.string.thought_labeling_title)) },
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
            if (!exerciseStarted && !exerciseCompleted) {
                // Écran d'introduction
                item {
                    IntroductionSection(
                        technique = technique,
                        thoughtLabels = thoughtLabels,
                        onStartExercise = ::startExercise
                    )
                }
            } else if (exerciseCompleted) {
                // Écran de completion
                item {
                    CompletionSection(
                        labeledThoughts = labeledThoughts,
                        sessionInsights = sessionInsights,
                        totalTime = elapsedTime,
                        onComplete = onComplete,
                        onRestart = ::resetExercise,
                        formatTime = ::formatTime
                    )
                }
            } else {
                // Interface d'exercice
                item {
                    ExerciseHeaderSection(
                        elapsedTime = elapsedTime,
                        labeledCount = labeledThoughts.size,
                        formatTime = ::formatTime
                    )
                }

                if (labeledThoughts.isNotEmpty() && currentThought.isEmpty()) {
                    item {
                        LabeledThoughtsSection(
                            labeledThoughts = labeledThoughts,
                            getLabelById = ::getLabelById
                        )
                    }
                }

                if (currentThought.isEmpty()) {
                    item {
                        ThoughtInputSection(
                            thoughtInput = thoughtInput,
                            onThoughtInputChange = { thoughtInput = it },
                            onSetCurrentThought = ::setCurrentThought
                        )
                    }
                } else {
                    item {
                        CurrentThoughtSection(currentThought = currentThought)
                    }
                    
                    item {
                        LabelsGridSection(
                            thoughtLabels = thoughtLabels,
                            selectedLabels = selectedLabels,
                            onToggleLabel = ::toggleLabel
                        )
                    }
                    
                    item {
                        ActionButtonsSection(
                            selectedLabels = selectedLabels,
                            onCancel = ::clearCurrentThought,
                            onSave = ::saveThought
                        )
                    }
                }

                if (labeledThoughts.size >= 3 && currentThought.isEmpty()) {
                    item {
                        Button(
                            onClick = { exerciseCompleted = true },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF10B981)
                            )
                        ) {
                            Icon(Icons.Default.Check, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(stringResource(R.string.finish_exercise))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun IntroductionSection(
    technique: Technique,
    thoughtLabels: List<ThoughtLabel>,
    onStartExercise: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Psychology,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = Color(0xFF4F46E5)
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

        // Bénéfices
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BenefitCard(
                icon = Icons.Default.RemoveRedEye,
                title = "Conscience",
                description = "Reconnaître ses patterns",
                color = Color(0xFF4F46E5),
                modifier = Modifier.weight(1f)
            )
            BenefitCard(
                icon = Icons.Default.Balance,
                title = "Équilibre",
                description = "Distance émotionnelle",
                color = Color(0xFF8B5CF6),
                modifier = Modifier.weight(1f)
            )
            BenefitCard(
                icon = Icons.Default.Lightbulb,
                title = "Clarté",
                description = "Pensée plus claire",
                color = Color(0xFF3B82F6),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onStartExercise,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F46E5)
            )
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Commencer l'exercice",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun BenefitCard(
    icon: ImageVector,
    title: String,
    description: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                fontSize = 10.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ExerciseHeaderSection(
    elapsedTime: Long,
    labeledCount: Int,
    formatTime: (Long) -> String
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4F46E5).copy(alpha = 0.1f)
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Psychology,
                contentDescription = null,
                tint = Color(0xFF4F46E5),
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Étiquetage des pensées",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Observer et catégoriser",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = labeledCount.toString(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4F46E5)
                )
                Text(
                    text = "pensées • ${formatTime(elapsedTime)}",
                    fontSize = 10.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun LabeledThoughtsSection(
    labeledThoughts: List<LabeledThought>,
    getLabelById: (String) -> ThoughtLabel?
) {
    Column {
        Text(
            text = "Vos pensées étiquetées",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        labeledThoughts.forEach { thought ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        text = thought.text,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        thought.labels.forEach { labelId ->
                            getLabelById(labelId)?.let { label ->
                                Surface(
                                    color = label.backgroundColor,
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            label.icon,
                                            contentDescription = null,
                                            tint = label.color,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = label.name,
                                            fontSize = 10.sp,
                                            color = label.color,
                                            fontWeight = FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ThoughtInputSection(
    thoughtInput: String,
    onThoughtInputChange: (String) -> Unit,
    onSetCurrentThought: () -> Unit
) {
    Column {
        Text(
            text = "Qu'est-ce qui vous préoccupe ?",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Écrivez une pensée anxieuse que vous avez en ce moment :",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = thoughtInput,
            onValueChange = onThoughtInputChange,
            placeholder = { Text(stringResource(R.string.thought_input_placeholder)) },
            modifier = Modifier.fillMaxWidth(),
            minLines = 4,
            supportingText = { Text("${thoughtInput.length}/500") }
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onSetCurrentThought,
            enabled = thoughtInput.trim().isNotEmpty(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4F46E5)
            )
        ) {
            Text(stringResource(R.string.label_this_thought))
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
private fun CurrentThoughtSection(currentThought: String) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4F46E5).copy(alpha = 0.1f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.ChatBubble,
                    contentDescription = null,
                    tint = Color(0xFF4F46E5)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Votre pensée :",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF4F46E5)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currentThought,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
private fun LabelsGridSection(
    thoughtLabels: List<ThoughtLabel>,
    selectedLabels: List<String>,
    onToggleLabel: (String) -> Unit
) {
    Column {
        Text(
            text = "Choisissez les étiquettes qui décrivent le mieux cette pensée :",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(400.dp)
        ) {
            items(thoughtLabels) { label ->
                val isSelected = selectedLabels.contains(label.id)
                
                Card(
                    onClick = { onToggleLabel(label.id) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) {
                            label.backgroundColor
                        } else {
                            MaterialTheme.colorScheme.surface
                        }
                    ),
                    border = if (isSelected) {
                        BorderStroke(2.dp, label.color)
                    } else null
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (isSelected) label.color else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(6.dp),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                label.icon,
                                contentDescription = null,
                                tint = if (isSelected) Color.White else label.color,
                                modifier = Modifier
                                    .size(16.dp)
                                    .padding(8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = label.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (isSelected) label.color else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = label.description,
                                fontSize = 10.sp,
                                color = if (isSelected) label.color.copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2
                            )
                        }

                        if (isSelected) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = label.color,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActionButtonsSection(
    selectedLabels: List<String>,
    onCancel: () -> Unit,
    onSave: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedButton(
            onClick = onCancel,
            modifier = Modifier.weight(1f)
        ) {
            Icon(Icons.Default.Close, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(stringResource(R.string.cancel))
        }

        Button(
            onClick = onSave,
            enabled = selectedLabels.isNotEmpty(),
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF10B981)
            )
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(4.dp))
            Text(stringResource(R.string.continue_text))
        }
    }
}

@Composable
private fun CompletionSection(
    labeledThoughts: List<LabeledThought>,
    sessionInsights: List<SessionInsight>,
    totalTime: Long,
    onComplete: () -> Unit,
    onRestart: () -> Unit,
    formatTime: (Long) -> String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF10B981).copy(alpha = 0.1f)
            )
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    tint = Color(0xFF10B981)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Étiquetage terminé !",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Vous avez étiqueté ${labeledThoughts.size} pensée${if (labeledThoughts.size > 1) "s" else ""} en ${formatTime(totalTime)}.",
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (sessionInsights.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "Insights de session",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    sessionInsights.forEach { insight ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF3B82F6).copy(alpha = 0.1f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    insight.icon,
                                    contentDescription = null,
                                    tint = Color(0xFF3B82F6)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = insight.title,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = Color(0xFF3B82F6)
                                    )
                                    Text(
                                        text = insight.description,
                                        fontSize = 10.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Bénéfices obtenus
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RemoveRedEye, contentDescription = null, tint = Color(0xFF4F46E5), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Conscience accrue", fontSize = 10.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Psychology, contentDescription = null, tint = Color(0xFF8B5CF6), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Pensées organisées", fontSize = 10.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFFEF4444), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Distance créée", fontSize = 10.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Button(
                onClick = onRestart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF4F46E5)
                )
            ) {
                Icon(Icons.Default.Refresh, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Recommencer l'exercice", fontSize = 16.sp)
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