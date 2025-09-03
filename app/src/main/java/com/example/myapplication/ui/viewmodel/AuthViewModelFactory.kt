package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.repository.AuthRepository
import com.example.myapplication.data.repository.UserRepository

class AuthViewModelFactory(private val database: AppDatabase) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            val userRepository = UserRepository(database)
            val authRepository = AuthRepository(userRepository = userRepository)
            return AuthViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}