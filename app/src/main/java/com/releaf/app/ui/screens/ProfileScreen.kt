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
import com.releaf.app.MainActivity
import com.releaf.app.R
import com.releaf.app.data.model.UserProgress
import com.releaf.app.data.model.UserLevel
import com.releaf.app.data.model.Badge
import com.releaf.app.data.model.BadgeRarity
import com.releaf.app.data.LanguagePreferences
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
    // Mock user progress for demonstration
    val mockProgress = remember {
        UserProgress(
            userId = "demo-user",
            currentStreak = 7,
            longestStreak = 15,
            totalSessions = 42,
            totalMinutes = 320,
            sessionsThisWeek = 5,
            minutesThisWeek = 45,
            sessionsThisMonth = 18,
            minutesThisMonth = 150,
            lastSessionDate = "2024-01-15",
            averageMoodImprovement = 3.8f,
            level = UserLevel.APPRENTICE,
            experiencePoints = 245,
            badges = listOf("first_session", "streak_3", "streak_7", "total_hour", "mood_booster")
        )
    }
    
    val mockBadges = remember {
        listOf(
            Badge("first_session", "Premier Pas", "Complétez votre première session", "play_circle", 
                  com.releaf.app.data.model.BadgeCategory.STREAK, BadgeRarity.COMMON, System.currentTimeMillis()),
            Badge("streak_3", "Constance", "Maintenez une série de 3 jours", "local_fire_department",
                  com.releaf.app.data.model.BadgeCategory.STREAK, BadgeRarity.UNCOMMON, System.currentTimeMillis()),
            Badge("streak_7", "Semaine Zen", "Série de 7 jours consécutifs", "whatshot",
                  com.releaf.app.data.model.BadgeCategory.STREAK, BadgeRarity.RARE, System.currentTimeMillis()),
            Badge("total_hour", "Une Heure Zen", "Cumulez 60 minutes de pratique", "schedule",
                  com.releaf.app.data.model.BadgeCategory.DURATION, BadgeRarity.COMMON, System.currentTimeMillis()),
            Badge("mood_booster", "Remonteur de Moral", "Améliorez votre humeur de 3+ points", "sentiment_very_satisfied",
                  com.releaf.app.data.model.BadgeCategory.MOOD_IMPROVEMENT, BadgeRarity.COMMON, System.currentTimeMillis())
        )
    }
    
    val nextLevelXP = remember(mockProgress) {
        UserLevel.values().getOrNull(mockProgress.level.ordinal + 1)?.minXP ?: mockProgress.level.minXP
    }
    
    val progressToNextLevel = remember(mockProgress, nextLevelXP) {
        if (nextLevelXP > mockProgress.level.minXP) {
            ((mockProgress.experiencePoints - mockProgress.level.minXP).toFloat() / 
             (nextLevelXP - mockProgress.level.minXP)).coerceIn(0f, 1f)
        } else 1f
    }
    
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // User Level Card
        item {
            UserLevelCard(
                progress = mockProgress,
                progressToNextLevel = progressToNextLevel,
                nextLevelXP = nextLevelXP
            )
        }
        
        // Stats Overview
        item {
            StatsOverviewCard(progress = mockProgress)
        }
        
        // Badges Section
        item {
            BadgesSection(badges = mockBadges)
        }
        
        // Weekly Progress
        item {
            WeeklyProgressCard(progress = mockProgress)
        }
        
        // Aller plus loin Section
        item {
            GoFurtherSection()
        }
        
        // Settings Section
        item {
            SettingsSection(
                onLanguageClick = { showLanguageDialog = true }
            )
        }
    }
    
    // Language Selection Dialog
    if (showLanguageDialog) {
        LanguageSelectionDialog(
            currentLanguage = languagePreferences.getLanguage(),
            onLanguageSelected = { languageCode ->
                languagePreferences.setLanguage(languageCode)
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

@Composable
private fun UserLevelCard(
    progress: UserProgress,
    progressToNextLevel: Float,
    nextLevelXP: Int
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
                text = "Utilisateur Demo",
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
private fun WeeklyProgressCard(progress: UserProgress) {
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
                CircularProgressIndicator(
                    progress = { progress.sessionsThisWeek / 7f },
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
    onLanguageClick: () -> Unit = {}
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
            
            val settingsItems = listOf(
                Triple(Icons.Default.Notifications, "Notifications", {}),
                Triple(Icons.Default.Palette, "Thème", {}),
                Triple(Icons.Default.Language, "Langue", onLanguageClick),
                Triple(Icons.Default.Backup, "Sauvegarde", {}),
                Triple(Icons.Default.Help, "Aide", {}),
                Triple(Icons.Default.ExitToApp, "Déconnexion", {})
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
private fun GoFurtherSection() {
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
                text = "Aller plus loin ?",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // About App Description
            Text(
                text = "Outils gratuits pour vous aider lorsque vous vous sentez anxieux. Ces exercices sont simples à suivre et ne nécessitent aucun équipement particulier, utilisez-les dès que votre esprit est trop agité ou lorsque vous avez besoin d'un moment de calme.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                lineHeight = 20.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // About anxiety section
            Text(
                text = "Comprendre l'anxiété",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // What happens card
                InfoCardProfile(
                    icon = Icons.Default.Warning,
                    title = "Ce qui se passe pendant l'anxiété",
                    description = "Votre corps active la réponse « combat ou fuite », augmentant le rythme cardiaque, accélérant la respiration et libérant des hormones de stress comme l'adrénaline.",
                    backgroundColor = Color(0xFFFEF3C7),
                    iconColor = Color(0xFFD97706),
                    borderColor = Color(0xFFF59E0B)
                )
                
                // How techniques help card
                InfoCardProfile(
                    icon = Icons.Default.CheckCircle,
                    title = "Comment ces techniques aident",
                    description = "Ces techniques fondées sur des données scientifiques activent votre système nerveux parasympathique, qui contrebalance naturellement la réponse au stress.",
                    backgroundColor = Color(0xFFDCFCE7),
                    iconColor = Color(0xFF16A34A),
                    borderColor = Color(0xFF22C55E)
                )
                
                // Tips card
                TipsCardProfile()
            }
        }
    }
}

@Composable
private fun InfoCardProfile(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    backgroundColor: Color,
    iconColor: Color,
    borderColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 6.dp)
            ) {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(6.dp))
                
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
private fun TipsCardProfile() {
    val tips = listOf(
        "Trouvez un endroit calme et confortable",
        "Commencez par des techniques courtes (2-5 min)",
        "Pratiquez régulièrement pour développer vos compétences",
        "Soyez patient avec vous-même"
    )
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color(0xFF60A5FA), RoundedCornerShape(8.dp)),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFDBEAFE))
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Icon(
                    Icons.Default.Lightbulb,
                    contentDescription = null,
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(16.dp)
                )
                
                Spacer(modifier = Modifier.width(6.dp))
                
                Text(
                    text = "Conseils pour de meilleurs résultats",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Column {
                tips.forEach { tip ->
                    Row(
                        modifier = Modifier.padding(vertical = 1.dp)
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(end = 6.dp, top = 1.dp)
                        )
                        
                        Text(
                            text = tip,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 16.sp,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

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