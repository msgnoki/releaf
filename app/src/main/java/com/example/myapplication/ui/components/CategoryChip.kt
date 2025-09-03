package com.example.myapplication.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.model.TechniqueCategory
import com.example.myapplication.ui.theme.CategoryColors

@Composable
fun CategoryChip(
    category: TechniqueCategory,
    isSelected: Boolean = false,
    onSelectionChanged: (Boolean) -> Unit = {},
    modifier: Modifier = Modifier,
    showCount: Boolean = false,
    count: Int = 0
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) {
            CategoryColors.getCategoryColor(category, false)
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "background_color"
    )
    
    val contentColor by animateColorAsState(
        targetValue = if (isSelected) {
            Color.White
        } else {
            MaterialTheme.colorScheme.onSurface
        },
        label = "content_color"
    )
    
    Surface(
        modifier = modifier
            .clip(RoundedCornerShape(20.dp))
            .toggleable(
                value = isSelected,
                onValueChange = onSelectionChanged
            ),
        color = backgroundColor,
        contentColor = contentColor,
        tonalElevation = if (isSelected) 4.dp else 1.dp,
        shadowElevation = if (isSelected) 2.dp else 0.dp,
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.emoji,
                style = MaterialTheme.typography.bodyMedium
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                ),
                color = contentColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            if (showCount && count > 0) {
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "($count)",
                    style = MaterialTheme.typography.bodySmall,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}

@Composable
fun CategoryChipRow(
    categories: List<TechniqueCategory>,
    selectedCategories: List<TechniqueCategory>,
    onCategoryToggle: (TechniqueCategory, Boolean) -> Unit,
    modifier: Modifier = Modifier,
    showCounts: Boolean = false,
    categoryCounts: Map<TechniqueCategory, Int> = emptyMap()
) {
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(categories) { category ->
            CategoryChip(
                category = category,
                isSelected = selectedCategories.contains(category),
                onSelectionChanged = { isSelected ->
                    onCategoryToggle(category, isSelected)
                },
                showCount = showCounts,
                count = categoryCounts[category] ?: 0
            )
        }
    }
}

@Composable
fun CompactCategoryChip(
    category: TechniqueCategory,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = CategoryColors.getCategoryColor(category, false).copy(alpha = 0.2f),
        contentColor = CategoryColors.getCategoryColor(category, false),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = category.emoji,
                style = MaterialTheme.typography.bodySmall
            )
            
            Spacer(modifier = Modifier.width(4.dp))
            
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}