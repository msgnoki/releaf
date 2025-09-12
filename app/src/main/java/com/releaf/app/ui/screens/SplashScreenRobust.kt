package com.releaf.app.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import com.releaf.app.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.releaf.app.data.CitationsRepository
import com.releaf.app.data.model.Citation
import com.releaf.app.ui.theme.AccentPrimary
import com.releaf.app.ui.theme.AccentSecondary
import kotlinx.coroutines.delay

@Composable
fun SplashScreenRobust(
    onSplashFinished: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var citation by remember { mutableStateOf<Citation?>(null) }
    var isVisible by remember { mutableStateOf(false) }
    var timeRemaining by remember { mutableStateOf(4) } // 4 secondes total
    
    // Timer robuste basé sur l'horloge système
    LaunchedEffect(Unit) {
        CitationsRepository.loadCitations(context)
        citation = CitationsRepository.getRandomCitation()
        
        val startTime = System.currentTimeMillis()
        val totalDuration = 4000L // 4 secondes précises
        
        // Animation d'entrée après 500ms
        delay(500L)
        isVisible = true
        
        // Mise à jour du compteur toutes les 100ms
        while (System.currentTimeMillis() - startTime < totalDuration) {
            val elapsed = System.currentTimeMillis() - startTime
            timeRemaining = ((totalDuration - elapsed) / 1000L).toInt() + 1
            delay(100L)
        }
        
        // Debug: afficher la durée réelle
        val actualDuration = System.currentTimeMillis() - startTime
        println("DEBUG: Splash screen - Durée réelle: ${actualDuration}ms (attendu: 4000ms)")
        
        onSplashFinished()
    }
    
    // Animation for fade in/out
    val alpha by animateFloatAsState(
        targetValue = if (isVisible) 1f else 0f,
        animationSpec = tween(
            durationMillis = 1000,
            easing = FastOutSlowInEasing
        ),
        label = "alpha_animation"
    )
    
    // Background gradient
    val gradient = Brush.verticalGradient(
        colors = listOf(
            AccentPrimary.copy(alpha = 0.1f),
            AccentSecondary.copy(alpha = 0.1f),
            Color.White
        )
    )
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(gradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(32.dp)
                .wrapContentHeight()
        ) {
            // App Logo
            Image(
                painter = painterResource(id = R.drawable.releaf_logo),
                contentDescription = "Releaf Logo",
                modifier = Modifier
                    .size(160.dp)
                    .padding(bottom = 48.dp)
            )
            
            // Citation with animation
            AnimatedVisibility(
                visible = citation != null,
                enter = fadeIn(
                    animationSpec = tween(
                        durationMillis = 1500,
                        delayMillis = 500
                    )
                ) + slideInVertically(
                    animationSpec = tween(
                        durationMillis = 1500,
                        delayMillis = 500
                    ),
                    initialOffsetY = { it / 2 }
                )
            ) {
                citation?.let { cite ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    ) {
                        // Quote marks and citation
                        Text(
                            text = "« ${cite.citation} »",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 18.sp,
                                lineHeight = 26.sp
                            ),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        
                        // Author
                        Text(
                            text = "— ${cite.auteur}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Medium,
                            color = AccentSecondary,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            
            // Loading indicator with timer
            Spacer(modifier = Modifier.height(48.dp))
            
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = AccentPrimary.copy(alpha = 0.7f),
                    strokeWidth = 2.dp
                )
                
                // Debug timer display
                if (timeRemaining > 0) {
                    Text(
                        text = "${timeRemaining}s",
                        fontSize = 12.sp,
                        color = AccentPrimary.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}
