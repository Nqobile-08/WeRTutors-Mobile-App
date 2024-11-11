package com.ratjatji.eskhathinitutors

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import java.util.Locale

class LanguageSelectionFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_change_language, container, false)

        val englishButton: Button = view.findViewById(R.id.englishButton)
        val afrikaansButton: Button = view.findViewById(R.id.afrikaansButton)
        val swatiButton: Button = view.findViewById(R.id.swatiButton)

        englishButton.setOnClickListener { setLocale("en") }
        afrikaansButton.setOnClickListener { setLocale("af") }
        swatiButton.setOnClickListener { setLocale("ss") }

        return view
    }

    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        saveLanguagePreference(languageCode)

        // Restart the activity to apply the new language
        activity?.recreate()
    }

    private fun saveLanguagePreference(languageCode: String) {
        val sharedPreferences: SharedPreferences = requireActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("LANGUAGE_CODE", languageCode)
        editor.apply()
    }
}