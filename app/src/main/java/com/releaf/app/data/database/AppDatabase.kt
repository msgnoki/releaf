package com.releaf.app.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import com.releaf.app.data.user.*
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [
        User::class,
        UserPreferences::class,
        Session::class,
        TechniqueStats::class,
        UserGoal::class
    ],
    version = 1,
    exportSchema = true // Activé pour les migrations futures
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun sessionDao(): SessionDao
    abstract fun techniqueStatsDao(): TechniqueStatsDao
    abstract fun userGoalDao(): UserGoalDao

    companion object {
        private const val DATABASE_NAME = "releaf_database"
        const val DATABASE_VERSION = 1

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Liste de toutes les migrations supportées.
         * À mettre à jour lors de chaque changement de schéma.
         */
        private val ALL_MIGRATIONS = arrayOf<Migration>(
            // Les migrations seront ajoutées ici au fur et à mesure
            // MIGRATION_1_2,
            // MIGRATION_2_3,
        )

        /**
         * Obtient l'instance de la base de données chiffrée.
         * Utilise SQLCipher pour le chiffrement AES-256.
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = createEncryptedDatabase(context)
                INSTANCE = instance
                instance
            }
        }

        /**
         * Crée la base de données avec chiffrement SQLCipher
         */
        private fun createEncryptedDatabase(context: Context): AppDatabase {
            // Obtenir la passphrase de chiffrement
            val passphrase = DatabaseKeyManager.getPassphrase(context)
            val factory = SupportFactory(passphrase)

            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .openHelperFactory(factory)
                .addMigrations(*ALL_MIGRATIONS)
                .addCallback(DatabaseCallback())
                // En développement uniquement: fallbackToDestructiveMigration()
                // permet de recréer la base si une migration manque.
                // À RETIRER en production!
                .fallbackToDestructiveMigrationOnDowngrade()
                .build()
        }

        /**
         * Callback pour les opérations de base de données
         */
        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Initialisation après création de la base
            }

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                // Configuration à l'ouverture
            }
        }

        // =====================================================================
        // MIGRATIONS
        // =====================================================================
        // Chaque migration doit être ajoutée à ALL_MIGRATIONS ci-dessus.
        // Template pour les futures migrations:
        //
        // val MIGRATION_1_2 = object : Migration(1, 2) {
        //     override fun migrate(database: SupportSQLiteDatabase) {
        //         // Exemple: ajouter une colonne
        //         // database.execSQL("ALTER TABLE users ADD COLUMN avatar_url TEXT")
        //
        //         // Exemple: créer une nouvelle table
        //         // database.execSQL("""
        //         //     CREATE TABLE IF NOT EXISTS new_table (
        //         //         id TEXT PRIMARY KEY NOT NULL,
        //         //         name TEXT NOT NULL,
        //         //         created_at INTEGER NOT NULL
        //         //     )
        //         // """)
        //
        //         // Exemple: ajouter un index
        //         // database.execSQL("CREATE INDEX IF NOT EXISTS idx_sessions_user_id ON sessions(userId)")
        //     }
        // }
        //
        // val MIGRATION_2_3 = object : Migration(2, 3) {
        //     override fun migrate(database: SupportSQLiteDatabase) {
        //         // ...
        //     }
        // }
        // =====================================================================
    }
}