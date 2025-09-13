package com.releaf.app.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.releaf.app.R
import com.releaf.app.data.Technique
import com.releaf.app.data.TechniquesRepository
import com.releaf.app.ui.components.TechniqueCard
import com.releaf.app.ui.viewmodel.FavoritesViewModel
import com.releaf.app.ui.viewmodel.FavoritesViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritesScreen(
    onTechniqueClick: (Technique) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val favoritesViewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModelFactory())
    val uiState by favoritesViewModel.uiState.collectAsState()
    
    // Show error message
    uiState.errorMessage?.let { error ->
        LaunchedEffect(error) {
            // You could show a snackbar here
            favoritesViewModel.clearError()
        }
    }
    
    // Get favorite techniques from Firebase
    val favoriteTechniques = remember(uiState.favorites) {
        TechniquesRepository.getAllTechniques().filter { technique ->
            uiState.favorites.contains(technique.id)
        }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Favoris",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        if (favoriteTechniques.isEmpty()) {
            // Empty state
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.FavoriteBorder,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 16.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
                
                Text(
                    text = "Aucun favori",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Ajoutez vos techniques préférées en appuyant sur le cœur dans la liste des exercices",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline,
                    textAlign = TextAlign.Center,
                    lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
                )
            }
        } else {
            // Favorites grid
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(favoriteTechniques) { technique ->
                    TechniqueCard(
                        technique = technique,
                        onClick = { onTechniqueClick(technique) },
                        isFavorite = true,
                        onFavoriteClick = { 
                            favoritesViewModel.removeFromFavorites(technique.id)
                        }
                    )
                }
            }
        }
    }
}