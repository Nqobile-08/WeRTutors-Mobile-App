package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment


class SettingsHomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "Settings"
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings_home, container, false)

        // Find buttons by their IDs
        val btnEditProfile: Button = view.findViewById(R.id.btnEditProfile)
        val btnLogout: Button = view.findViewById(R.id.btnLogout)
        val btnSettings: Button = view.findViewById(R.id.btnSettings)
        val btnPayment: Button = view.findViewById(R.id.btnPayment)

        // Set onClickListeners for the buttons
        btnEditProfile.setOnClickListener {
            val fragment = ProfileFragment()

            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }

        btnLogout.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear task to prevent back navigation
            startActivity(intent)
            activity?.finish() // Close the current activity
        }
        btnSettings.setOnClickListener {
            val fragment = SettingsDarkModeFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
        btnPayment.setOnClickListener {
            val fragment = PaymentFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit()
        }
        return view
    }
}