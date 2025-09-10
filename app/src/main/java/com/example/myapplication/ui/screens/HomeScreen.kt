package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.data.Technique
import com.example.myapplication.data.TechniquesRepository
import com.example.myapplication.data.model.TechniqueCategory
import com.example.myapplication.ui.components.TechniqueCard
import com.example.myapplication.ui.components.CategoryChipRow
import com.example.myapplication.ui.theme.MyApplicationTheme

@Composable
fun HomeScreen(
    onTechniqueClick: (Technique) -> Unit,
    modifier: Modifier = Modifier
) {
    // Get all techniques and categories
    val allTechniques by remember { 
        mutableStateOf(TechniquesRepository.getAllTechniques()) 
    }
    val allCategories by remember {
        mutableStateOf(TechniquesRepository.getAllCategories())
    }
    
    // Filter states
    var selectedCategories by remember { mutableStateOf(emptyList<TechniqueCategory>()) }
    
    // Filter techniques based on selected categories
    val filteredTechniques = remember(selectedCategories) {
        if (selectedCategories.isEmpty()) {
            allTechniques
        } else {
            allTechniques.filter { selectedCategories.contains(it.category) }
        }
    }
    
    // Group techniques by category for chapitrage
    val techniquesByCategory = remember(filteredTechniques) {
        filteredTechniques.groupBy { it.category }
    }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        item {
            // Titre de l'app
            Text(
                text = "Releaf",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        
        item {
            // Tags de filtrage (sans icônes)
            CategoryChipRow(
                categories = allCategories,
                selectedCategories = selectedCategories,
                onCategoryToggle = { category, isSelected ->
                    selectedCategories = if (isSelected) {
                        selectedCategories + category
                    } else {
                        selectedCategories - category
                    }
                }
            )
        }
        
        // Chapitrage par catégories
        techniquesByCategory.forEach { (category, techniques) ->
            item {
                TechniqueCategorySection(
                    category = category,
                    techniques = techniques,
                    onTechniqueClick = onTechniqueClick
                )
            }
        }
    }
}

@Composable
private fun TechniqueCategorySection(
    category: TechniqueCategory,
    techniques: List<Technique>,
    onTechniqueClick: (Technique) -> Unit
) {
    Column {
        // Titre de la catégorie
        Text(
            text = "${category.displayName} (${techniques.size})",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Grille de techniques (responsive: 2-4 colonnes selon la largeur)
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 80.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.height((kotlin.math.ceil(techniques.size / 4.0).toInt() * 140).dp)
        ) {
            items(
                items = techniques,
                key = { it.id }
            ) { technique ->
                TechniqueCard(
                    technique = technique,
                    onClick = remember(technique.id) { 
                        { onTechniqueClick(technique) } 
                    }
                )
            }
        }
    }
}

// Preview Functions
@Preview(name = "Light Mode", showBackground = true)
@Composable
fun HomeScreenPreview() {
    MyApplicationTheme {
        HomeScreen(
            onTechniqueClick = { }
        )
    }
}

@PreviewLightDark
@Composable
fun HomeScreenLightDarkPreview() {
    MyApplicationTheme {
        HomeScreen(
            onTechniqueClick = { }
        )
    }
}

@PreviewScreenSizes
@Composable
fun HomeScreenDifferentSizesPreview() {
    MyApplicationTheme {
        HomeScreen(
            onTechniqueClick = { }
        )
    }
}