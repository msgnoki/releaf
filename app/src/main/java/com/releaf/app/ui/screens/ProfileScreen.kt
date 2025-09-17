package com.releaf.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import android.content.Intent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.PathEffect
import java.text.SimpleDateFormat
import java.util.*
import com.releaf.app.MainActivity
import com.releaf.app.R
import com.releaf.app.data.model.UserProgress
import com.releaf.app.data.model.UserLevel
import com.releaf.app.data.model.Badge
import com.releaf.app.data.model.BadgeRarity
import com.releaf.app.data.LanguagePreferences
import com.releaf.app.data.model.firebase.*
import com.releaf.app.ui.viewmodel.ProfileViewModel
import com.releaf.app.ui.viewmodel.ProfileViewModelFactory
import com.releaf.app.ui.theme.CategoryColors
import androidx.compose.material.icons.outlined.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val languagePreferences = remember { LanguagePreferences(context) }
    var showLanguageDialog by remember { mutableStateOf(false) }
    
    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModelFactory())
    val uiState by profileViewModel.uiState.collectAsState()
    
    // Initialize user data if needed
    LaunchedEffect(Unit) {
        profileViewModel.initializeUserDataIfNeeded()
    }
    
    // Show error message
    val errorMessage = uiState.errorMessage
    if (errorMessage != null) {
        LaunchedEffect(errorMessage) {
            // You could show a snackbar here
            profileViewModel.clearError()
        }
    }
    
    // Convert Firebase data to legacy format for compatibility
    val convertedProgress = uiState.profileData?.let { profileData ->
        profileData.progress?.let { progress ->
            UserProgress(
                userId = progress.userId,
                currentStreak = progress.currentStreak,
                longestStreak = progress.longestStreak,
                totalSessions = progress.totalSessions,
                totalMinutes = progress.totalMinutes,
                sessionsThisWeek = progress.sessionsThisWeek,
                minutesThisWeek = progress.minutesThisWeek,
                sessionsThisMonth = 0, // Not available in new schema
                minutesThisMonth = 0, // Not available in new schema
                lastSessionDate = progress.lastSessionDate,
                averageMoodImprovement = progress.averageMoodImprovement,
                level = getUserLevel(profileData.user?.level ?: 1),
                experiencePoints = profileData.user?.currentXp ?: 0,
                badges = profileData.unlockedBadges.map { it.badgeId }
            )
        }
    }
    
    val convertedBadges = uiState.badgeDefinitions.map { badge ->
        Badge(
            id = badge.badgeId,
            name = badge.name,
            description = badge.description,
            icon = badge.icon ?: "emoji_events",
            category = when (badge.category) {
                "STREAK" -> com.releaf.app.data.model.BadgeCategory.STREAK
                "DURATION" -> com.releaf.app.data.model.BadgeCategory.DURATION
                "FREQUENCY" -> com.releaf.app.data.model.BadgeCategory.FREQUENCY
                "TECHNIQUE_MASTERY" -> com.releaf.app.data.model.BadgeCategory.TECHNIQUE_MASTERY
                "MOOD_IMPROVEMENT" -> com.releaf.app.data.model.BadgeCategory.MOOD_IMPROVEMENT
                else -> com.releaf.app.data.model.BadgeCategory.SPECIAL
            },
            rarity = when (badge.rarity) {
                "COMMON" -> BadgeRarity.COMMON
                "UNCOMMON" -> BadgeRarity.UNCOMMON
                "RARE" -> BadgeRarity.RARE
                "EPIC" -> BadgeRarity.EPIC
                "LEGENDARY" -> BadgeRarity.LEGENDARY
                else -> BadgeRarity.COMMON
            },
            unlockedAt = System.currentTimeMillis()
        )
    }
    
    val nextLevelXP = convertedProgress?.let { progress ->
        UserLevel.values().getOrNull(progress.level.ordinal + 1)?.minXP ?: progress.level.minXP
    } ?: 100
    
    val progressToNextLevel = convertedProgress?.let { progress ->
        if (nextLevelXP > progress.level.minXP) {
            ((progress.experiencePoints - progress.level.minXP).toFloat() / 
             (nextLevelXP - progress.level.minXP)).coerceIn(0f, 1f)
        } else 1f
    } ?: 0f
    
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else if (convertedProgress != null) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // User Level Card
            item {
                UserLevelCard(
                    progress = convertedProgress,
                    progressToNextLevel = progressToNextLevel,
                    nextLevelXP = nextLevelXP,
                    displayName = uiState.profileData?.user?.displayName ?: "Utilisateur"
                )
            }
            
            // Stats Overview
            item {
                StatsOverviewCard(progress = convertedProgress)
            }
            
            // Badges Section
            item {
                BadgesSection(badges = convertedBadges)
            }
            
            // Mood History Graph
            item {
                MoodHistoryCard(profileViewModel = profileViewModel)
            }
            
            // Weekly Progress
            item {
                WeeklyProgressCard(
                    progress = convertedProgress,
                    weeklyProgress = uiState.profileData?.weeklyProgress
                )
            }
            
            // Settings Section (simplified)
            item {
                SettingsSection(
                    onLanguageClick = { showLanguageDialog = true },
                    onLogoutClick = { profileViewModel.signOut() }
                )
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Erreur lors du chargement du profil",
                    style = MaterialTheme.typography.titleMedium
                )
                
                val currentErrorMessage = uiState.errorMessage
                if (currentErrorMessage != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentErrorMessage,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
    
    // Language Selection Dialog
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = languagePreferences.getLanguage(),
            onLanguageSelected = { languageCode ->
                languagePreferences.setLanguage(languageCode)
                profileViewModel.updateLanguage(languageCode)
                showLanguageDialog = false
                // Restart the app to apply the language change
                val activity = context as? androidx.activity.ComponentActivity
                activity?.let {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    context.startActivity(intent)
                    it.finish()
                }
            },
            onDismiss = { showLanguageDialog = false }
        )
    }
}

