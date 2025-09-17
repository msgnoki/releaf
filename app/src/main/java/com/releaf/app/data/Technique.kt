package com.releaf.app.data

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable
import com.releaf.app.data.model.TechniqueCategory
import com.releaf.app.data.model.DurationPreference
import com.releaf.app.data.model.Recommendation
import com.releaf.app.data.model.RecommendationReason
import com.releaf.app.data.model.RecommendationWithTechnique
import java.util.UUID

@Stable
@Serializable
data class Technique(
    val id: String,
    val icon: String,
    val iconColor: String,
    val tags: List<String>,
    val name: String,
    val shortDescription: String,
    val description: String,
    val duration: String,
    val category: TechniqueCategory,
    val durationMinutesStart: Int,
    val durationMinutesEnd: Int,
    val difficultyLevel: Int = 1, // 1-5 scale
    val popularity: Int = 0,
    val isForCrisis: Boolean = false
)

enum class TechniqueTag(val value: String) {
    HIGH_ANXIETY("high-anxiety"),
    MODERATE_ANXIETY("moderate-anxiety"),
    SHORT_TIME("short-time"),
    MEDIUM_TIME("medium-time"),
    LONG_TIME("long-time")
}

object TechniquesRepository {
    
    private val techniquesData = mapOf(
        "breathing" to Technique(
            id = "breathing",
            icon = "Techniques icons/icon_respiration_2min.png",
            iconColor = "cyan",
            tags = listOf("high-anxiety", "short-time"),
            name = "Respiration 2 Minutes",
            shortDescription = "Exercice de respiration simple qui calme votre système nerveux et réduit l'anxiété en quelques minutes",
            description = "Quand l'anxiété frappe, votre respiration devient superficielle et rapide. Cette technique vous enseigne des modèles de respiration simples qui activent la réponse naturelle de relaxation de votre corps.",
            duration = "2-3 min",
            category = TechniqueCategory.RESPIRATION,
            durationMinutesStart = 2,
            durationMinutesEnd = 3,
            difficultyLevel = 1,
            popularity = 95,
            isForCrisis = true
        ),
        "grounding" to Technique(
            id = "grounding",
            icon = "Techniques icons/icon_ancrage_54321.png",
            iconColor = "green",
            tags = listOf("high-anxiety", "short-time"),
            name = "Ancrage 5-4-3-2-1",
            shortDescription = "Une technique sensorielle qui vous sort des pensées anxieuses en vous concentrant sur ce qui vous entoure maintenant",
            description = "La technique d'ancrage utilise vos cinq sens pour vous ancrer dans le moment présent. En remarquant 5 choses que vous pouvez voir, 4 que vous pouvez toucher, 3 que vous pouvez entendre, 2 que vous pouvez sentir et 1 que vous pouvez goûter, cela interrompt le cycle des pensées anxieuses.",
            duration = "3-5 min",
            category = TechniqueCategory.ANCRAGE,
            durationMinutesStart = 3,
            durationMinutesEnd = 5,
            difficultyLevel = 1,
            popularity = 88,
            isForCrisis = true
        ),
        "guided-breathing" to Technique(
            id = "guided-breathing",
            icon = "Techniques icons/icon_respiration_guidee.png",
            iconColor = "blue",
            tags = listOf("moderate-anxiety", "medium-time"),
            name = "Respiration Guidée",
            shortDescription = "Modèles de respiration étape par étape pour ralentir votre rythme cardiaque et réduire la tension",
            description = "Suivez des exercices de respiration structurés conçus pour réguler votre système nerveux autonome. Ces guides visuels vous aident à maintenir un timing et un rythme appropriés.",
            duration = "5-10 min",
            category = TechniqueCategory.RESPIRATION,
            durationMinutesStart = 5,
            durationMinutesEnd = 10,
            difficultyLevel = 2,
            popularity = 82
        ),
        "progressive-muscle-relaxation" to Technique(
            id = "progressive-muscle-relaxation",
            icon = "Techniques icons/icon_relaxation_musculaire.png",
            iconColor = "purple",
            tags = listOf("moderate-anxiety", "medium-time"),
            name = "Relaxation Musculaire Progressive",
            shortDescription = "Libérez la tension physique en contractant et relâchant systématiquement les groupes musculaires",
            description = "L'anxiété crée souvent une tension physique que vous ne remarquez peut-être même pas. Cette technique vous apprend à contracter puis relâcher différents groupes musculaires.",
            duration = "8-12 min",
            category = TechniqueCategory.RELAXATION,
            durationMinutesStart = 8,
            durationMinutesEnd = 12,
            difficultyLevel = 2,
            popularity = 75
        ),
        "peaceful-visualization" to Technique(
            id = "peaceful-visualization",
            icon = "Techniques icons/icon_visualisation_paisible.png",
            iconColor = "green",
            tags = listOf("moderate-anxiety", "medium-time"),
            name = "Visualisation Paisible",
            shortDescription = "Imagerie mentale guidée qui vous transporte vers des environnements apaisants",
            description = "Votre esprit a le pouvoir de créer des expériences vivantes et apaisantes grâce à la visualisation. Cette technique vous guide à travers des scénarios paisibles.",
            duration = "10-12 min",
            category = TechniqueCategory.VISUALISATION,
            durationMinutesStart = 10,
            durationMinutesEnd = 12,
            difficultyLevel = 3,
            popularity = 70
        ),
        "thought-labeling" to Technique(
            id = "thought-labeling",
            icon = "Techniques icons/icon_etiquetage_pensees.png",
            iconColor = "indigo",
            tags = listOf("moderate-anxiety", "medium-time"),
            name = "Étiquetage des Pensées",
            shortDescription = "Une technique de pleine conscience qui vous aide à reconnaître les pensées anxieuses comme des événements mentaux temporaires",
            description = "Les pensées anxieuses peuvent sembler accablantes quand vous les traitez comme des vérités absolues. Cette pratique de pleine conscience vous apprend à observer vos pensées avec curiosité.",
            duration = "5-15 min",
            category = TechniqueCategory.RELAXATION,
            durationMinutesStart = 5,
            durationMinutesEnd = 15,
            difficultyLevel = 3,
            popularity = 65
        ),
        "stress-relief-bubbles" to Technique(
            id = "stress-relief-bubbles",
            icon = "Techniques icons/icon_bulles_antistress.png",
            iconColor = "teal",
            tags = listOf("high-anxiety", "short-time"),
            name = "Bulles Anti-Stress",
            shortDescription = "Activité interactive d'éclatement de bulles qui redirige l'énergie anxieuse vers un mouvement ludique",
            description = "Parfois, la meilleure façon de gérer l'anxiété est par une distraction douce et ludique. Cette activité interactive simple aide à rediriger l'énergie nerveuse.",
            duration = "2-5 min",
            category = TechniqueCategory.STRESS_RELIEF,
            durationMinutesStart = 2,
            durationMinutesEnd = 5,
            difficultyLevel = 1,
            popularity = 85,
            isForCrisis = true
        ),
        "sound-therapy" to Technique(
            id = "sound-therapy",
            icon = "Techniques icons/icon_therapie_sonore.png",
            iconColor = "indigo",
            tags = listOf("moderate-anxiety", "medium-time", "long-time"),
            name = "Thérapie Sonore",
            shortDescription = "Fréquences curatives qui favorisent la relaxation et la clarté mentale",
            description = "Des fréquences sonores spécifiques ont montré leur capacité à influencer les ondes cérébrales et favoriser la relaxation.",
            duration = "5-30 min",
            category = TechniqueCategory.RELAXATION,
            durationMinutesStart = 5,
            durationMinutesEnd = 30,
            difficultyLevel = 2,
            popularity = 60
        ),
        "stress-ball" to Technique(
            id = "stress-ball",
            icon = "Techniques icons/icon_balle_antistress.png",
            iconColor = "orange",
            tags = listOf("high-anxiety", "short-time"),
            name = "Balle Anti-Stress",
            shortDescription = "Balle anti-stress interactive pour évacuer la tension et rediriger l'énergie anxieuse",
            description = "Parfois, la tension physique a besoin d'une libération physique. Cette balle anti-stress virtuelle offre une expérience tactile satisfaisante.",
            duration = "1-5 min",
            category = TechniqueCategory.STRESS_RELIEF,
            durationMinutesStart = 1,
            durationMinutesEnd = 5,
            difficultyLevel = 1,
            popularity = 78,
            isForCrisis = true
        ),
        "breathing-stress-15" to Technique(
            id = "breathing-stress-15",
            icon = "Techniques icons/icon_respiration_1_5.png",
            iconColor = "teal",
            tags = listOf("moderate-anxiety", "high-anxiety", "medium-time", "beginner", "stress"),
            name = "Respiration anti-stress (1–5)",
            shortDescription = "Technique de respiration avec comptage 1-5 pour retrouver son calme lors d'une montée de stress",
            description = "Respirer profondément en comptant de 1 à 5 active le système parasympathique, favorisant la détente. Le comptage régulier agit comme un métronome interne, réduisant la charge mentale et facilitant l'autorégulation du rythme cardiaque et respiratoire.",
            duration = "5 min",
            category = TechniqueCategory.RESPIRATION,
            durationMinutesStart = 5,
            durationMinutesEnd = 5,
            difficultyLevel = 1,
            popularity = 85,
            isForCrisis = true
        ),
        "respiration-e12" to Technique(
            id = "respiration-e12",
            icon = "Techniques icons/icon_respiration_diaphragmatique.png",
            iconColor = "blue",
            tags = listOf("moderate-anxiety", "high-anxiety", "medium-time", "long-time", "diaphragme", "relaxation", "apprentissage"),
            name = "Respiration diaphragmatique (abdominale)",
            shortDescription = "Technique de respiration profonde avec le diaphragme pour calmer l'esprit et prévenir l'hyperventilation",
            description = "La respiration diaphragmatique sollicite principalement le diaphragme ; elle est naturellement plus lente et profonde, favorise l'activation du système nerveux parasympathique et diminue le risque d'hyperventilation. Particulièrement indiquée pour les personnes anxieuses.",
            duration = "5-10 min",
            category = TechniqueCategory.RESPIRATION,
            durationMinutesStart = 5,
            durationMinutesEnd = 10,
            difficultyLevel = 2,
            popularity = 92,
            isForCrisis = false
        ),
        "autogenic-training" to Technique(
            id = "autogenic-training",
            icon = "Techniques icons/icon_training_autogene.png",
            iconColor = "deep_purple",
            tags = listOf("moderate-anxiety", "high-anxiety", "medium-time", "long-time", "relaxation-profonde", "auto-suggestion"),
            name = "Training autogène",
            shortDescription = "Méthode de relaxation profonde utilisant l'auto-suggestion pour créer des sensations de lourdeur et de chaleur",
            description = "Le training autogène est une technique d'auto-hypnose fondée sur des auto-suggestions. En répétant mentalement des formules comme \"Mon bras droit est lourd\", vous activez le système parasympathique et induisez une relaxation profonde du corps et de l'esprit.",
            duration = "12 min",
            category = TechniqueCategory.RELAXATION,
            durationMinutesStart = 12,
            durationMinutesEnd = 12,
            difficultyLevel = 3,
            popularity = 70,
            isForCrisis = false
        ),
        "breathing-box" to Technique(
            id = "breathing-box",
            icon = "Techniques icons/icon_respiration_carree.png",
            iconColor = "blue",
            tags = listOf("moderate-anxiety", "high-anxiety", "short-time", "stress", "concentration"),
            name = "Respiration carrée",
            shortDescription = "Technique de respiration 4-4-4-4 utilisée par les forces spéciales pour retrouver rapidement son calme",
            description = "La respiration carrée consiste à inspirer 4 secondes, retenir 4 secondes, expirer 4 secondes et retenir 4 secondes. Cette technique militaire active la réponse de relaxation, ralentit le rythme cardiaque et améliore la concentration.",
            duration = "4 min",
            category = TechniqueCategory.RESPIRATION,
            durationMinutesStart = 4,
            durationMinutesEnd = 4,
            difficultyLevel = 2,
            popularity = 88,
            isForCrisis = true
        ),
        "breathing-478" to Technique(
            id = "breathing-478",
            icon = "Techniques icons/icon_breathing_478.png",
            iconColor = "purple",
            tags = listOf("moderate-anxiety", "high-anxiety", "short-time", "stress", "sommeil", "debutant"),
            name = "Respiration 4-7-8",
            shortDescription = "Technique du Dr. Weil pour l'endormissement : inspirez 4s, retenez 7s, expirez 8s",
            description = "La méthode 4-7-8 consiste à inspirer 4 secondes, retenir son souffle 7 secondes puis expirer 8 secondes. Ce rythme ralentit la respiration, stimule le système parasympathique et favorise l'endormissement naturel.",
            duration = "4 min",
            category = TechniqueCategory.RESPIRATION,
            durationMinutesStart = 4,
            durationMinutesEnd = 4,
            difficultyLevel = 1,
            popularity = 90,
            isForCrisis = true
        ),
        "body-scan-meditation" to Technique(
            id = "body-scan-meditation",
            icon = "Techniques icons/icon_body_scan.png",
            iconColor = "purple",
            tags = listOf("moderate-anxiety", "medium-time", "pleine-conscience", "relaxation", "sommeil", "bien-être"),
            name = "Body scan",
            shortDescription = "Méditation de pleine conscience qui parcourt le corps pour développer la conscience corporelle et relâcher les tensions",
            description = "Le body scan consiste à porter son attention successivement sur chaque région du corps. Cette pratique de pleine conscience permet de relâcher les tensions, calmer l'esprit et améliorer la qualité du sommeil.",
            duration = "10 min",
            category = TechniqueCategory.MEDITATION,
            durationMinutesStart = 10,
            durationMinutesEnd = 10,
            difficultyLevel = 2,
            popularity = 75,
            isForCrisis = false
        ),
        "mindful-breathing" to Technique(
            id = "mindful-breathing",
            icon = "Techniques icons/icon_mindful_breathing.png",
            iconColor = "green",
            tags = listOf("moderate-anxiety", "short-time", "pleine-conscience", "concentration", "apaisement"),
            name = "Respiration consciente",
            shortDescription = "Méditation mindfulness qui développe la présence en observant simplement le souffle naturel",
            description = "La respiration consciente invite à observer le souffle tel qu'il est, sans chercher à le modifier. Cette pratique de pleine conscience développe la présence, réduit les ruminations et améliore la concentration.",
            duration = "6 min",
            category = TechniqueCategory.MEDITATION,
            durationMinutesStart = 6,
            durationMinutesEnd = 6,
            difficultyLevel = 1,
            popularity = 80,
            isForCrisis = false
        ),
        "loving-kindness-meditation" to Technique(
            id = "loving-kindness-meditation",
            icon = "Techniques icons/icon_loving_kindness.png",
            iconColor = "pink",
            tags = listOf("moderate-anxiety", "medium-time", "compassion", "bienveillance", "emotion"),
            name = "Méditation bienveillante",
            shortDescription = "Méditation Metta qui cultive la compassion envers soi-même et les autres pour réduire le stress émotionnel",
            description = "La méditation bienveillante consiste à répéter des souhaits positifs en commençant par soi puis en élargissant aux proches et tous les êtres. Cette pratique favorise les émotions positives et réduit la rumination.",
            duration = "10 min",
            category = TechniqueCategory.MEDITATION,
            durationMinutesStart = 10,
            durationMinutesEnd = 10,
            difficultyLevel = 2,
            popularity = 72,
            isForCrisis = false
        ),
        "auto-hypnosis-autogenic" to Technique(
            id = "auto-hypnosis-autogenic",
            icon = "Techniques icons/icon_autogenic_hypnosis.png",
            iconColor = "deep_purple",
            tags = listOf("moderate-anxiety", "long-time", "auto-hypnose", "relaxation-profonde", "avancé", "thérapeutique"),
            name = "Auto-hypnose Autogène",
            shortDescription = "Technique d'induction auto-hypnotique combinant entraînement autogène et suggestions pour une relaxation profonde",
            description = "L'auto-hypnose autogène combine les techniques d'entraînement autogène et d'induction hypnotique pour créer un état de relaxation profonde et contrôlée. Cette technique avancée vous guide à travers des sensations de lourdeur, chaleur et régulation cardiaque.",
            duration = "12-15 min",
            category = TechniqueCategory.RELAXATION,
            durationMinutesStart = 12,
            durationMinutesEnd = 15,
            difficultyLevel = 4,
            popularity = 65
        ),
        "forest-immersion-nature" to Technique(
            id = "forest-immersion-nature",
            icon = "Techniques icons/icon_forest_immersion.png",
            iconColor = "green",
            tags = listOf("moderate-anxiety", "medium-time", "nature", "biophilie", "stress-relief", "immersif"),
            name = "Immersion Forêt",
            shortDescription = "Évasion mentale immersive dans un environnement forestier avec sons 3D et guidage sensoriel",
            description = "Cette technique d'immersion vous transporte mentalement dans une forêt apaisante grâce à une expérience multisensorielle. Vous découvrirez un sentier forestier, une clairière baignée de lumière et un ruisseau cristallin pour une reconnexion complète avec la nature.",
            duration = "8-15 min",
            category = TechniqueCategory.VISUALISATION,
            durationMinutesStart = 8,
            durationMinutesEnd = 15,
            difficultyLevel = 2,
            popularity = 80
        ),
        "meditation-breath-awareness" to Technique(
            id = "meditation-breath-awareness",
            icon = "Techniques icons/icon_breath_awareness.png",
            iconColor = "amber",
            tags = listOf("moderate-anxiety", "medium-time", "pleine-conscience", "méditation", "attention", "débutant"),
            name = "Méditation Conscience du Souffle",
            shortDescription = "Méditation de pleine conscience utilisant la respiration naturelle comme ancre pour développer l'attention présente",
            description = "Cette méditation fondamentale vous apprend à porter attention à votre respiration naturelle pour cultiver la pleine conscience. En observant simplement les sensations de la respiration, vous développez votre capacité d'attention et de présence au moment présent.",
            duration = "5-15 min",
            category = TechniqueCategory.MEDITATION,
            durationMinutesStart = 5,
            durationMinutesEnd = 15,
            difficultyLevel = 1,
            popularity = 85
        )
    )
    
