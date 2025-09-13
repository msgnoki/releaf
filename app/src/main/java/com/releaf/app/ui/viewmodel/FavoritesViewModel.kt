package com.releaf.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.releaf.app.data.firebase.FirestoreUserRepositorySimple
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favorites: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class FavoritesViewModel(
    private val firestoreRepository: FirestoreUserRepositorySimple = FirestoreUserRepositorySimple()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()
    
    init {
        observeFavorites()
    }
    
    private fun observeFavorites() {
        viewModelScope.launch {
            firestoreRepository.getFavoritesFlow()
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to observe favorites: ${e.message}"
                    )
                }
                .collect { favorites ->
                    _uiState.value = _uiState.value.copy(favorites = favorites)
                }
        }
    }
    
    fun addToFavorites(techniqueId: String) {
        viewModelScope.launch {
            val result = firestoreRepository.addToFavorites(techniqueId)
            result.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = exception.message
                )
            }
        }
    }
    
    fun removeFromFavorites(techniqueId: String) {
        viewModelScope.launch {
            val result = firestoreRepository.removeFromFavorites(techniqueId)
            result.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = exception.message
                )
            }
        }
    }
    
    fun toggleFavorite(techniqueId: String) {
        val currentFavorites = _uiState.value.favorites
        if (currentFavorites.contains(techniqueId)) {
            removeFromFavorites(techniqueId)
        } else {
            addToFavorites(techniqueId)
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}