package com.ratjatji.eskhathinitutors

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatDelegate

class SettingsDarkModeFragment : Fragment() {

    private val PREFS_NAME = "settings_prefs"
    private val DARK_MODE_KEY = "dark_mode"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings_dark_mode, container, false)

        val switchDarkMode = view.findViewById<Switch>(R.id.switchDarkMode)
        val textDarkModeStatus = view.findViewById<TextView>(R.id.textDarkModeStatus)

        // Get SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Load the dark mode preference (default is false - off)
        val isDarkModeOn = sharedPreferences.getBoolean(DARK_MODE_KEY, false)

        // Set switch state and text based on the saved preference
        switchDarkMode.isChecked = isDarkModeOn
        textDarkModeStatus.text = if (isDarkModeOn) "On" else "Off"

        // Apply dark mode based on saved preference
        if (isDarkModeOn) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        // Set the listener to toggle dark mode and save the preference
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Enable dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                textDarkModeStatus.text = "On"
                saveDarkModePreference(true)
            } else {
                // Disable dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                textDarkModeStatus.text = "Off"
                saveDarkModePreference(false)
            }
        }

        return view
    }

    // Function to save dark mode preference
    private fun saveDarkModePreference(isDarkModeOn: Boolean) {
        val sharedPreferences = requireActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putBoolean(DARK_MODE_KEY, isDarkModeOn)
            apply()
        }
    }
}
