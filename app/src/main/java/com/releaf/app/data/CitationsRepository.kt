package com.releaf.app.data

import android.content.Context
import com.releaf.app.data.model.Citation
import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlin.random.Random

object CitationsRepository {
    
    private var citations: List<Citation> = emptyList()
    private var isLoaded = false
    
    fun loadCitations(context: Context) {
        if (isLoaded) return
        
        try {
            val json = context.assets.open("citations.json").bufferedReader().use { it.readText() }
            citations = Json.decodeFromString<List<Citation>>(json)
            isLoaded = true
        } catch (e: Exception) {
            // Fallback citations if file loading fails
            citations = getFallbackCitations()
            isLoaded = true
        }
    }
    
    fun getRandomCitation(): Citation {
        return if (citations.isNotEmpty()) {
            citations[Random.nextInt(citations.size)]
        } else {
            Citation(
                citation = "La sérénité vient de l'intérieur. Ne la cherchez pas à l'extérieur.",
                auteur = "Bouddha"
            )
        }
    }
    
    fun getAllCitations(): List<Citation> = citations
    
    private fun getFallbackCitations(): List<Citation> {
        return listOf(
            Citation(
                citation = "Ton esprit prendra la couleur de tes pensées habituelles ; car l'âme se teinte de la couleur de ses pensées.",
                auteur = "Marc Aurèle"
            ),
            Citation(
                citation = "La sérénité vient de l'intérieur. Ne la cherchez pas à l'extérieur.",
                auteur = "Bouddha"
            ),
            Citation(
                citation = "Ne t'abandonne pas à des rêves de ce que tu n'as pas, mais considère les principaux biens que tu possèdes déjà…",
                auteur = "Marc Aurèle"
            ),
            Citation(
                citation = "Nous devons méditer sur ce qui apporte le bonheur…",
                auteur = "Épictète"
            )
        )
    }
}