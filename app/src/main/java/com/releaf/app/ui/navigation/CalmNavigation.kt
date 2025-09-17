package com.releaf.app.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.releaf.app.data.TechniquesRepository
import com.releaf.app.ui.screens.*
import com.releaf.app.ui.screens.auth.*
import com.releaf.app.ui.viewmodel.AuthViewModel
import com.releaf.app.ui.viewmodel.SessionViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.releaf.app.ReleafApplication
import com.releaf.app.ui.viewmodel.AuthViewModelFactory

@Composable
fun CalmNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val application = context.applicationContext as ReleafApplication
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModelFactory(application.database))
    val sessionViewModel: SessionViewModel = viewModel()
    val authState by authViewModel.authState.collectAsState()
    val forgotPasswordMessage by authViewModel.forgotPasswordState.collectAsState()
    
    var showSplash by remember { mutableStateOf(true) }
    
    if (showSplash) {
        SplashScreen(
            onSplashFinished = { showSplash = false },
            modifier = modifier
        )
    } else {
        // Navigate to home when user logs in - only after splash is finished
        LaunchedEffect(authState.isLoggedIn) {
            if (authState.isLoggedIn) {
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        
        NavHost(
            navController = navController,
            startDestination = if (authState.isLoggedIn) "home" else "login", // Mode auth activé
            modifier = modifier
        ) {
        
        // Authentication screens
        composable("login") {
            LoginScreenWithGoogleAuth(
                authViewModel = authViewModel,
                onRegisterClick = {
                    navController.navigate("register")
                },
                onForgotPasswordClick = {
                    navController.navigate("forgot_password")
                },
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }
        
        composable("register") {
            RegisterScreen(
                onRegisterClick = { email, password, displayName, anxietyLevel ->
                    authViewModel.signUp(email, password, displayName, anxietyLevel)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage
            )
        }
        
        composable("forgot_password") {
            ForgotPasswordScreen(
                onResetPasswordClick = { email ->
                    authViewModel.resetPassword(email)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                isLoading = authState.isLoading,
                errorMessage = authState.errorMessage,
                successMessage = forgotPasswordMessage
            )
        }
        composable("home") {
            HomeScreen(
                onTechniqueClick = { technique ->
                    navController.navigate("technique/${technique.id}")
                }
            )
        }
        
        composable("favorites") {
            FavoritesScreen(
                onTechniqueClick = { technique ->
                    navController.navigate("technique/${technique.id}")
                }
            )
        }
        
        composable("sleep") {
            SleepScreen(
                onTechniqueClick = { technique ->
                    navController.navigate("technique/${technique.id}")
                }
            )
        }
        
        composable("profile") {
            ProfileScreen()
        }
        
        
        composable("session_completion/{techniqueId}") { backStackEntry ->
            val techniqueId = backStackEntry.arguments?.getString("techniqueId") ?: return@composable
            val technique = TechniquesRepository.getTechnique(techniqueId) ?: return@composable
            
            SessionCompletionScreen(
                technique = technique,
                sessionViewModel = sessionViewModel,
                onComplete = {
                    sessionViewModel.resetState()
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }
        
        composable("technique/{techniqueId}") { backStackEntry ->
            val techniqueId = backStackEntry.arguments?.getString("techniqueId") ?: return@composable
            val technique = TechniquesRepository.getTechnique(techniqueId) ?: return@composable
            
            TechniqueDetailScreen(
                technique = technique,
                onBackClick = {
                    navController.popBackStack()
                },
                onStartExercise = { selectedTechnique ->
                    if (selectedTechnique.id == "guided-breathing") {
                        navController.navigate("guided_breathing_selection/${selectedTechnique.id}")
                    } else {
                        navController.navigate("exercise/${selectedTechnique.id}")
                    }
                },
                onRelatedTechniqueClick = { relatedTechnique ->
                    navController.navigate("technique/${relatedTechnique.id}")
                }
            )
        }
        
        composable("exercise/{techniqueId}") { backStackEntry ->
            val techniqueId = backStackEntry.arguments?.getString("techniqueId") ?: return@composable
            val technique = TechniquesRepository.getTechnique(techniqueId) ?: return@composable
            
            when (techniqueId) {
                "breathing" -> {
                    // Démarrer la session quand on entre dans l'écran
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    BreathingExerciseScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "grounding" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    GroundingScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "guided-breathing" -> {
                    // Cette route n'est plus utilisée, la navigation se fait directement vers guided_breathing_selection
                    GuidedBreathingExerciseScreen(
                        technique = technique,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.popBackStack("technique/${technique.id}", false)
                        }
                    )
                }
                "progressive-muscle-relaxation" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    ProgressiveMuscleRelaxationScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "peaceful-visualization" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    PeacefulVisualizationScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "thought-labeling" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    ThoughtLabelingScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "stress-relief-bubbles" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    StressReliefBubblesScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "sound-therapy" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    SoundTherapyScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "stress-ball" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    StressBallScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "breathing-stress-15" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    StressBreathingExerciseScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "respiration-e12" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    DiaphragmaticBreathingScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "autogenic-training" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    AutogenicTrainingScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "breathing-box" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    BoxBreathingScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "breathing-478" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    Breathing478Screen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "body-scan-meditation" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    BodyScanScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "mindful-breathing" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    MindfulBreathingScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "loving-kindness-meditation" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    LovingKindnessScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "auto-hypnosis-autogenic" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    AutoHypnosisAutogenicScreen(
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "forest-immersion-nature" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    ForestImmersionScreen(
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                "meditation-breath-awareness" -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    MeditationBreathAwarenessScreen(
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
                else -> {
                    LaunchedEffect(Unit) {
                        sessionViewModel.startSession(technique)
                    }
                    
                    ExerciseScreen(
                        technique = technique,
                        onBackClick = {
                            sessionViewModel.cancelSession()
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.navigate("session_completion/${technique.id}")
                        }
                    )
                }
            }
        }
        
        // Écran de sélection des techniques de respiration guidée
        composable("guided_breathing_selection/{techniqueId}") { backStackEntry ->
            val techniqueId = backStackEntry.arguments?.getString("techniqueId") ?: return@composable
            val technique = TechniquesRepository.getTechnique(techniqueId) ?: return@composable
            
            GuidedBreathingSelectionScreen(
                technique = technique,
                onBackClick = {
                    navController.popBackStack()
                },
                onTechniqueSelected = { selectedGuidedTechnique ->
                    navController.navigate("guided_breathing_advanced/${technique.id}/${selectedGuidedTechnique.key}")
                }
            )
        }
        
        // Écran d'exercice avancé de respiration guidée
        composable("guided_breathing_advanced/{techniqueId}/{guidedTechniqueKey}") { backStackEntry ->
            val techniqueId = backStackEntry.arguments?.getString("techniqueId") ?: return@composable
            val guidedTechniqueKey = backStackEntry.arguments?.getString("guidedTechniqueKey") ?: return@composable
            
            // Recréer la technique guidée sélectionnée
            val selectedGuidedTechnique = remember(guidedTechniqueKey) {
                when (guidedTechniqueKey) {
                    "box" -> GuidedBreathingTechnique(
                        key = "box",
                        name = "Respiration carrée",
                        description = "Un timing égal pour toutes les phases crée une clarté mentale et une concentration.",
                        timing = "4-4-4-4",
                        color = Color(0xFF3B82F6),
                        icon = Icons.Default.CropSquare,
                        bestFor = "Concentration et focalisation",
                        pattern = listOf(
                            BreathingPhase("inhale", 4000, "Inspirez"),
                            BreathingPhase("hold_in", 4000, "Retenez"),
                            BreathingPhase("exhale", 4000, "Expirez"),
                            BreathingPhase("hold_out", 4000, "Retenez")
                        )
                    )
                    "calming" -> GuidedBreathingTechnique(
                        key = "calming",
                        name = "Technique 4-7-8",
                        description = "Maintien prolongé pour une relaxation profonde",
                        timing = "4-7-8",
                        color = Color(0xFF8B5CF6),
                        icon = Icons.Default.NightsStay,
                        bestFor = "Relaxation profonde et sommeil",
                        pattern = listOf(
                            BreathingPhase("inhale", 4000, "Inspirez"),
                            BreathingPhase("hold_in", 7000, "Retenez"),
                            BreathingPhase("exhale", 8000, "Expirez")
                        )
                    )
                    "energizing" -> GuidedBreathingTechnique(
                        key = "energizing",
                        name = "4-4-6 Énergisant",
                        description = "Modèle équilibré pour la vigilance",
                        timing = "4-4-6",
                        color = Color(0xFF10B981),
                        icon = Icons.Default.Bolt,
                        bestFor = "Énergie et clarté mentale",
                        pattern = listOf(
                            BreathingPhase("inhale", 4000, "Inspirez"),
                            BreathingPhase("hold_in", 4000, "Retenez"),
                            BreathingPhase("exhale", 6000, "Expirez")
                        )
                    )
                    "quick" -> GuidedBreathingTechnique(
                        key = "quick",
                        name = "Réinitialisation rapide",
                        description = "Technique rapide pour un soulagement immédiat",
                        timing = "3-3-3",
                        color = Color(0xFFF59E0B),
                        icon = Icons.Default.Schedule,
                        bestFor = "Soulagement rapide du stress",
                        pattern = listOf(
                            BreathingPhase("inhale", 3000, "Inspirez"),
                            BreathingPhase("hold_in", 3000, "Retenez"),
                            BreathingPhase("exhale", 3000, "Expirez")
                        )
                    )
                    else -> null
                }
            }
            
            selectedGuidedTechnique?.let { guidedTech ->
                GuidedBreathingAdvancedScreen(
                    selectedTechnique = guidedTech,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onComplete = {
                        navController.popBackStack("technique/$techniqueId", false)
                    }
                )
            }
        }
    }
    } // Closing brace for else block
}