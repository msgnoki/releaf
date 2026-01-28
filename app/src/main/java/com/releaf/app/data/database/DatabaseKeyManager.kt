package com.releaf.app.data.database

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import java.security.SecureRandom
import java.util.Base64

/**
 * Gestionnaire de clé de chiffrement pour la base de données Room.
 * Utilise Android Keystore et EncryptedSharedPreferences pour stocker
 * la clé de chiffrement de manière sécurisée.
 */
object DatabaseKeyManager {

    private const val PREFS_FILE_NAME = "releaf_secure_prefs"
    private const val KEY_DATABASE_KEY = "database_encryption_key"
    private const val KEY_LENGTH_BYTES = 32 // 256 bits

    /**
     * Obtient ou génère la clé de chiffrement de la base de données.
     * La clé est stockée de manière sécurisée via EncryptedSharedPreferences.
     */
    fun getOrCreateDatabaseKey(context: Context): ByteArray {
        val masterKey = createMasterKey(context)
        val encryptedPrefs = createEncryptedPrefs(context, masterKey)

        // Récupérer la clé existante ou en générer une nouvelle
        val existingKey = encryptedPrefs.getString(KEY_DATABASE_KEY, null)

        return if (existingKey != null) {
            Base64.getDecoder().decode(existingKey)
        } else {
            val newKey = generateSecureKey()
            val encodedKey = Base64.getEncoder().encodeToString(newKey)
            encryptedPrefs.edit().putString(KEY_DATABASE_KEY, encodedKey).apply()
            newKey
        }
    }

    /**
     * Crée la MasterKey pour EncryptedSharedPreferences
     */
    private fun createMasterKey(context: Context): MasterKey {
        val keyGenSpec = KeyGenParameterSpec.Builder(
            MasterKey.DEFAULT_MASTER_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()

        return MasterKey.Builder(context)
            .setKeyGenParameterSpec(keyGenSpec)
            .build()
    }

    /**
     * Crée les EncryptedSharedPreferences
     */
    private fun createEncryptedPrefs(
        context: Context,
        masterKey: MasterKey
    ) = EncryptedSharedPreferences.create(
        context,
        PREFS_FILE_NAME,
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    /**
     * Génère une clé de chiffrement sécurisée
     */
    private fun generateSecureKey(): ByteArray {
        val key = ByteArray(KEY_LENGTH_BYTES)
        SecureRandom().nextBytes(key)
        return key
    }

    /**
     * Convertit la clé en passphrase pour SQLCipher
     */
    fun getPassphrase(context: Context): ByteArray {
        return getOrCreateDatabaseKey(context)
    }
}
