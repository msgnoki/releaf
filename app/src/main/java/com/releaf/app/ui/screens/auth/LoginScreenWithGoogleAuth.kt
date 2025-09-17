package com.releaf.app.ui.screens.auth

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.releaf.app.data.auth.GoogleSignInHelper
import com.releaf.app.ui.viewmodel.AuthViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginScreenWithGoogleAuth(
    authViewModel: AuthViewModel,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    
    val googleSignInHelper = remember { GoogleSignInHelper(context) }
    
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        coroutineScope.launch {
            val intent = result.data
            if (intent != null) {
                val credentialResult = googleSignInHelper.getSignInCredentialFromIntent(intent)
                credentialResult.fold(
                    onSuccess = { idToken ->
                        authViewModel.signInWithGoogle(idToken)
                    },
                    onFailure = { error ->
                        // Handle error - could update AuthViewModel with error
                    }
                )
            }
        }
    }
    
    LoginScreen(
        onLoginClick = { email, password ->
            authViewModel.signIn(email, password)
        },
        onRegisterClick = onRegisterClick,
        onForgotPasswordClick = onForgotPasswordClick,
        onGoogleSignInClick = {
            coroutineScope.launch {
                val result = googleSignInHelper.beginSignIn()
                result.fold(
                    onSuccess = { intentSenderRequest ->
                        googleSignInLauncher.launch(intentSenderRequest)
                    },
                    onFailure = { error ->
                        // Handle error - could update AuthViewModel with error
                    }
                )
            }
        },
        isLoading = isLoading,
        errorMessage = errorMessage
    )
}