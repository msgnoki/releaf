package com.releaf.app

import android.app.Application
import com.google.firebase.FirebaseApp
import com.releaf.app.data.database.AppDatabase
import com.releaf.app.data.LanguagePreferences

class ReleafApplication : Application() {
    
    val database by lazy { 
        AppDatabase.getDatabase(this) 
    }
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        
        // Apply language preference at application level
        val languagePreferences = LanguagePreferences(this)
        languagePreferences.applyLanguage(this)
    }
}