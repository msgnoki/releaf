package com.releaf.app.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.releaf.app.data.user.User
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class FirebaseAuthRepository(
    private val userRepository: UserRepository
) {
    
    private val auth = FirebaseAuth.getInstance()
    
    val currentUserFlow: Flow<FirebaseUser?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }
        auth.addAuthStateListener(authStateListener)
        
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
    
    val isLoggedInFlow: Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        auth.addAuthStateListener(authStateListener)
        
        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }
    
    suspend fun signUp(email: String, password: String, displayName: String): Result<Boolean> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("User creation failed"))
            
            // Update display name
            val profileUpdates = userProfileChangeRequest {
                this.displayName = displayName
            }
            firebaseUser.updateProfile(profileUpdates).await()
            
            // Create user in local database
            val user = User(
                uid = firebaseUser.uid,
                email = firebaseUser.email ?: email,
                displayName = displayName,
                createdAt = System.currentTimeMillis(),
                lastLoginAt = System.currentTimeMillis()
            )
            
            userRepository.insertUser(user)
            userRepository.createDefaultUserPreferences(user.uid)
            
            Result.success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(Exception("Erreur lors de la création du compte: ${e.message}"))
        }
    }
    
    suspend fun signIn(email: String, password: String): Result<Boolean> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Login failed"))
            
            // Update or create user in local database
            val existingUser = userRepository.getUser(firebaseUser.uid)
            if (existingUser == null) {
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: email,
                    displayName = firebaseUser.displayName ?: "Utilisateur",
                    createdAt = System.currentTimeMillis(),
                    lastLoginAt = System.currentTimeMillis()
                )
                userRepository.insertUser(user)
                userRepository.createDefaultUserPreferences(user.uid)
            } else {
                // Update last login time
                val updatedUser = existingUser.copy(lastLoginAt = System.currentTimeMillis())
                userRepository.insertUser(updatedUser)
            }
            
            Result.success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(Exception("Erreur de connexion: ${e.message}"))
        }
    }
    
    suspend fun signInWithGoogle(idToken: String): Result<Boolean> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = auth.signInWithCredential(credential).await()
            val firebaseUser = result.user ?: return Result.failure(Exception("Google Sign-In failed"))
            
            // Update or create user in local database
            val existingUser = userRepository.getUser(firebaseUser.uid)
            if (existingUser == null) {
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: "Utilisateur",
                    createdAt = System.currentTimeMillis(),
                    lastLoginAt = System.currentTimeMillis()
                )
                userRepository.insertUser(user)
                userRepository.createDefaultUserPreferences(user.uid)
            } else {
                // Update last login time
                val updatedUser = existingUser.copy(lastLoginAt = System.currentTimeMillis())
                userRepository.insertUser(updatedUser)
            }
            
            Result.success(true)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(Exception("Erreur de connexion Google: ${e.message}"))
        }
    }
    
    suspend fun signOut(): Result<Unit> {
        return try {
            auth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            auth.sendPasswordResetEmail(email).await()
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(Exception("Erreur lors de l'envoi de l'email de réinitialisation: ${e.message}"))
        }
    }
    
    suspend fun getCurrentUserData(): User? {
        return try {
            val firebaseUser = auth.currentUser ?: return null
            userRepository.getUser(firebaseUser.uid)
        } catch (e: Exception) {
            null
        }
    }
    
    fun getCurrentFirebaseUser(): FirebaseUser? = auth.currentUser
    
    fun isUserLoggedIn(): Boolean = auth.currentUser != null
    
    suspend fun updateProfile(displayName: String): Result<Unit> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("Utilisateur non connecté"))
            
            val profileUpdates = userProfileChangeRequest {
                this.displayName = displayName
            }
            user.updateProfile(profileUpdates).await()
            
            // Update local database
            val localUser = userRepository.getUser(user.uid)
            if (localUser != null) {
                val updatedUser = localUser.copy(displayName = displayName)
                userRepository.insertUser(updatedUser)
            }
            
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(Exception("Erreur lors de la mise à jour du profil: ${e.message}"))
        }
    }
    
    suspend fun deleteAccount(): Result<Unit> {
        return try {
            val user = auth.currentUser ?: return Result.failure(Exception("Utilisateur non connecté"))
            val userId = user.uid
            
            // Delete from local database first
            userRepository.deleteUser(userId)
            
            // Delete Firebase account
            user.delete().await()
            
            Result.success(Unit)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(Exception("Erreur lors de la suppression du compte: ${e.message}"))
        }
    }
}