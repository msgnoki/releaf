package com.releaf.app.data.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.tasks.await

class GoogleSignInHelper(private val context: Context) {
    
    private val oneTapClient: SignInClient = Identity.getSignInClient(context)
    
    private val signInRequest = BeginSignInRequest.builder()
        .setGoogleIdTokenRequestOptions(
            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                .setSupported(true)
                .setServerClientId(context.getString(com.releaf.app.R.string.default_web_client_id))
                .setFilterByAuthorizedAccounts(false)
                .build()
        )
        .setAutoSelectEnabled(true)
        .build()
    
    suspend fun beginSignIn(): Result<IntentSenderRequest> {
        return try {
            val result = oneTapClient.beginSignIn(signInRequest).await()
            val intentSenderRequest = IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
            Result.success(intentSenderRequest)
        } catch (e: ApiException) {
            Result.failure(e)
        }
    }
    
    suspend fun getSignInCredentialFromIntent(intent: Intent): Result<String> {
        return try {
            val credential = oneTapClient.getSignInCredentialFromIntent(intent)
            val idToken = credential.googleIdToken
            if (idToken != null) {
                Result.success(idToken)
            } else {
                Result.failure(Exception("No ID token found"))
            }
        } catch (e: ApiException) {
            Result.failure(e)
        }
    }
}