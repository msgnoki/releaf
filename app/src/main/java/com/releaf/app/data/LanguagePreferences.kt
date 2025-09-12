package com.releaf.app.data

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

class LanguagePreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "language_preferences"
        private const val KEY_LANGUAGE = "selected_language"
        private const val DEFAULT_LANGUAGE = "fr" // Default to French
    }
    
    fun setLanguage(languageCode: String) {
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
    }
    
    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }
    
    fun getCurrentLanguageDisplayName(): String {
        return when (getLanguage()) {
            "en" -> "English"
            "fr" -> "Français"
            else -> "Français"
        }
    }
    
    fun applyLanguage(context: Context) {
        val languageCode = getLanguage()
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
    }
}