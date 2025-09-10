package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.user.AnxietyLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.repository.UserRepository

data class AuthState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoggedIn: Boolean = false
)

class AuthViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _authState = MutableStateFlow(AuthState())
    val authState: StateFlow<AuthState> = _authState.asStateFlow()
    
    private val _forgotPasswordState = MutableStateFlow<String?>(null)
    val forgotPasswordState: StateFlow<String?> = _forgotPasswordState.asStateFlow()
    
    init {
        // Désactivé pour tests - forcer l'affichage de l'écran de connexion
        // viewModelScope.launch {
        //     val exists = authRepository.checkIfDemoUserExists()
        //     _authState.value = _authState.value.copy(isLoggedIn = exists)
        //     
        //     // Observer les changements d'état de connexion
        //     authRepository.isLoggedInFlow.collect { isLoggedIn ->
        //         _authState.value = _authState.value.copy(isLoggedIn = isLoggedIn)
        //     }
        // }
    }
    
    fun signIn(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)
            
            val result = authRepository.signInDemo(email, password)
            result.fold(
                onSuccess = {
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        isLoggedIn = true,
                        errorMessage = null
                    )
                },
                onFailure = { exception ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        errorMessage = getErrorMessage(exception)
                    )
                }
            )
        }
    }
    
    fun signUp(email: String, password: String, displayName: String, anxietyLevel: AnxietyLevel) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)
            
            // En mode démo, rediriger vers la connexion démo
            if (email == "demo@releaf.com") {
                signIn(email, password)
            } else {
                _authState.value = _authState.value.copy(
                    isLoading = false,
                    errorMessage = "Inscription non disponible en mode démo. Utilisez demo@releaf.com"
                )
            }
        }
    }
    
    fun signOut() {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true)
            
            val result = authRepository.signOut()
            result.fold(
                onSuccess = {
                    _authState.value = AuthState() // Reset to default state
                },
                onFailure = { exception ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        errorMessage = getErrorMessage(exception)
                    )
                }
            )
        }
    }
    
    fun resetPassword(email: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isLoading = true, errorMessage = null)
            _forgotPasswordState.value = null
            
            val result = authRepository.resetPassword(email)
            result.fold(
                onSuccess = {
                    _authState.value = _authState.value.copy(isLoading = false)
                    _forgotPasswordState.value = "Un e-mail de réinitialisation a été envoyé à $email"
                },
                onFailure = { exception ->
                    _authState.value = _authState.value.copy(
                        isLoading = false,
                        errorMessage = getErrorMessage(exception)
                    )
                }
            )
        }
    }
    
    fun clearError() {
        _authState.value = _authState.value.copy(errorMessage = null)
    }
    
    fun clearForgotPasswordMessage() {
        _forgotPasswordState.value = null
    }
    
    private fun getErrorMessage(exception: Throwable): String {
        return when (exception.message) {
            "The email address is badly formatted." -> "Format d'e-mail invalide"
            "There is no user record corresponding to this identifier. The user may have been deleted." -> "Aucun compte trouvé avec cette adresse e-mail"
            "The password is invalid or the user does not have a password." -> "Mot de passe incorrect"
            "The email address is already in use by another account." -> "Cette adresse e-mail est déjà utilisée"
            "The password must be 6 characters long or more." -> "Le mot de passe doit contenir au moins 6 caractères"
            "A network error (such as timeout, interrupted connection or unreachable host) has occurred." -> "Erreur de connexion réseau"
            else -> exception.message ?: "Une erreur inconnue s'est produite"
        }
    }
}