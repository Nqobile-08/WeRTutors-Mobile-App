package com.ratjatji.eskhathinitutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

class SettingsDarkModeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings_dark_mode, container, false)
        NotificationHelper.createNotificationChannels(requireContext())
        val switchDarkMode = view.findViewById<Switch>(R.id.switchDarkMode)
        val textDarkModeStatus = view.findViewById<TextView>(R.id.textDarkModeStatus)

        // Check current night mode setting and set the switch state accordingly
        val currentNightMode = AppCompatDelegate.getDefaultNightMode()
        if (currentNightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            switchDarkMode.isChecked = true
            NotificationHelper.sendNotification(requireContext(), "Dark Mode", "Dark Mode", "Dark Mode is enabled")
            //clear the notification


            textDarkModeStatus.text = "On"
        } else {
            switchDarkMode.isChecked = false
            NotificationHelper.sendNotification(requireContext(), "Dark Mode Disabled", "Light Mode", "Light Mode is enabled")
            textDarkModeStatus.text = "Off"
        }

        // Set the listener to toggle dark mode on switch toggle
        switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Enable dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                textDarkModeStatus.text = "On"
            } else {
                // Disable dark mode
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                textDarkModeStatus.text = "Off"
            }
        }

        return view
    }
}