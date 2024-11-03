package com.ratjatji.eskhathinitutors

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

class SharedPreferences(private val context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

    fun setLocale(languageCode: String) {
        val editor = sharedPreferences.edit()
        editor.putString("My_Lang", languageCode)
        editor.apply()
        updateResources(languageCode)
    }

    private fun updateResources(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
    }

    fun loadLocale(): String? {
        return sharedPreferences.getString("My_Lang", "en")
    }
}