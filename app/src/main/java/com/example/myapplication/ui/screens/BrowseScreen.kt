package com.example.myapplication.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myapplication.data.TechniquesRepository
import com.example.myapplication.data.Technique
import com.example.myapplication.data.model.TechniqueCategory
import com.example.myapplication.ui.components.SearchBar
import com.example.myapplication.ui.components.CategoryChipRow
import com.example.myapplication.ui.components.TechniqueCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BrowseScreen(
    onTechniqueClick: (Technique) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategories by remember { mutableStateOf(emptyList<TechniqueCategory>()) }
    var isGridView by remember { mutableStateOf(true) }
    
    val allTechniques = remember { TechniquesRepository.getAllTechniques() }
    val allCategories = remember { TechniquesRepository.getAllCategories() }
    
    // Filter techniques based on search query and selected categories
    val filteredTechniques = remember(searchQuery, selectedCategories) {
        allTechniques.filter { technique ->
            val matchesSearch = if (searchQuery.isBlank()) {
                true
            } else {
                technique.name.contains(searchQuery, ignoreCase = true) ||
                technique.shortDescription.contains(searchQuery, ignoreCase = true) ||
                technique.tags.any { it.contains(searchQuery, ignoreCase = true) }
            }
            
            val matchesCategory = if (selectedCategories.isEmpty()) {
                true
            } else {
                selectedCategories.contains(technique.category)
            }
            
            matchesSearch && matchesCategory
        }
    }
    
    // Category counts for chips
    val categoryCounts = remember(allTechniques) {
        allCategories.associateWith { category ->
            allTechniques.count { it.category == category }
        }
    }
    
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Top bar with search and view toggle
        Surface(
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Explorer",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    
                    // View toggle button
                    IconButton(
                        onClick = { isGridView = !isGridView }
                    ) {
                        Icon(
                            imageVector = if (isGridView) Icons.Default.ViewList else Icons.Default.GridView,
                            contentDescription = if (isGridView) "Vue liste" else "Vue grille",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Search bar
                SearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    onSearch = { /* Already filtered on query change */ },
                    placeholder = "Rechercher des techniques..."
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Category filters
                CategoryChipRow(
                    categories = allCategories,
                    selectedCategories = selectedCategories,
                    onCategoryToggle = { category, isSelected ->
                        selectedCategories = if (isSelected) {
                            selectedCategories + category
                        } else {
                            selectedCategories - category
                        }
                    },
                    showCounts = true,
                    categoryCounts = categoryCounts
                )
            }
        }
        
        // Results section
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp)
        ) {
            // Results header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${filteredTechniques.size} technique${if (filteredTechniques.size > 1) "s" else ""} trouvée${if (filteredTechniques.size > 1) "s" else ""}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    
                    // Clear filters button
                    if (searchQuery.isNotBlank() || selectedCategories.isNotEmpty()) {
                        TextButton(
                            onClick = {
                                searchQuery = ""
                                selectedCategories = emptyList()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Effacer les filtres")
                        }
                    }
                }
            }
            
            // Techniques grid/list
            if (isGridView) {
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.height(((filteredTechniques.size / 2 + filteredTechniques.size % 2) * 200).dp)
                    ) {
                        items(filteredTechniques) { technique ->
                            TechniqueCard(
                                technique = technique,
                                onClick = { onTechniqueClick(technique) }
                            )
                        }
                    }
                }
            } else {
                items(filteredTechniques) { technique ->
                    TechniqueCard(
                        technique = technique,
                        onClick = { onTechniqueClick(technique) },
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
            
            // Empty state
            if (filteredTechniques.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Aucune technique trouvée",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Essayez d'ajuster vos filtres ou votre recherche",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                        )
                    }
                }
            }
        }
    }
}