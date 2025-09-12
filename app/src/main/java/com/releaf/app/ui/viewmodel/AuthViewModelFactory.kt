package com.releaf.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.releaf.app.data.database.AppDatabase
import com.releaf.app.data.repository.FirebaseAuthRepository
import com.releaf.app.data.repository.UserRepository

class AuthViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val userRepository = UserRepository(database)
            val authRepository = FirebaseAuthRepository(userRepository = userRepository)
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}