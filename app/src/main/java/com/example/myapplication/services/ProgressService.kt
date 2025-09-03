package com.example.myapplication.services

import com.example.myapplication.data.model.UserProgress
import com.example.myapplication.data.model.Badge
import com.example.myapplication.data.model.BadgeCategory
import com.example.myapplication.data.model.BadgeRarity
import com.example.myapplication.data.model.UserLevel
import com.example.myapplication.data.model.TechniqueCategory
import com.example.myapplication.data.model.DurationPreference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
object ProgressService {
    
    private val _userProgress = MutableStateFlow<UserProgress?>(null)
    val userProgress: Flow<UserProgress?> = _userProgress.asStateFlow()
    
    private val _availableBadges = MutableStateFlow<List<Badge>>(emptyList())
    val availableBadges: Flow<List<Badge>> = _availableBadges.asStateFlow()
    
    init {
        initializeBadges()
    }
    
    private fun initializeBadges() {
        val badges = listOf(
            // Streak badges
            Badge("first_session", "Premier Pas", "Complétez votre première session", "play_circle", BadgeCategory.STREAK, BadgeRarity.COMMON),
            Badge("streak_3", "Constance", "Maintenez une série de 3 jours", "local_fire_department", BadgeCategory.STREAK, BadgeRarity.UNCOMMON),
            Badge("streak_7", "Semaine Zen", "Série de 7 jours consécutifs", "whatshot", BadgeCategory.STREAK, BadgeRarity.RARE),
            Badge("streak_30", "Maître de la Régularité", "30 jours consécutifs", "military_tech", BadgeCategory.STREAK, BadgeRarity.EPIC),
            Badge("streak_100", "Centenaire Zen", "100 jours consécutifs", "emoji_events", BadgeCategory.STREAK, BadgeRarity.LEGENDARY),
            
            // Duration badges
            Badge("total_hour", "Une Heure Zen", "Cumulez 60 minutes de pratique", "schedule", BadgeCategory.DURATION, BadgeRarity.COMMON),
            Badge("total_10_hours", "Dix Heures de Paix", "Cumulez 10 heures de pratique", "access_time", BadgeCategory.DURATION, BadgeRarity.UNCOMMON),
            Badge("total_50_hours", "Maître du Temps", "Cumulez 50 heures de pratique", "timer", BadgeCategory.DURATION, BadgeRarity.RARE),
            Badge("total_100_hours", "Sage Temporel", "Cumulez 100 heures de pratique", "hourglass_full", BadgeCategory.DURATION, BadgeRarity.EPIC),
            
            // Frequency badges
            Badge("weekly_warrior", "Guerrier Hebdomadaire", "5 sessions cette semaine", "fitness_center", BadgeCategory.FREQUENCY, BadgeRarity.UNCOMMON),
            Badge("monthly_master", "Maître Mensuel", "20 sessions ce mois", "calendar_month", BadgeCategory.FREQUENCY, BadgeRarity.RARE),
            Badge("session_50", "Cinquante Sessions", "Complétez 50 sessions au total", "repeat", BadgeCategory.FREQUENCY, BadgeRarity.RARE),
            Badge("session_200", "Bicentenaire", "200 sessions accomplies", "trending_up", BadgeCategory.FREQUENCY, BadgeRarity.EPIC),
            
            // Technique mastery badges
            Badge("breathing_master", "Maître de la Respiration", "Maîtrisez toutes les techniques de respiration", "air", BadgeCategory.TECHNIQUE_MASTERY, BadgeRarity.RARE),
            Badge("relaxation_guru", "Guru de la Relaxation", "Explorez toutes les techniques de relaxation", "self_improvement", BadgeCategory.TECHNIQUE_MASTERY, BadgeRarity.RARE),
            Badge("all_categories", "Explorateur Complet", "Essayez chaque catégorie", "explore", BadgeCategory.TECHNIQUE_MASTERY, BadgeRarity.EPIC),
            
            // Mood improvement badges
            Badge("mood_booster", "Remonteur de Moral", "Améliorez votre humeur de 3+ points", "sentiment_very_satisfied", BadgeCategory.MOOD_IMPROVEMENT, BadgeRarity.COMMON),
            Badge("happiness_master", "Maître du Bonheur", "Moyenne d'amélioration de 4+ points", "mood", BadgeCategory.MOOD_IMPROVEMENT, BadgeRarity.RARE),
            
            // Special badges
            Badge("early_bird", "Lève-tôt", "Session avant 7h du matin", "wb_sunny", BadgeCategory.SPECIAL, BadgeRarity.UNCOMMON),
            Badge("night_owl", "Oiseau de Nuit", "Session après 22h", "bedtime", BadgeCategory.SPECIAL, BadgeRarity.UNCOMMON),
            Badge("crisis_warrior", "Guerrier de Crise", "Utilisez l'aide en cas de crise", "healing", BadgeCategory.SPECIAL, BadgeRarity.RARE),
            Badge("perfectionist", "Perfectionniste", "Complétez une routine entière", "done_all", BadgeCategory.SPECIAL, BadgeRarity.EPIC)
        )
        
        _availableBadges.value = badges
    }
    
