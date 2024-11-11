package com.ratjatji.eskhathinitutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SettingsHomeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_settings_home, container, false)
        requireActivity().title = "Account settings"
        recyclerView = view.findViewById(R.id.rvSettings)

        val settingsItems = listOf(
            SettingsItem(
                R.drawable.icon,
                "Edit profile",
                "Edit your account details",
                ProfileFragment::class.java
            ),
            SettingsItem(
                R.drawable.ic_language,
                "Change language",
                "Change the application's language",
                LanguageSelectionFragment::class.java
            ),
            SettingsItem(
                R.drawable.ic_dark_mode,
                "Dark mode",
                "Enable or disable dark mode",
                SettingsDarkModeFragment::class.java
            ),
            SettingsItem(
                R.drawable.ic_card,
                "Billing",
                "Add, edit or remove card information",
                PaymentFragment::class.java
            ),
            SettingsItem(
                R.drawable.logout,
                "Log out",
                "Log out of your account",
                null // No fragment, handle logout using Intent
            )
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SettingsAdapter(settingsItems, requireActivity())

        return view
    }
}