    fun getAllTechniques(): List<Technique> = techniquesData.values.toList()
    
    fun getTechnique(id: String): Technique? = techniquesData[id]
    
    fun getRelatedTechniques(currentId: String, limit: Int = 3): List<Technique> {
        val current = techniquesData[currentId] ?: return emptyList()
        
        val others = techniquesData.values.filter { it.id != currentId }
        
        // Score techniques by tag overlap
        val scored = others.map { technique ->
            val sharedTags = technique.tags.intersect(current.tags.toSet())
            technique to sharedTags.size
        }
        
        // Sort by score and return top techniques
        return scored
            .sortedByDescending { it.second }
            .take(limit)
            .map { it.first }
    }
    
    fun getTechniquesByTag(tag: String): List<Technique> {
        return techniquesData.values.filter { it.tags.contains(tag) }
    }
    
    // New methods for v0.2 recommendations and filtering
    
    fun getTechniquesByCategory(category: TechniqueCategory): List<Technique> {
        return techniquesData.values.filter { it.category == category }
    }
    
    fun getTechniquesByDuration(durationPreference: DurationPreference): List<Technique> {
        return techniquesData.values.filter { technique ->
            technique.durationMinutesStart <= durationPreference.maxMinutes && 
            technique.durationMinutesEnd >= durationPreference.minMinutes
        }
    }
    
