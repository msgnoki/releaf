package com.releaf.app.data.repository

import com.releaf.app.BuildConfig
import com.releaf.app.data.user.User
import com.releaf.app.data.user.AnxietyLevel
import com.releaf.app.services.ProgressService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Repository d'authentification pour le mode démo uniquement.
 * Le mode démo n'est disponible qu'en build DEBUG pour des raisons de sécurité.
 */
class AuthRepository(
    private val userRepository: UserRepository
) {

    companion object {
        private const val DEMO_USER_ID = "demo-user-id"
        private const val DEMO_EMAIL_DOMAIN = "@releaf.demo"
    }

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

    /**
     * Mode démo uniquement disponible en DEBUG
     * En release, cette méthode retourne toujours une erreur
     */
    suspend fun signInDemo(email: String, password: String): Result<Boolean> {
        // Sécurité: Le mode démo n'est disponible qu'en debug
        if (!BuildConfig.DEBUG) {
            return Result.failure(Exception("Le mode démo n'est pas disponible"))
        }

        // Valider que c'est une tentative de connexion démo
        val isDemoAttempt = email.endsWith(DEMO_EMAIL_DOMAIN) ||
                email.equals("demo", ignoreCase = true) ||
                email.contains("demo", ignoreCase = true)

        if (!isDemoAttempt) {
            return Result.failure(Exception("Utilisez Firebase Auth pour les comptes non-démo"))
        }

        return try {
            // Créer un utilisateur démo local
            val demoUser = User(
                uid = DEMO_USER_ID,
                email = "demo$DEMO_EMAIL_DOMAIN",
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
    }
    
    suspend fun signOut(): Result<Unit> {
        return try {
            // Reset all singleton services to prevent data leaks
            ProgressService.reset()

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
            userRepository.getUser(DEMO_USER_ID)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun checkIfDemoUserExists(): Boolean {
        // En release, pas de mode démo
        if (!BuildConfig.DEBUG) {
            return false
        }

        return try {
            val user = userRepository.getUser(DEMO_USER_ID)
            val exists = user != null
            _isLoggedIn.value = exists
            exists
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Indique si le mode démo est disponible (uniquement en DEBUG)
     */
    fun isDemoModeAvailable(): Boolean = BuildConfig.DEBUG

    fun isUserLoggedIn(): Boolean = _isLoggedIn.value
}