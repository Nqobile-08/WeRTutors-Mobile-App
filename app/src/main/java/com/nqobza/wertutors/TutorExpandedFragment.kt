package com.nqobza.wertutors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
class TutorExpandedFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_tutor_expanded, container, false)

        // Initialize the views
//        val tutorProfilePicture: ImageView = view.findViewById(R.id.ivProfilePic)
//        val tutorName: TextView = view.findViewById(R.id.tvTutorName)
//        val tutorRating: TextView = view.findViewById(R.id.tvRating)
//        val tutorRate: TextView = view.findViewById(R.id.tvRate)
//        val tutorLocation: TextView = view.findViewById(R.id.tvLocation)
//        val tutorLanguages: TextView = view.findViewById(R.id.tvLanguage)
//        val tutorDescription: TextView = view.findViewById(R.id.tvDescription)
//        val tutorAbout: TextView = view.findViewById(R.id.tvAboutTutor)
//
//        val bundle : Bundle?= intent.extras
//
//        val profilePic = bundle?.getInt("ProfilePic")
//        val name = bundle!!.getString("Name")
//        val rating = bundle.getString("Rating")
//        val rate = bundle.getString("Rate")
//        val location = bundle.getString("Location")
//        val language = bundle.getString("Language")
//        val description = bundle.getString("Description")
//        val aboutTutor = bundle.getString("aboutTutor")
//
//
//        tutorProfilePicture.setImageResource(profilePic)
//        tutorName.text = name
//        tutorRating.text = rating
//        tutorRate.text = rate
//        tutorLocation.text = location
//        tutorLanguages.text = language
//        tutorDescription.text = description
//        tutorAbout.text = aboutTutor
//
//        // Get the data from the bundle (passed via setArguments)
//        val bundle = arguments
//        if (bundle != null) {
//            val profilePic = bundle.getInt("ProfilePic")
//            val name = bundle.getString("Name")
//            val rating = bundle.getString("Rating")
//            val rate = bundle.getString("Rate")
//            val location = bundle.getString("Location")
//            val language = bundle.getString("Language")
//            val description = bundle.getString("Description")
//            val aboutTutor = bundle.getString("aboutTutor")
//
//            // Set data to views
//            tutorProfilePicture.setImageResource(profilePic)
//            tutorName.text = name
//            tutorRating.text = rating
//            tutorRate.text = rate
//            tutorLocation.text = location
//            tutorLanguages.text = language
//            tutorDescription.text = description
//            tutorAbout.text = aboutTutor
//        }

        return view
    }
}