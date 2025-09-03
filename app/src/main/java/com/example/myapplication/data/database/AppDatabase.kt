package com.example.myapplication.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.example.myapplication.data.user.*

@Database(
    entities = [
        User::class,
        UserPreferences::class,
        Session::class,
        TechniqueStats::class,
        UserGoal::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun techniqueStatsDao(): TechniqueStatsDao
    abstract fun userGoalDao(): UserGoalDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "brythee_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}