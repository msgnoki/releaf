package com.releaf.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.releaf.app.data.Technique
import com.releaf.app.ui.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SessionCompletionScreen(
    technique: Technique,
    sessionViewModel: SessionViewModel = viewModel(),
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var moodAfter by remember { mutableStateOf(7) }
    var notes by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    
    val sessionState by sessionViewModel.sessionState.collectAsState()
    val duration = sessionViewModel.getCurrentDuration()
    
    // Auto-complétion quand la session est enregistrée
    LaunchedEffect(sessionState.isSessionCompleted) {
        if (sessionState.isSessionCompleted) {
            onComplete()
        }
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Icône de félicitation
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Félicitations !",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Vous avez terminé la session « ${technique.name} »",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
            
            Text(
                text = "Durée: ${duration} minute${if (duration > 1) "s" else ""}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Sélection d'humeur après
            Text(
                text = "Comment vous sentez-vous maintenant ?",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Slider pour l'humeur
            Column {
                Slider(
                    value = moodAfter.toFloat(),
                    onValueChange = { moodAfter = it.toInt() },
                    valueRange = 1f..10f,
                    steps = 8,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "😔 Mal",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "$moodAfter/10",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = "😊 Très bien",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Notes optionnelles
            OutlinedTextField(
                value = notes,
                onValueChange = { notes = it },
                label = { Text("Notes (optionnel)") },
                placeholder = { Text("Comment s'est passée cette session ?") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 3,
                shape = RoundedCornerShape(12.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Affichage des erreurs
            if (sessionState.errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = sessionState.errorMessage ?: "Erreur inconnue",
                        modifier = Modifier.padding(12.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // Boutons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { 
                        sessionViewModel.cancelSession()
                        onComplete() 
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isSubmitting
                ) {
                    Text("Ignorer")
                }
                
                Button(
                    onClick = {
                        isSubmitting = true
                        sessionViewModel.completeSession(moodAfter, notes)
                    },
                    modifier = Modifier.weight(1f),
                    enabled = !isSubmitting
                ) {
                    if (isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Enregistrer")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Amélioration affichée
            val improvement = moodAfter - sessionState.moodBefore
            if (improvement > 0) {
                Text(
                    text = "Amélioration: +$improvement points 🎉",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}