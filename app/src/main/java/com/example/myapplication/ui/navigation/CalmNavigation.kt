package com.example.myapplication.ui.navigation

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myapplication.data.TechniquesRepository
import com.example.myapplication.ui.screens.*

@Composable
fun CalmNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        composable("home") {
            HomeScreen(
                onTechniqueClick = { technique ->
                    navController.navigate("technique/${technique.id}")
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
                    BreathingExerciseScreen(
                        technique = technique,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.popBackStack("technique/${technique.id}", false)
                        }
                    )
                }
                "grounding" -> {
                    GroundingScreen(
                        technique = technique,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.popBackStack("technique/${technique.id}", false)
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
                    ProgressiveMuscleRelaxationScreen(
                        technique = technique,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.popBackStack("technique/${technique.id}", false)
                        }
                    )
                }
                "peaceful-visualization" -> {
                    PeacefulVisualizationScreen(
                        technique = technique,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.popBackStack("technique/${technique.id}", false)
                        }
                    )
                }
                "thought-labeling" -> {
                    ThoughtLabelingScreen(
                        technique = technique,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.popBackStack("technique/${technique.id}", false)
                        }
                    )
                }
                "stress-relief-bubbles" -> {
                    StressReliefBubblesScreen(
                        technique = technique,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.popBackStack("technique/${technique.id}", false)
                        }
                    )
                }
                "sound-therapy" -> {
                    SoundTherapyScreen(
                        technique = technique,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.popBackStack("technique/${technique.id}", false)
                        }
                    )
                }
                "stress-ball" -> {
                    StressBallScreen(
                        technique = technique,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.popBackStack("technique/${technique.id}", false)
                        }
                    )
                }
                else -> {
                    ExerciseScreen(
                        technique = technique,
                        onBackClick = {
                            navController.popBackStack()
                        },
                        onComplete = {
                            navController.popBackStack("technique/${technique.id}", false)
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
}