// Helper function to convert Firebase level to UserLevel enum
private fun getUserLevel(level: Int): UserLevel {
    return when (level) {
        1 -> UserLevel.BEGINNER
        2 -> UserLevel.APPRENTICE
        3 -> UserLevel.PRACTITIONER
        4 -> UserLevel.EXPERT
        5 -> UserLevel.MASTER
        6 -> UserLevel.ZEN_MASTER
        else -> UserLevel.BEGINNER
    }
}

@Composable
private fun UserLevelCard(
    progress: UserProgress,
    progressToNextLevel: Float,
    nextLevelXP: Int,
    displayName: String = "Utilisateur"
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = Color(android.graphics.Color.parseColor(progress.level.color)),
                contentColor = Color.White
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = displayName,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Surface(
                color = Color(android.graphics.Color.parseColor(progress.level.color)).copy(alpha = 0.2f),
                contentColor = Color(android.graphics.Color.parseColor(progress.level.color)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = progress.level.displayName,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // XP Progress
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${progress.experiencePoints} XP",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "$nextLevelXP XP",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                LinearProgressIndicator(
                    progress = { progressToNextLevel },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = Color(android.graphics.Color.parseColor(progress.level.color)),
                    trackColor = Color(android.graphics.Color.parseColor(progress.level.color)).copy(alpha = 0.2f)
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "${nextLevelXP - progress.experiencePoints} XP vers le niveau suivant",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun StatsOverviewCard(progress: UserProgress) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Statistiques",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    icon = Icons.Default.LocalFireDepartment,
                    value = "${progress.currentStreak}",
                    label = "Série actuelle",
                    iconColor = CategoryColors.getCategoryColorByHex("#FF5722")
                )
                
                StatItem(
                    icon = Icons.Default.Schedule,
                    value = "${progress.totalMinutes}",
                    label = "Minutes totales",
                    iconColor = CategoryColors.getCategoryColorByHex("#2196F3")
                )
                
                StatItem(
                    icon = Icons.Default.Refresh,
                    value = "${progress.totalSessions}",
                    label = "Sessions",
                    iconColor = CategoryColors.getCategoryColorByHex("#4CAF50")
                )
                
                StatItem(
                    icon = Icons.Default.SentimentVerySatisfied,
                    value = "${String.format("%.1f", progress.averageMoodImprovement)}",
                    label = "Amélioration",
                    iconColor = CategoryColors.getCategoryColorByHex("#FF9800")
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String,
    iconColor: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun BadgesSection(badges: List<Badge>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Badges",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                
                Text(
                    text = "${badges.size}/20",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(badges) { badge ->
                    BadgeItem(badge = badge)
                }
            }
        }
    }
}

@Composable
private fun BadgeItem(badge: Badge) {
    Surface(
        color = Color(android.graphics.Color.parseColor(badge.rarity.color)).copy(alpha = 0.1f),
        contentColor = Color(android.graphics.Color.parseColor(badge.rarity.color)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .width(80.dp)
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents, // Placeholder icon
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = badge.name,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}

@Composable
private fun WeeklyProgressCard(
    progress: UserProgress,
    weeklyProgress: WeeklyProgress? = null
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Cette semaine",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "${progress.sessionsThisWeek} sessions",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${progress.minutesThisWeek} minutes",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                // Weekly goal indicator
                val weeklyGoalProgress = weeklyProgress?.let { wp ->
                    wp.dailySessions.sum() / 7f
                } ?: (progress.sessionsThisWeek / 7f)
                
                CircularProgressIndicator(
                    progress = { weeklyGoalProgress.coerceAtMost(1f) },
                    modifier = Modifier.size(50.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                )
            }
        }
    }
}

@Composable
private fun SettingsSection(
    onLanguageClick: () -> Unit = {},
    onLogoutClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Paramètres",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Simplified settings - only language and logout as requested
            val settingsItems = listOf(
                Triple(Icons.Default.Language, "Langue", onLanguageClick),
                Triple(Icons.Default.ExitToApp, "Déconnexion", onLogoutClick)
            )
            
            settingsItems.forEach { (icon, title, onClick) ->
                SettingsItem(
                    icon = icon,
                    title = title,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
private fun SettingsItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.weight(1f)
            )
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

@Composable
private fun MoodHistoryCard(profileViewModel: ProfileViewModel) {
    val moodHistory by profileViewModel.moodHistory.collectAsState()
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Évolution de l'humeur",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            if (moodHistory.isEmpty()) {
                Text(
                    text = "Pas encore de données d'humeur disponibles.\nComplétez quelques sessions pour voir votre évolution !",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                // Mood Graph
                MoodGraph(
                    moodHistory = moodHistory,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Average improvement
                val avgImprovement = moodHistory.map { it.improvement }.average()
                Text(
                    text = "Amélioration moyenne: ${String.format("%.1f", avgImprovement)} points",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun MoodGraph(
    moodHistory: List<ProfileViewModel.MoodEntry>,
    modifier: Modifier = Modifier
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant
    
    Canvas(modifier = modifier) {
        if (moodHistory.isEmpty()) return@Canvas
        
        val width = size.width
        val height = size.height
        val padding = 20f
        
        // Drawing area
        val graphWidth = width - 2 * padding
        val graphHeight = height - 2 * padding
        
        // Mood range (1-10)
        val minMood = 1f
        val maxMood = 10f
        val moodRange = maxMood - minMood
        
        // Draw background grid
        for (i in 1..9) {
            val y = padding + (i / 10f) * graphHeight
            drawLine(
                color = surfaceVariant,
                start = Offset(padding, y),
                end = Offset(width - padding, y),
                strokeWidth = 1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(5f, 5f))
            )
        }
        
        if (moodHistory.size < 2) return@Canvas
        
        // Calculate points for before and after mood lines
        val beforePoints = moodHistory.mapIndexed { index, entry ->
            val x = padding + (index.toFloat() / (moodHistory.size - 1)) * graphWidth
            val y = padding + ((maxMood - entry.moodBefore) / moodRange) * graphHeight
            Offset(x, y)
        }
        
        val afterPoints = moodHistory.mapIndexed { index, entry ->
            val x = padding + (index.toFloat() / (moodHistory.size - 1)) * graphWidth
            val y = padding + ((maxMood - entry.moodAfter) / moodRange) * graphHeight
            Offset(x, y)
        }
        
        // Draw mood before line (lighter)
        for (i in 0 until beforePoints.size - 1) {
            drawLine(
                color = primaryColor.copy(alpha = 0.4f),
                start = beforePoints[i],
                end = beforePoints[i + 1],
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
        
        // Draw mood after line (darker)
        for (i in 0 until afterPoints.size - 1) {
            drawLine(
                color = primaryColor,
                start = afterPoints[i],
                end = afterPoints[i + 1],
                strokeWidth = 3f,
                cap = StrokeCap.Round
            )
        }
        
        // Draw points
        beforePoints.forEach { point ->
            drawCircle(
                color = primaryColor.copy(alpha = 0.4f),
                radius = 4f,
                center = point
            )
        }
        
        afterPoints.forEach { point ->
            drawCircle(
                color = primaryColor,
                radius = 4f,
                center = point
            )
        }
    }
}

// Section "Aller plus loin" removed as requested (section 6)

@Composable
private fun LanguageSelectionDialog(
    currentLanguage: String,
    onLanguageSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val languagePreferences = remember { LanguagePreferences(context) }
    
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.language_title),
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                Text(
                    text = stringResource(
                        R.string.language_current,
                        languagePreferences.getCurrentLanguageDisplayName()
                    ),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                // French Option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentLanguage == "fr",
                        onClick = { onLanguageSelected("fr") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.language_french),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                // English Option
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = currentLanguage == "en",
                        onClick = { onLanguageSelected("en") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = stringResource(R.string.language_english),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = stringResource(R.string.language_restart_required),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}