    suspend fun initializeUserProgress(userId: String): UserProgress {
        val progress = UserProgress(userId = userId)
        _userProgress.value = progress
        return progress
    }
    
    suspend fun recordSession(
        userId: String,
        techniqueId: String,
        durationMinutes: Int,
        moodBefore: Int,
        moodAfter: Int,
        category: TechniqueCategory
    ): UserProgress {
        val currentProgress = _userProgress.value ?: initializeUserProgress(userId)
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        
        // Calculate streak
        val newStreak = calculateStreak(currentProgress, today)
        
        // Calculate mood improvement
        val moodImprovement = moodAfter - moodBefore
        val newAverageMoodImprovement = if (currentProgress.totalSessions == 0) {
            moodImprovement.toFloat()
        } else {
            ((currentProgress.averageMoodImprovement * currentProgress.totalSessions) + moodImprovement) / 
            (currentProgress.totalSessions + 1)
        }
        
        // Calculate new experience points (1 XP per minute, bonus for streaks)
        val baseXP = durationMinutes
        val streakBonus = when {
            newStreak >= 30 -> 10
            newStreak >= 7 -> 5
            newStreak >= 3 -> 2
            else -> 0
        }
        val sessionXP = baseXP + streakBonus
        val newTotalXP = currentProgress.experiencePoints + sessionXP
        
        // Calculate new level
        val newLevel = calculateLevel(newTotalXP)
        
        // Update weekly/monthly counters
        val currentWeekNumber = getCurrentWeekNumber()
        val currentMonth = getCurrentMonth()
        
        val (newWeeklySessions, newWeeklyMinutes) = if (isNewWeek(currentProgress)) {
            1 to durationMinutes
        } else {
            (currentProgress.sessionsThisWeek + 1) to (currentProgress.minutesThisWeek + durationMinutes)
        }
        
        val (newMonthlySessions, newMonthlyMinutes) = if (isNewMonth(currentProgress)) {
            1 to durationMinutes
        } else {
            (currentProgress.sessionsThisMonth + 1) to (currentProgress.minutesThisMonth + durationMinutes)
        }
        
        val updatedProgress = currentProgress.copy(
            currentStreak = newStreak,
            longestStreak = maxOf(currentProgress.longestStreak, newStreak),
            totalSessions = currentProgress.totalSessions + 1,
            totalMinutes = currentProgress.totalMinutes + durationMinutes,
            sessionsThisWeek = newWeeklySessions,
            minutesThisWeek = newWeeklyMinutes,
            sessionsThisMonth = newMonthlySessions,
            minutesThisMonth = newMonthlyMinutes,
            lastSessionDate = today,
            averageMoodImprovement = newAverageMoodImprovement,
            level = newLevel,
            experiencePoints = newTotalXP,
            badges = checkAndAwardBadges(currentProgress.copy(
                currentStreak = newStreak,
                totalSessions = currentProgress.totalSessions + 1,
                totalMinutes = currentProgress.totalMinutes + durationMinutes,
                sessionsThisWeek = newWeeklySessions,
                sessionsThisMonth = newMonthlySessions,
                averageMoodImprovement = newAverageMoodImprovement
            ), moodImprovement),
            updatedAt = System.currentTimeMillis()
        )
        
        _userProgress.value = updatedProgress
        return updatedProgress
    }
    
