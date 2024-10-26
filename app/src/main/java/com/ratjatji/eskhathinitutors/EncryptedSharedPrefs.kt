package com.ratjatji.eskhathinitutors

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptedSharedPrefs(context: Context) {
    private val masterKeySpec = KeyGenParameterSpec.Builder(
        MasterKey.DEFAULT_MASTER_KEY_ALIAS,
        KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
    )
        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
        .setKeySize(256)
        .build()

    private val masterKey = MasterKey.Builder(context)
        .setKeyGenParameterSpec(masterKeySpec)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "secure_prefs",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun saveCredentials(email: String, password: String) {
        with(sharedPreferences.edit()) {
            putString(KEY_EMAIL, email)
            putString(KEY_PASSWORD, password)
            apply()
        }
    }

    fun getEmail(): String {
        return sharedPreferences.getString(KEY_EMAIL, "") ?: ""
    }

    fun getPassword(): String {
        return sharedPreferences.getString(KEY_PASSWORD, "") ?: ""
    }

    fun clearCredentials() {
        with(sharedPreferences.edit()) {
            remove(KEY_EMAIL)
            remove(KEY_PASSWORD)
            apply()
        }
    }

    companion object {
        private const val KEY_EMAIL = "encrypted_email"
        private const val KEY_PASSWORD = "encrypted_password"
    }
}