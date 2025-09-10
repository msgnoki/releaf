package com.example.myapplication.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    onLoginClick: (email: String, password: String) -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    isLoading: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val passwordFocusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    
    val isFormValid = email.isNotBlank() && password.isNotBlank()
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))
        
        // App Logo/Title
        Icon(
            Icons.Default.SelfImprovement,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Releaf",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = "Votre compagnon de gestion du stress",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(48.dp))
        
        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Adresse e-mail") },
            leadingIcon = {
                Icon(Icons.Default.Email, contentDescription = null)
            },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { passwordFocusRequester.requestFocus() }
            ),
            singleLine = true,
            enabled = !isLoading
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mot de passe") },
            leadingIcon = {
                Icon(Icons.Default.Lock, contentDescription = null)
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Masquer le mot de passe" else "Afficher le mot de passe"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(passwordFocusRequester),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    if (isFormValid && !isLoading) {
                        onLoginClick(email, password)
                    }
                }
            ),
            singleLine = true,
            enabled = !isLoading
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Forgot Password
        TextButton(
            onClick = onForgotPasswordClick,
            enabled = !isLoading
        ) {
            Text("Mot de passe oublié ?")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Error Message
        if (errorMessage != null) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = errorMessage,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Login Button
        Button(
            onClick = { onLoginClick(email, password) },
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormValid && !isLoading
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Text("Se connecter")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Divider
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = "ou",
                modifier = Modifier.padding(horizontal = 16.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Register Button
        OutlinedButton(
            onClick = onRegisterClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Créer un compte")
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Terms and Privacy
        Text(
            text = "En continuant, vous acceptez nos conditions d'utilisation et notre politique de confidentialité",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            lineHeight = 16.sp
        )
    }
}

// Preview Functions
@Preview(name = "Normal State", showBackground = true)
@Composable
fun LoginScreenPreview() {
    MyApplicationTheme {
        LoginScreen(
            onLoginClick = { _, _ -> },
            onRegisterClick = { },
            onForgotPasswordClick = { }
        )
    }
}

@Preview(name = "Loading State", showBackground = true)
@Composable
fun LoginScreenLoadingPreview() {
    MyApplicationTheme {
        LoginScreen(
            onLoginClick = { _, _ -> },
            onRegisterClick = { },
            onForgotPasswordClick = { },
            isLoading = true
        )
    }
}

@Preview(name = "With Error", showBackground = true)
@Composable
fun LoginScreenWithErrorPreview() {
    MyApplicationTheme {
        LoginScreen(
            onLoginClick = { _, _ -> },
            onRegisterClick = { },
            onForgotPasswordClick = { },
            errorMessage = "Email ou mot de passe incorrect. Veuillez réessayer."
        )
    }
}

@PreviewLightDark
@Composable
fun LoginScreenLightDarkPreview() {
    MyApplicationTheme {
        LoginScreen(
            onLoginClick = { _, _ -> },
            onRegisterClick = { },
            onForgotPasswordClick = { }
        )
    }
}

@PreviewScreenSizes
@Composable
fun LoginScreenSizesPreview() {
    MyApplicationTheme {
        LoginScreen(
            onLoginClick = { _, _ -> },
            onRegisterClick = { },
            onForgotPasswordClick = { }
        )
    }
}

@Preview(
    name = "Landscape",
    showBackground = true,
    widthDp = 640,
    heightDp = 360
)
@Composable
fun LoginScreenLandscapePreview() {
    MyApplicationTheme {
        LoginScreen(
            onLoginClick = { _, _ -> },
            onRegisterClick = { },
            onForgotPasswordClick = { }
        )
    }
}