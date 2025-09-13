package com.releaf.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.releaf.app.data.model.firebase.*
import com.releaf.app.data.repository.firebase.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    
    private val auth = FirebaseAuth.getInstance()
    private val userRepository = FirebaseUserRepository()
    private val progressRepository = ProgressRepository()
    private val badgeRepository = BadgeRepository()
    private val weeklyProgressRepository = WeeklyProgressRepository()
    private val userPreferencesRepository = UserPreferencesRepository()
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    init {
        loadUserProfile()
    }
    
    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                errorMessage = "Utilisateur non connecté"
            )
            return
        }
        
        val userId = currentUser.uid
        
        // Combine all user data flows
        viewModelScope.launch {
            combine(
                userRepository.getUserFlow(userId),
                progressRepository.getProgressFlow(userId),
                badgeRepository.getUserUnlockedBadgesFlow(userId),
                weeklyProgressRepository.getCurrentWeekProgressFlow(userId),
                userPreferencesRepository.getPreferencesFlow(userId)
            ) { user, progress, badges, weeklyProgress, preferences ->
                ProfileData(
                    user = user,
                    progress = progress,
                    unlockedBadges = badges,
                    weeklyProgress = weeklyProgress,
                    preferences = preferences
                )
            }.catch { error ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Erreur lors du chargement: ${error.message}"
                )
            }.collect { profileData ->
                // Load badge definitions for unlocked badges
                loadBadgeDefinitions(profileData.unlockedBadges.map { it.badgeId })
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    profileData = profileData,
                    errorMessage = null
                )
            }
        }
    }
    
    private fun loadBadgeDefinitions(badgeIds: List<String>) {
        viewModelScope.launch {
            val badgeDefinitionsResult = badgeRepository.getAllBadgeDefinitions()
            badgeDefinitionsResult.onSuccess { allBadges ->
                val userBadgeDefinitions = allBadges.filter { badge ->
                    badgeIds.contains(badge.badgeId)
                }
                
                _uiState.value = _uiState.value.copy(
                    badgeDefinitions = userBadgeDefinitions
                )
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erreur lors du chargement des badges: ${error.message}"
                )
            }
        }
    }
    
    fun updateLanguage(languageCode: String) {
        val currentUser = auth.currentUser ?: return
        
        viewModelScope.launch {
            userPreferencesRepository.updateLanguage(currentUser.uid, languageCode)
                .onFailure { error ->
                    _uiState.value = _uiState.value.copy(
                        errorMessage = "Erreur lors de la mise à jour de la langue: ${error.message}"
                    )
                }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            try {
                auth.signOut()
                _uiState.value = ProfileUiState() // Reset state
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "Erreur lors de la déconnexion: ${e.message}"
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
    
    fun initializeUserDataIfNeeded() {
        val currentUser = auth.currentUser ?: return
        val userId = currentUser.uid
        
        viewModelScope.launch {
            // Check if user data exists, if not create it
            val userResult = userRepository.getUser(userId)
            if (userResult.isSuccess && userResult.getOrNull() == null) {
                // Create user profile
                val firebaseUser = FirebaseUser(
                    email = currentUser.email ?: "",
                    displayName = currentUser.displayName ?: "Utilisateur",
                    level = 1,
                    currentXp = 0,
                    createdAt = System.currentTimeMillis(),
                    updatedAt = System.currentTimeMillis()
                )
                userRepository.createUser(userId, firebaseUser)
                
                // Create progress
                val progress = Progress(
                    userId = userId,
                    updatedAt = System.currentTimeMillis()
                )
                progressRepository.createProgress(userId, progress)
                
                // Create preferences
                userPreferencesRepository.createDefaultPreferences(userId)
                
                // Initialize weekly progress
                weeklyProgressRepository.initializeCurrentWeek(userId)
            }
        }
    }
}

data class ProfileUiState(
    val isLoading: Boolean = true,
    val profileData: ProfileData? = null,
    val badgeDefinitions: List<BadgeDefinition> = emptyList(),
    val errorMessage: String? = null
)

data class ProfileData(
    val user: FirebaseUser?,
    val progress: Progress?,
    val unlockedBadges: List<UnlockedBadge>,
    val weeklyProgress: WeeklyProgress?,
    val preferences: UserPreferences?
)