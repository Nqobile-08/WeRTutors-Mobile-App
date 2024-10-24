package com.ratjatji.eskhathinitutors.Tutors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

import com.ratjatji.eskhathinitutors.R
import com.ratjatji.eskhathinitutors.LoginActivity

class LogoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logout, container, false)

        // Find the logout button
        val logoutButton: Button = view.findViewById(R.id.btn_logout)

        // Set onClickListener to redirect to LoginActivity
        logoutButton.setOnClickListener {
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Clear task to prevent back navigation
            startActivity(intent)
            activity?.finish() // Close the current activity
        }

        return view
    }
}