package com.example.myapplication.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun BreathingAnimation(
    isActive: Boolean,
    modifier: Modifier = Modifier,
    onPhaseChange: (String) -> Unit = {}
) {
    var animationState by remember { mutableStateOf(BreathingPhase.PREPARE) }
    var cycleCount by remember { mutableStateOf(0) }
    
    val animationValue by animateFloatAsState(
        targetValue = when (animationState) {
            BreathingPhase.PREPARE -> 0.5f
            BreathingPhase.INHALE -> 1f
            BreathingPhase.HOLD_IN -> 1f
            BreathingPhase.EXHALE -> 0.3f
            BreathingPhase.HOLD_OUT -> 0.3f
        },
        animationSpec = when (animationState) {
            BreathingPhase.PREPARE -> tween(1000)
            BreathingPhase.INHALE -> tween(4000, easing = LinearEasing)
            BreathingPhase.HOLD_IN -> tween(100)
            BreathingPhase.EXHALE -> tween(6000, easing = LinearEasing)
            BreathingPhase.HOLD_OUT -> tween(100)
        },
        label = "breathing_animation" // Stable label for performance
    )
    
    // Animation cycle management
    LaunchedEffect(isActive) {
        if (isActive) {
            while (isActive) {
                // Inhale phase
                animationState = BreathingPhase.INHALE
                onPhaseChange("Inspirez lentement...")
                delay(4000)
                
                if (!isActive) break
                
                // Hold inhale
                animationState = BreathingPhase.HOLD_IN
                onPhaseChange("Retenez...")
                delay(2000)
                
                if (!isActive) break
                
                // Exhale phase
                animationState = BreathingPhase.EXHALE
                onPhaseChange("Expirez doucement...")
                delay(6000)
                
                if (!isActive) break
                
                // Hold exhale
                animationState = BreathingPhase.HOLD_OUT
                onPhaseChange("Pause...")
                delay(2000)
                
                cycleCount++
            }
        } else {
            animationState = BreathingPhase.PREPARE
            onPhaseChange("Appuyez sur commencer quand vous êtes prêt")
        }
    }
    
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(250.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            ) {
                drawBreathingCircle(animationValue)
            }
            
            // Breathing instructions overlay
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = when (animationState) {
                        BreathingPhase.PREPARE -> "Prêt ?"
                        BreathingPhase.INHALE -> "Inspirez"
                        BreathingPhase.HOLD_IN -> "Retenez"
                        BreathingPhase.EXHALE -> "Expirez"
                        BreathingPhase.HOLD_OUT -> "Pause"
                    },
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                
                if (cycleCount > 0) {
                    Text(
                        text = "Cycle $cycleCount",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Breathing pattern indicator
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            repeat(4) { index ->
                val isActive = when (animationState) {
                    BreathingPhase.INHALE -> index < 4
                    BreathingPhase.HOLD_IN -> true
                    BreathingPhase.EXHALE -> index < 2
                    BreathingPhase.HOLD_OUT -> false
                    else -> false
                }
                
                Surface(
                    color = if (isActive) MaterialTheme.colorScheme.primary 
                           else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(8.dp)
                        .padding(horizontal = 2.dp)
                ) {}
            }
        }
    }
}

private fun DrawScope.drawBreathingCircle(animationValue: Float) {
    val centerX = size.width / 2
    val centerY = size.height / 2
    val maxRadius = size.minDimension / 2 * 0.8f
    val currentRadius = maxRadius * animationValue
    
    // Outer ring
    drawCircle(
        color = Color(0xFF6200EE).copy(alpha = 0.1f),
        radius = maxRadius,
        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
    )
    
    // Animated circle
    drawCircle(
        color = Color(0xFF6200EE).copy(alpha = 0.6f),
        radius = currentRadius,
        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
    )
    
    // Inner circle
    drawCircle(
        color = Color(0xFF6200EE).copy(alpha = 0.8f),
        radius = currentRadius * 0.7f,
        center = androidx.compose.ui.geometry.Offset(centerX, centerY)
    )
}

private enum class BreathingPhase {
    PREPARE, INHALE, HOLD_IN, EXHALE, HOLD_OUT
}