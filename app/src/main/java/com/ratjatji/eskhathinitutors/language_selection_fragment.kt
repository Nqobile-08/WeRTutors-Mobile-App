package com.ratjatji.eskhathinitutors

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.ratjatji.eskhathinitutors.Tutors.MainActivity
import java.util.Locale

class LanguageSelectionFragment : Fragment() {

    // Declare buttons
    private lateinit var btnEnglish: Button
    private lateinit var btnZulu: Button
    private lateinit var btnSwati: Button
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        sharedPreferences = SharedPreferences(requireContext())
        sharedPreferences.loadLocale()?.let { sharedPreferences.setLocale(it) }
        return inflater.inflate(R.layout.fragment_language_selection_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Step 1: Initialize buttons
        btnEnglish = view.findViewById(R.id.btnEnglish)
        btnZulu = view.findViewById(R.id.btnZulu)
        btnSwati = view.findViewById(R.id.btnSwati)

        // Step 2: Set up button click for English
        btnEnglish.setOnClickListener {
            sharedPreferences.setLocale("en") // Set language to English
            restartApp() // Restart to apply language change
        }

        // Step 3: Set up button click for Zulu
        btnZulu.setOnClickListener {
            setAppLocale(requireContext(), "zu") // Set language to Zulu
            restartApp() // Restart to apply language change
        }

        // Step 4: Set up button click for Swati
        btnSwati.setOnClickListener {
            setAppLocale(requireContext(), "ss") // Set language to Swati
            restartApp() // Restart to apply language change
        }
    }

    // Method to change the app's locale
    private fun setAppLocale(context: Context, languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = context.resources.configuration
        config.setLocale(locale)
        context.createConfigurationContext(config)
    }

    // Step 5: Restart the app to apply language changes
    private fun restartApp() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()
        // Close the current activity to fully restart
    }
}