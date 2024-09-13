package com.yourpackage.name

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.nqobza.wertutors.R

class ProfileFragment : Fragment() {

    private lateinit var profilePicture: ImageView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var editProfileButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Initialize views
        profilePicture = view.findViewById(R.id.profile_picture)
        profileName = view.findViewById(R.id.profile_name)
        profileEmail = view.findViewById(R.id.profile_email)
        editProfileButton = view.findViewById(R.id.btnBack)

        // Set user data (replace with real data fetching logic)
        loadUserData()

        // Set button click listener to edit profile
        editProfileButton.setOnClickListener {
            // Start Edit Profile activity or navigate to Edit Profile Fragment
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

        return view
    }

    private fun loadUserData() {
        // Example static data, replace with actual user data from Firebase or other source
        profileName.text = "John Doe"
        profileEmail.text = "johndoe@example.com"
        profilePicture.setImageResource(R.drawable.tutors)  // Placeholder image
    }
}
