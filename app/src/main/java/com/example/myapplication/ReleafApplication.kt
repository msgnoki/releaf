package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.database.AppDatabase

class ReleafApplication : Application() {
    
    val database by lazy { 
        AppDatabase.getDatabase(this) 
    }
    
    override fun onCreate() {
        super.onCreate()
    }
}