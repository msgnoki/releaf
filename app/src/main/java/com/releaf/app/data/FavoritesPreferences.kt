package com.releaf.app.data

import android.content.Context
import android.content.SharedPreferences

class FavoritesPreferences(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    companion object {
        private const val PREFS_NAME = "favorites_preferences"
        private const val KEY_FAVORITES = "favorite_technique_ids"
    }
    
    fun addFavorite(techniqueId: String) {
        val currentFavorites = getFavoriteIds().toMutableSet()
        currentFavorites.add(techniqueId)
        saveFavorites(currentFavorites)
    }
    
    fun removeFavorite(techniqueId: String) {
        val currentFavorites = getFavoriteIds().toMutableSet()
        currentFavorites.remove(techniqueId)
        saveFavorites(currentFavorites)
    }
    
    fun isFavorite(techniqueId: String): Boolean {
        return getFavoriteIds().contains(techniqueId)
    }
    
    fun getFavoriteIds(): Set<String> {
        val favoritesString = prefs.getString(KEY_FAVORITES, "") ?: ""
        return if (favoritesString.isBlank()) {
            emptySet()
        } else {
            favoritesString.split(",").toSet()
        }
    }
    
    private fun saveFavorites(favorites: Set<String>) {
        val favoritesString = favorites.joinToString(",")
        prefs.edit().putString(KEY_FAVORITES, favoritesString).apply()
    }
    
    fun toggleFavorite(techniqueId: String): Boolean {
        return if (isFavorite(techniqueId)) {
            removeFavorite(techniqueId)
            false
        } else {
            addFavorite(techniqueId)
            true
        }
    }
}