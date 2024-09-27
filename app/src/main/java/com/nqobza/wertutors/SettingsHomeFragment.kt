package com.nqobza.wertutors

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
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_settings_home, container, false)

        // Find buttons by their IDs
        val btnEditProfile: Button = view.findViewById(R.id.btnEditProfile)
        val btnPayment: Button = view.findViewById(R.id.btnPayment)
        val btnSettings: Button = view.findViewById(R.id.btnSettings)

        // Set onClickListeners for the buttons
        btnEditProfile.setOnClickListener {
            val fragment = EditProfileFragment()

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
btnSettings.setOnClickListener {
    val fragment = SettingsDarkModeFragment()
    parentFragmentManager.beginTransaction()
        .replace(R.id.nav_host_fragment, fragment)
        .addToBackStack(null)
        .commit()
}

        return view
    }
}