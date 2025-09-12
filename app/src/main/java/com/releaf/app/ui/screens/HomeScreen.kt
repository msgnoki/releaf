package com.releaf.app.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import com.releaf.app.R
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.releaf.app.data.Technique
import com.releaf.app.data.TechniquesRepository
import com.releaf.app.data.FavoritesPreferences
import com.releaf.app.data.model.TechniqueCategory
import com.releaf.app.ui.components.TechniqueCard
import com.releaf.app.ui.components.CategoryChipRow
import com.releaf.app.ui.theme.MyApplicationTheme

@Composable
fun HomeScreen(
    onTechniqueClick: (Technique) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val favoritesPreferences = remember { FavoritesPreferences(context) }
    var favoriteIds by remember { mutableStateOf(favoritesPreferences.getFavoriteIds()) }
    
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
            // Logo de l'app
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.releaf_logo),
                    contentDescription = "Releaf Logo",
                    modifier = Modifier.size(120.dp)
                )
            }
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
        
        // Chapitrage par catégories - chaque technique comme un item individuel
        techniquesByCategory.forEach { (category, techniques) ->
            // Titre de la catégorie
            item {
                Text(
                    text = category.displayName,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(bottom = 16.dp, top = 8.dp)
                )
            }
            
            // Techniques en grille - diviser en chunks de 4 pour faire des lignes
            val techniqueChunks = techniques.chunked(4)
            items(techniqueChunks) { techniqueRow ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    techniqueRow.forEach { technique ->
                        Box(modifier = Modifier.weight(1f)) {
                            TechniqueCard(
                                technique = technique,
                                onClick = { onTechniqueClick(technique) },
                                isFavorite = favoriteIds.contains(technique.id),
                                onFavoriteClick = {
                                    favoritesPreferences.toggleFavorite(technique.id)
                                    favoriteIds = favoritesPreferences.getFavoriteIds()
                                }
                            )
                        }
                    }
                    // Ajouter des espaces vides si la ligne n'est pas complète
                    repeat(4 - techniqueRow.size) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
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