    fun getCrisisTechniques(): List<Technique> {
        return techniquesData.values.filter { it.isForCrisis }.sortedByDescending { it.popularity }
    }
    
    fun getTechniquesByDifficulty(maxLevel: Int): List<Technique> {
        return techniquesData.values.filter { it.difficultyLevel <= maxLevel }
    }
    
    fun getMostPopularTechniques(limit: Int = 3): List<Technique> {
        return techniquesData.values.sortedByDescending { it.popularity }.take(limit)
    }
    
    fun getBeginnerFriendlyTechniques(): List<Technique> {
        return getTechniquesByDifficulty(2).sortedByDescending { it.popularity }
    }
    
    fun generateRecommendations(
        userId: String,
        recentTechniqueIds: List<String> = emptyList(),
        favoriteCategory: TechniqueCategory? = null,
        durationPreference: DurationPreference = DurationPreference.MEDIUM,
        experienceLevel: Int = 1,
        isInCrisis: Boolean = false
    ): List<RecommendationWithTechnique> {
        
        if (isInCrisis) {
            return getCrisisTechniques().take(3).map { technique ->
                RecommendationWithTechnique(
                    recommendation = Recommendation(
                        id = UUID.randomUUID().toString(),
                        userId = userId,
                        techniqueId = technique.id,
                        reason = RecommendationReason.ANXIETY_LEVEL,
                        reasonText = "Idéal pour une crise d'anxiété",
                        score = technique.popularity.toFloat(),
                        category = technique.category,
                        validUntil = System.currentTimeMillis() + 24 * 60 * 60 * 1000 // 24h
                    ),
                    techniqueId = technique.id,
                    techniqueName = technique.name,
                    duration = technique.duration,
                    shortDescription = technique.shortDescription,
                    icon = technique.icon,
                    iconColor = technique.iconColor
                )
            }
        }
        
        val recommendations = mutableListOf<RecommendationWithTechnique>()
        
        // Recent pattern recommendation
        if (recentTechniqueIds.isNotEmpty()) {
            val recentCategories = recentTechniqueIds.mapNotNull { getTechnique(it)?.category }
            val mostUsedCategory = recentCategories.groupingBy { it }.eachCount().maxByOrNull { it.value }?.key
            
            mostUsedCategory?.let { category ->
                getTechniquesByCategory(category)
                    .filter { !recentTechniqueIds.contains(it.id) }
                    .sortedByDescending { it.popularity }
                    .firstOrNull()?.let { technique ->
                        recommendations.add(
                            RecommendationWithTechnique(
                                recommendation = Recommendation(
                                    id = UUID.randomUUID().toString(),
                                    userId = userId,
                                    techniqueId = technique.id,
                                    reason = RecommendationReason.RECENT_PATTERN,
                                    reasonText = "Basé sur votre usage récent",
                                    score = technique.popularity.toFloat() * 1.2f,
                                    category = technique.category,
                                    validUntil = System.currentTimeMillis() + 24 * 60 * 60 * 1000
                                ),
                                techniqueId = technique.id,
                                techniqueName = technique.name,
                                duration = technique.duration,
                                shortDescription = technique.shortDescription,
                                icon = technique.icon,
                                iconColor = technique.iconColor
                            )
                        )
                    }
            }
        }
        
        // Duration preference recommendation
        getTechniquesByDuration(durationPreference)
            .filter { !recentTechniqueIds.contains(it.id) }
            .filter { !recommendations.any { rec -> rec.techniqueId == it.id } }
            .sortedByDescending { it.popularity }
            .firstOrNull()?.let { technique ->
                recommendations.add(
                    RecommendationWithTechnique(
                        recommendation = Recommendation(
                            id = UUID.randomUUID().toString(),
                            userId = userId,
                            techniqueId = technique.id,
                            reason = RecommendationReason.DURATION_PREFERENCE,
                            reasonText = "Correspond à vos durées préférées",
                            score = technique.popularity.toFloat() * 1.1f,
                            category = technique.category,
                            validUntil = System.currentTimeMillis() + 24 * 60 * 60 * 1000
                        ),
                        techniqueId = technique.id,
                        techniqueName = technique.name,
                        duration = technique.duration,
                        shortDescription = technique.shortDescription,
                        icon = technique.icon,
                        iconColor = technique.iconColor
                    )
                )
            }
        
        // Popular technique recommendation
        if (recommendations.size < 3) {
            getMostPopularTechniques()
                .filter { !recentTechniqueIds.contains(it.id) }
                .filter { !recommendations.any { rec -> rec.techniqueId == it.id } }
                .take(3 - recommendations.size)
                .forEach { technique ->
                    recommendations.add(
                        RecommendationWithTechnique(
                            recommendation = Recommendation(
                                id = UUID.randomUUID().toString(),
                                userId = userId,
                                techniqueId = technique.id,
                                reason = RecommendationReason.MOST_USED,
                                reasonText = "Populaire auprès des utilisateurs",
                                score = technique.popularity.toFloat(),
                                category = technique.category,
                                validUntil = System.currentTimeMillis() + 24 * 60 * 60 * 1000
                            ),
                            techniqueId = technique.id,
                            techniqueName = technique.name,
                            duration = technique.duration,
                            shortDescription = technique.shortDescription,
                            icon = technique.icon,
                            iconColor = technique.iconColor
                        )
                    )
                }
        }
        
        return recommendations.sortedByDescending { it.recommendation.score }.take(3)
    }
    
    fun getAllCategories(): List<TechniqueCategory> {
        return techniquesData.values.map { it.category }.distinct().sortedBy { it.displayName }
    }
}