package com.releaf.app.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.releaf.app.data.Technique
import kotlinx.coroutines.delay

data class GroundingStep(
    val number: Int,
    val title: String,
    val instruction: String,
    val icon: ImageVector,
    val color: Color,
    val examples: List<String>
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroundingScreen(
    technique: Technique,
    onBackClick: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStepIndex by remember { mutableStateOf(0) }
    var isActive by remember { mutableStateOf(false) }
    var completedItems by remember { mutableStateOf<List<String>>(emptyList()) }
    var sessionCompleted by remember { mutableStateOf(false) }
    
    val steps = remember {
        listOf(
            GroundingStep(
                number = 5,
                title = "5 choses que vous VOYEZ",
                instruction = "Regardez autour de vous et identifiez 5 choses que vous pouvez voir",
                icon = Icons.Default.Visibility,
                color = Color(0xFF2196F3),
                examples = listOf("Un livre", "Une plante", "Une fenêtre", "Vos mains", "Un objet coloré")
            ),
            GroundingStep(
                number = 4,
                title = "4 choses que vous TOUCHEZ",
                instruction = "Identifiez 4 textures ou sensations tactiles",
                icon = Icons.Default.TouchApp,
                color = Color(0xFF4CAF50),
                examples = listOf("La texture de vos vêtements", "La surface de votre chaise", "La température de l'air", "Vos pieds au sol")
            ),
            GroundingStep(
                number = 3,
                title = "3 choses que vous ENTENDEZ",
                instruction = "Écoutez attentivement et identifiez 3 sons",
                icon = Icons.Default.VolumeUp,
                color = Color(0xFFFF9800),
                examples = listOf("Le bruit de fond", "Votre respiration", "Des sons lointains", "Le silence")
            ),
            GroundingStep(
                number = 2,
                title = "2 choses que vous SENTEZ",
                instruction = "Concentrez-vous sur 2 odeurs ou parfums",
                icon = Icons.Default.Cloud,
                color = Color(0xFF9C27B0),
                examples = listOf("L'air frais", "Un parfum familier", "L'odeur de l'espace", "Votre environnement")
            ),
            GroundingStep(
                number = 1,
                title = "1 chose que vous GOÛTEZ",
                instruction = "Identifiez 1 goût dans votre bouche",
                icon = Icons.Default.Restaurant,
                color = Color(0xFFE91E63),
                examples = listOf("Le goût actuel dans votre bouche", "L'arrière-goût d'une boisson", "La fraîcheur de votre salive")
            )
        )
    }
    
    val currentStep = if (currentStepIndex < steps.size) steps[currentStepIndex] else null
    
    // Auto-complete session after all steps
    LaunchedEffect(currentStepIndex) {
        if (currentStepIndex >= steps.size && !sessionCompleted) {
            sessionCompleted = true
            delay(2000) // Wait 2 seconds before completing
            onComplete()
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Ancrage 5-4-3-2-1") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        )
        
        if (sessionCompleted) {
            // Completion Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Exercice terminé !",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Vous avez completé la technique d'ancrage 5-4-3-2-1. Vous devriez vous sentir plus ancré et présent.",
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )
            }
        } else if (currentStep != null) {
            // Current Step Screen
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
            ) {
                // Progress indicator
                LinearProgressIndicator(
                    progress = { currentStepIndex.toFloat() / steps.size },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = currentStep.color,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Step header
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = currentStep.color.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(currentStep.color, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentStep.number.toString(),
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Icon(
                            currentStep.icon,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                            tint = currentStep.color
                        )
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        Text(
                            text = currentStep.title,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = currentStep.instruction,
                            fontSize = 16.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Examples section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                modifier = Modifier.size(20.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Exemples pour vous aider :",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        currentStep.examples.forEach { example ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(currentStep.color, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = example,
                                    fontSize = 14.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Navigation buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Previous button
                    if (currentStepIndex > 0) {
                        OutlinedButton(
                            onClick = { 
                                currentStepIndex--
                            },
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                        ) {
                            Icon(
                                Icons.Default.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Précédent")
                        }
                    }
                    
                    // Next/Finish button
                    FilledTonalButton(
                        onClick = { 
                            currentStepIndex++
                        },
                        modifier = Modifier.weight(if (currentStepIndex > 0) 1f else 2f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = currentStep.color,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            if (currentStepIndex < steps.size - 1) "Suivant" else "Terminer"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            if (currentStepIndex < steps.size - 1) Icons.Default.ArrowForward else Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }
        }
    }
}