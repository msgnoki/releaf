package com.example.myapplication.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Technique

@Composable
fun TechniqueCard(
    technique: Technique,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Memoize computed values to avoid recomposition
    val icon = remember(technique.icon) { getIconForTechnique(technique.icon) }
    val iconColor = remember(technique.iconColor) { getColorForTechnique(technique.iconColor) }
    val tagEmojis = remember(technique.tags) { 
        technique.tags.take(2).map { tag ->
            when (tag) {
                "high-anxiety" -> "🔥"
                "moderate-anxiety" -> "🧘"
                "short-time" -> "⏱️"
                "medium-time" -> "⏰"
                "long-time" -> "🕰️"
                else -> ""
            }
        }
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        TechniqueCardContent(
            name = technique.name,
            duration = technique.duration,
            icon = icon,
            iconColor = iconColor,
            tagEmojis = tagEmojis
        )
    }
}

@Composable
private fun TechniqueCardContent(
    name: String,
    duration: String,
    icon: ImageVector,
    iconColor: Color,
    tagEmojis: List<String>
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icon
        Icon(
            imageVector = icon,
            contentDescription = name,
            tint = iconColor,
            modifier = Modifier.size(40.dp)
        )
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Title
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Duration
        Text(
            text = duration,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Tags
        if (tagEmojis.isNotEmpty()) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                tagEmojis.forEach { emoji ->
                    if (emoji.isNotEmpty()) {
                        Surface(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.padding(horizontal = 2.dp)
                        ) {
                            Text(
                                text = emoji,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

// Extracted as pure functions to avoid unnecessary recomposition
private fun getIconForTechnique(iconName: String): ImageVector {
    return when (iconName) {
        "air" -> Icons.Default.Air
        "anchor" -> Icons.Default.Anchor
        "timer" -> Icons.Default.Timer
        "accessibility" -> Icons.Default.Accessibility
        "landscape" -> Icons.Default.Landscape
        "psychology" -> Icons.Default.Psychology
        "bubble_chart" -> Icons.Default.BubbleChart
        "graphic_eq" -> Icons.Default.GraphicEq
        "sports_handball" -> Icons.Default.SportsHandball
        else -> Icons.Default.Circle
    }
}

// Pure function for color mapping
private fun getColorForTechnique(colorName: String): Color {
    return when (colorName) {
        "cyan" -> Color.Cyan
        "green" -> Color.Green
        "blue" -> Color.Blue
        "purple" -> Color(0xFF9C27B0)
        "indigo" -> Color(0xFF3F51B5)
        "teal" -> Color(0xFF009688)
        "orange" -> Color(0xFFFF9800)
        else -> Color(0xFF3F51B5) // Default material blue instead of theme-dependent
    }
}