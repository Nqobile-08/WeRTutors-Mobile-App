package com.nqobza.wertutors

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class TutorExpandedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutor_expanded)

        val tutorProfilePicture: ImageView = findViewById(R.id.ivProfilePic)
        val tutorName: TextView = findViewById(R.id.tvTutorName)
        val tutorRating: TextView = findViewById(R.id.tvRating)
        val tutorRate: TextView = findViewById(R.id.tvRate)
        val tutorLocation: TextView = findViewById(R.id.tvLocation)
        val tutorLanguages: TextView = findViewById(R.id.tvLanguage)
        val tutorDescription: TextView = findViewById(R.id.tvDescription)
        val tutorAbout: TextView = findViewById(R.id.tvAboutTutor)
        val tvLevelsAndSubjects: TextView = findViewById(R.id.tvLevelsAndSubjects)
        val rvReviews: RecyclerView = findViewById(R.id.rvReviews)

        val btnAbout: Button = findViewById(R.id.btnAbout)
        val btnLevelsSubjects: Button = findViewById(R.id.btnLevelsSubjects)
        val btnReviews: Button = findViewById(R.id.btnReviews)

        // Setting up the initial view state
        tutorAbout.visibility = View.VISIBLE
        tvLevelsAndSubjects.visibility = View.GONE
        rvReviews.visibility = View.GONE

        // OnClickListeners for buttons
        btnAbout.setOnClickListener {
            tutorAbout.visibility = View.VISIBLE
            tvLevelsAndSubjects.visibility = View.GONE
            rvReviews.visibility = View.GONE
        }

        btnLevelsSubjects.setOnClickListener {
            tutorAbout.visibility = View.GONE
            tvLevelsAndSubjects.visibility = View.VISIBLE
            rvReviews.visibility = View.GONE
        }

        btnReviews.setOnClickListener {
            tutorAbout.visibility = View.GONE
            tvLevelsAndSubjects.visibility = View.GONE
            rvReviews.visibility = View.VISIBLE
        }

        // Retrieving data from intent
        val bundle: Bundle? = intent.extras
        val name = bundle?.getString("Name")
        val profilePic = bundle?.getInt("ProfilePic")
        val rating = bundle?.getString("Rating")
        val rate = bundle?.getString("Rate")
        val location = bundle?.getString("Location")
        val language = bundle?.getString("Language")
        val description = bundle?.getString("Description")
        val aboutTutor = bundle?.getString("aboutTutor")

        // Setting tutor data
        tutorProfilePicture.setImageResource(profilePic ?: R.drawable.icon)
        tutorName.text = name
        tutorRating.text = rating
        tutorRate.text = rate
        tutorLocation.text = location
        tutorLanguages.text = language
        tutorDescription.text = description
        tutorAbout.text = aboutTutor
    }
}
