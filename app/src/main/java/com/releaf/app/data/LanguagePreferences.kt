package com.releaf.app.data

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

/**
 * Gestionnaire des préférences de langue avec support du changement à chaud.
 */
class LanguagePreferences(private val context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "language_preferences"
        private const val KEY_LANGUAGE = "selected_language"
        private const val DEFAULT_LANGUAGE = "fr" // Default to French

        /**
         * Liste des langues supportées avec leur code et nom d'affichage
         */
        val SUPPORTED_LANGUAGES = listOf(
            LanguageOption("fr", "Français"),
            LanguageOption("en", "English"),
            LanguageOption("es", "Español"),
            LanguageOption("zh", "中文")
        )

        /**
         * Applique la langue à un Context (à utiliser dans attachBaseContext)
         */
        fun wrapContext(context: Context): Context {
            val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            val languageCode = prefs.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
            return updateContextLocale(context, languageCode)
        }

        private fun updateContextLocale(context: Context, languageCode: String): Context {
            val locale = when (languageCode) {
                "zh" -> Locale.SIMPLIFIED_CHINESE
                else -> Locale(languageCode)
            }
            Locale.setDefault(locale)

            val config = Configuration(context.resources.configuration)
            config.setLocale(locale)
            config.setLayoutDirection(locale)

            return context.createConfigurationContext(config)
        }
    }

    data class LanguageOption(val code: String, val displayName: String)

    /**
     * Définit la langue et redémarre l'activité si nécessaire
     */
    fun setLanguageAndRestart(languageCode: String, activity: Activity) {
        val currentLanguage = getLanguage()
        if (currentLanguage != languageCode) {
            prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
            restartActivity(activity)
        }
    }

    /**
     * Définit la langue sans redémarrer (utile pour les tests)
     */
    fun setLanguage(languageCode: String) {
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
    }

    fun getLanguage(): String {
        return prefs.getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
    }

    fun getCurrentLanguageDisplayName(): String {
        val currentCode = getLanguage()
        return SUPPORTED_LANGUAGES.find { it.code == currentCode }?.displayName
            ?: SUPPORTED_LANGUAGES.first().displayName
    }

    fun getCurrentLanguageOption(): LanguageOption {
        val currentCode = getLanguage()
        return SUPPORTED_LANGUAGES.find { it.code == currentCode }
            ?: SUPPORTED_LANGUAGES.first()
    }

    /**
     * @deprecated Utiliser wrapContext dans attachBaseContext à la place
     */
    @Deprecated("Use wrapContext in attachBaseContext instead", ReplaceWith("LanguagePreferences.wrapContext(context)"))
    fun applyLanguage(context: Context): Context {
        return wrapContext(context)
    }

    fun needsLanguageUpdate(context: Context): Boolean {
        val currentLocale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.resources.configuration.locales.get(0)
        } else {
            @Suppress("DEPRECATION")
            context.resources.configuration.locale
        }
        return currentLocale.language != getLanguage()
    }

    /**
     * Redémarre l'activité pour appliquer la nouvelle langue
     */
    private fun restartActivity(activity: Activity) {
        val intent = activity.intent
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        activity.finish()
        activity.startActivity(intent)
        // Animation de transition douce
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
    }
}