    private fun calculateStreak(currentProgress: UserProgress, today: String): Int {
        val lastSessionDate = currentProgress.lastSessionDate
        
        if (lastSessionDate == null) {
            return 1 // First session
        }
        
        val lastDate = LocalDate.parse(lastSessionDate)
        val todayDate = LocalDate.parse(today)
        val daysBetween = ChronoUnit.DAYS.between(lastDate, todayDate)
        
        return when {
            daysBetween == 0L -> currentProgress.currentStreak // Same day
            daysBetween == 1L -> currentProgress.currentStreak + 1 // Next day
            else -> 1 // Streak broken, start new
        }
    }
    
    private fun calculateLevel(experiencePoints: Int): UserLevel {
        return UserLevel.values().lastOrNull { experiencePoints >= it.minXP } ?: UserLevel.BEGINNER
    }
    
    private fun isNewWeek(progress: UserProgress): Boolean {
        // Simple implementation - in real app would check actual week boundaries
        val lastSessionDate = progress.lastSessionDate ?: return true
        val lastDate = LocalDate.parse(lastSessionDate)
        val today = LocalDate.now()
        return ChronoUnit.WEEKS.between(lastDate, today) > 0
    }
    
    private fun isNewMonth(progress: UserProgress): Boolean {
        val lastSessionDate = progress.lastSessionDate ?: return true
        val lastDate = LocalDate.parse(lastSessionDate)
        val today = LocalDate.now()
        return lastDate.monthValue != today.monthValue || lastDate.year != today.year
    }
    
    private fun getCurrentWeekNumber(): Int {
        return LocalDate.now().dayOfYear / 7
    }
    
    private fun getCurrentMonth(): Int {
        return LocalDate.now().monthValue
    }
    
    private fun checkAndAwardBadges(progress: UserProgress, moodImprovement: Int): List<String> {
        val currentBadges = progress.badges.toMutableList()
        val availableBadgesList = _availableBadges.value
        
        availableBadgesList.forEach { badge ->
            if (!currentBadges.contains(badge.id)) {
                val shouldAward = when (badge.id) {
                    "first_session" -> progress.totalSessions >= 1
                    "streak_3" -> progress.currentStreak >= 3
                    "streak_7" -> progress.currentStreak >= 7
                    "streak_30" -> progress.currentStreak >= 30
                    "streak_100" -> progress.currentStreak >= 100
                    "total_hour" -> progress.totalMinutes >= 60
                    "total_10_hours" -> progress.totalMinutes >= 600
                    "total_50_hours" -> progress.totalMinutes >= 3000
                    "total_100_hours" -> progress.totalMinutes >= 6000
                    "weekly_warrior" -> progress.sessionsThisWeek >= 5
                    "monthly_master" -> progress.sessionsThisMonth >= 20
                    "session_50" -> progress.totalSessions >= 50
                    "session_200" -> progress.totalSessions >= 200
                    "mood_booster" -> moodImprovement >= 3
                    "happiness_master" -> progress.averageMoodImprovement >= 4f
                    else -> false
                }
                
                if (shouldAward) {
                    currentBadges.add(badge.id)
                }
            }
        }
        
        return currentBadges
    }
    
    fun getUnlockedBadges(): List<Badge> {
        val progress = _userProgress.value ?: return emptyList()
        val allBadges = _availableBadges.value
        
        return allBadges.filter { badge ->
            progress.badges.contains(badge.id)
        }.map { badge ->
            badge.copy(unlockedAt = System.currentTimeMillis())
        }
    }
    
    fun getNextLevelProgress(): Float {
        val progress = _userProgress.value ?: return 0f
        val currentLevel = progress.level
        val nextLevel = UserLevel.values().getOrNull(currentLevel.ordinal + 1) ?: return 1f
        
        val currentLevelXP = currentLevel.minXP
        val nextLevelXP = nextLevel.minXP
        val playerXP = progress.experiencePoints
        
        return ((playerXP - currentLevelXP).toFloat() / (nextLevelXP - currentLevelXP)).coerceIn(0f, 1f)
    }
    
    fun getXPToNextLevel(): Int {
        val progress = _userProgress.value ?: return 0
        val currentLevel = progress.level
        val nextLevel = UserLevel.values().getOrNull(currentLevel.ordinal + 1) ?: return 0
        
        return maxOf(0, nextLevel.minXP - progress.experiencePoints)
    }
}