package com.example.myapplication.data.repository

import com.example.myapplication.data.user.User
import com.example.myapplication.data.user.AnxietyLevel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

// Version simplifiée pour mode démo uniquement
class AuthRepository(
    private val userRepository: UserRepository
) {
    
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedInFlow: Flow<Boolean> = _isLoggedIn.asStateFlow()
    
    init {
        // Vérifier si un utilisateur démo existe déjà au démarrage
        checkExistingDemoUser()
    }
    
    private fun checkExistingDemoUser() {
        // Cette vérification sera faite dans le ViewModel de manière asynchrone
    }
    
    // Méthode factice pour compatibilité avec l'ancien code
    fun getCurrentUserFlow(): Flow<Any?> = kotlinx.coroutines.flow.flowOf(null)
    
    suspend fun signInDemo(email: String, password: String): Result<Boolean> {
        return if (email == "demo@brythee.com" && password == "demo123") {
            try {
                // Créer un utilisateur démo local
                val demoUser = User(
                    uid = "demo-user-id",
                    email = email,
                    displayName = "Utilisateur Démo",
                    createdAt = System.currentTimeMillis(),
                    lastLoginAt = System.currentTimeMillis()
                )
                userRepository.insertUser(demoUser)
                if (userRepository.getUserPreferences(demoUser.uid) == null) {
                    userRepository.createDefaultUserPreferences(demoUser.uid)
                }
                _isLoggedIn.value = true
                Result.success(true)
            } catch (e: Exception) {
                Result.failure(Exception("Erreur lors de la création du compte démo: ${e.message}"))
            }
        } else if (email == "demo@brythee.com") {
            Result.failure(Exception("Mot de passe démo incorrect. Utilisez 'demo123'"))
        } else {
            Result.failure(Exception("Email non reconnu. Utilisez demo@brythee.com"))
        }
    }
    
    suspend fun signOut(): Result<Unit> {
        return try {
            _isLoggedIn.value = false
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun resetPassword(email: String): Result<Unit> {
        return Result.success(Unit) // Simuler le succès en mode démo
    }
    
    suspend fun getCurrentUserData(): User? {
        return try {
            userRepository.getUser("demo-user-id")
        } catch (e: Exception) {
            null
        }
    }
    
    suspend fun checkIfDemoUserExists(): Boolean {
        return try {
            val user = userRepository.getUser("demo-user-id")
            val exists = user != null
            _isLoggedIn.value = exists
            exists
        } catch (e: Exception) {
            false
        }
    }
    
    fun isUserLoggedIn(): Boolean = _isLoggedIn.value
}