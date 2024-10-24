package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class LogoutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_logout, container, false)

        // Find the logout button
        val logoutButton: Button = view.findViewById(R.id.btn_logout)

        // Set onClickListener to logout and redirect to LoginActivity
        logoutButton.setOnClickListener {
            // Sign out the user from Firebase
            FirebaseAuth.getInstance().signOut()

            // Redirect to LoginActivity and clear the back stack
            val intent = Intent(activity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // Close the current activity
            activity?.finish()
        }

        return view
    }
}
