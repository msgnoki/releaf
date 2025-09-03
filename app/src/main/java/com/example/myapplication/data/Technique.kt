package com.example.myapplication.data

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

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
    val duration: String
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
            icon = "air",
            iconColor = "cyan",
            tags = listOf("high-anxiety", "short-time"),
            name = "Respiration 2 Minutes",
            shortDescription = "Exercice de respiration simple qui calme votre système nerveux et réduit l'anxiété en quelques minutes",
            description = "Quand l'anxiété frappe, votre respiration devient superficielle et rapide. Cette technique vous enseigne des modèles de respiration simples qui activent la réponse naturelle de relaxation de votre corps.",
            duration = "2-3 min"
        ),
        "grounding" to Technique(
            id = "grounding",
            icon = "anchor",
            iconColor = "green",
            tags = listOf("high-anxiety", "short-time"),
            name = "Ancrage 5-4-3-2-1",
            shortDescription = "Une technique sensorielle qui vous sort des pensées anxieuses en vous concentrant sur ce qui vous entoure maintenant",
            description = "La technique d'ancrage utilise vos cinq sens pour vous ancrer dans le moment présent. En remarquant 5 choses que vous pouvez voir, 4 que vous pouvez toucher, 3 que vous pouvez entendre, 2 que vous pouvez sentir et 1 que vous pouvez goûter, cela interrompt le cycle des pensées anxieuses.",
            duration = "3-5 min"
        ),
        "guided-breathing" to Technique(
            id = "guided-breathing",
            icon = "timer",
            iconColor = "blue",
            tags = listOf("moderate-anxiety", "medium-time"),
            name = "Respiration Guidée",
            shortDescription = "Modèles de respiration étape par étape pour ralentir votre rythme cardiaque et réduire la tension",
            description = "Suivez des exercices de respiration structurés conçus pour réguler votre système nerveux autonome. Ces guides visuels vous aident à maintenir un timing et un rythme appropriés.",
            duration = "5-10 min"
        ),
        "progressive-muscle-relaxation" to Technique(
            id = "progressive-muscle-relaxation",
            icon = "accessibility",
            iconColor = "purple",
            tags = listOf("moderate-anxiety", "medium-time"),
            name = "Relaxation Musculaire Progressive",
            shortDescription = "Libérez la tension physique en contractant et relâchant systématiquement les groupes musculaires",
            description = "L'anxiété crée souvent une tension physique que vous ne remarquez peut-être même pas. Cette technique vous apprend à contracter puis relâcher différents groupes musculaires.",
            duration = "8-12 min"
        ),
        "peaceful-visualization" to Technique(
            id = "peaceful-visualization",
            icon = "landscape",
            iconColor = "green",
            tags = listOf("moderate-anxiety", "medium-time"),
            name = "Visualisation Paisible",
            shortDescription = "Imagerie mentale guidée qui vous transporte vers des environnements apaisants",
            description = "Votre esprit a le pouvoir de créer des expériences vivantes et apaisantes grâce à la visualisation. Cette technique vous guide à travers des scénarios paisibles.",
            duration = "10-12 min"
        ),
        "thought-labeling" to Technique(
            id = "thought-labeling",
            icon = "psychology",
            iconColor = "indigo",
            tags = listOf("moderate-anxiety", "medium-time"),
            name = "Étiquetage des Pensées",
            shortDescription = "Une technique de pleine conscience qui vous aide à reconnaître les pensées anxieuses comme des événements mentaux temporaires",
            description = "Les pensées anxieuses peuvent sembler accablantes quand vous les traitez comme des vérités absolues. Cette pratique de pleine conscience vous apprend à observer vos pensées avec curiosité.",
            duration = "5-15 min"
        ),
        "stress-relief-bubbles" to Technique(
            id = "stress-relief-bubbles",
            icon = "bubble_chart",
            iconColor = "teal",
            tags = listOf("high-anxiety", "short-time"),
            name = "Bulles Anti-Stress",
            shortDescription = "Activité interactive d'éclatement de bulles qui redirige l'énergie anxieuse vers un mouvement ludique",
            description = "Parfois, la meilleure façon de gérer l'anxiété est par une distraction douce et ludique. Cette activité interactive simple aide à rediriger l'énergie nerveuse.",
            duration = "2-5 min"
        ),
        "sound-therapy" to Technique(
            id = "sound-therapy",
            icon = "graphic_eq",
            iconColor = "indigo",
            tags = listOf("moderate-anxiety", "medium-time", "long-time"),
            name = "Thérapie Sonore",
            shortDescription = "Fréquences curatives qui favorisent la relaxation et la clarté mentale",
            description = "Des fréquences sonores spécifiques ont montré leur capacité à influencer les ondes cérébrales et favoriser la relaxation.",
            duration = "5-30 min"
        ),
        "stress-ball" to Technique(
            id = "stress-ball",
            icon = "sports_handball",
            iconColor = "orange",
            tags = listOf("high-anxiety", "short-time"),
            name = "Balle Anti-Stress",
            shortDescription = "Balle anti-stress interactive pour évacuer la tension et rediriger l'énergie anxieuse",
            description = "Parfois, la tension physique a besoin d'une libération physique. Cette balle anti-stress virtuelle offre une expérience tactile satisfaisante.",
            duration = "1-5 min"
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
}