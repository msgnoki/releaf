package com.releaf.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.releaf.app.data.model.firebase.*
import com.releaf.app.data.repository.firebase.*
import com.releaf.app.services.StatisticsService
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    
    private val auth = FirebaseAuth.getInstance()
    private val userRepository = FirebaseUserRepository()
    private val progressRepository = ProgressRepository()
    private val badgeRepository = BadgeRepository()
    private val weeklyProgressRepository = WeeklyProgressRepository()
    private val userPreferencesRepository = UserPreferencesRepository()
    private val sessionRepository = SessionRepository()
    private val statisticsService = StatisticsService()
    
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
    
    // Mood history for the graph
    private val _moodHistory = MutableStateFlow<List<MoodEntry>>(emptyList())
    val moodHistory: StateFlow<List<MoodEntry>> = _moodHistory.asStateFlow()
    
    init {
        loadUserProfile()
        setupMoodHistoryFlow()
    }
    
    data class MoodEntry(
        val date: Long,
        val moodBefore: Int,
        val moodAfter: Int,
        val improvement: Int = moodAfter - moodBefore
    )
    
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
        
        // Combine all user data flows with real statistics
        viewModelScope.launch {
            combine(
                userRepository.getUserFlow(userId),
                statisticsService.getStatisticsFlow(),
                badgeRepository.getUserUnlockedBadgesFlow(userId),
                userPreferencesRepository.getPreferencesFlow(userId)
            ) { user, statistics, badges, preferences ->
                // Convert statistics to progress format
                val progress = statistics?.let { stats ->
                    // Calculate sessions this week from StatisticsService
                    val sessionsThisWeek = if (stats.weeklyMinutes > 0 && stats.averageSessionDuration > 0) {
                        stats.weeklyMinutes / stats.averageSessionDuration
                    } else {
                        0
                    }
                    
                    Progress(
                        userId = userId,
                        totalSessions = stats.totalSessions,
                        totalMinutes = stats.totalMinutes,
                        currentStreak = stats.currentStreak,
                        longestStreak = stats.longestStreak,
                        sessionsThisWeek = sessionsThisWeek,
                        minutesThisWeek = stats.weeklyMinutes,
                        averageMoodImprovement = stats.averageMoodImprovement,
                        lastSessionDate = java.time.Instant.now().toString()
                    )
                }
                
                // Enrich user data with statistics
                val enrichedUser = user?.copy(
                    level = statistics?.level ?: user.level,
                    currentXp = statistics?.currentXp ?: user.currentXp
                )
                
                ProfileData(
                    user = enrichedUser,
                    progress = progress,
                    unlockedBadges = badges,
                    weeklyProgress = null, // Will be handled by statistics
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
    
    private fun setupMoodHistoryFlow() {
        val currentUser = auth.currentUser ?: return
        val userId = currentUser.uid
        
        viewModelScope.launch {
            try {
                // Use the reactive Flow to get real-time updates
                sessionRepository.getUserSessionsFlow(userId, limit = 30)
                    .map { sessions ->
                        sessions
                            .filter { it.moodBefore > 0 && it.moodAfter > 0 }
                            .map { session ->
                                MoodEntry(
                                    date = session.createdAt,
                                    moodBefore = session.moodBefore,
                                    moodAfter = session.moodAfter
                                )
                            }
                            .sortedBy { it.date }
                    }
                    .collect { moodEntries ->
                        _moodHistory.value = moodEntries
                    }
            } catch (e: Exception) {
                // Silent fail for mood history
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