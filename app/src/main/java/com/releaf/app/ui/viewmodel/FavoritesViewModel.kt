package com.releaf.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.releaf.app.data.repository.firebase.FavoritesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favorites: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class FavoritesViewModel(
    private val favoritesRepository: FavoritesRepository = FavoritesRepository(),
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()
    
    init {
        observeFavorites()
    }
    
    private fun observeFavorites() {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            favoritesRepository.getUserFavoritesFlow(userId)
                .catch { e ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Failed to observe favorites: ${e.message}"
                    )
                }
                .collect { favorites ->
                    _uiState.value = _uiState.value.copy(favorites = favorites.toSet())
                }
        }
    }
    
    fun addToFavorites(techniqueId: String) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            val result = favoritesRepository.addToFavorites(userId, techniqueId)
            result.onFailure { exception ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = exception.message
                )
            }
        }
    }
    
    fun removeFromFavorites(techniqueId: String) {
        val userId = auth.currentUser?.uid ?: return
        viewModelScope.launch {
            val result = favoritesRepository.removeFromFavorites(userId, techniqueId)
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