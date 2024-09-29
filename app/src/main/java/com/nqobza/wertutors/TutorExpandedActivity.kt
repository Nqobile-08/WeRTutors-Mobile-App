package com.nqobza.wertutors

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

private const val TAG = "TutorExpandedActivity"

class TutorExpandedActivity : AppCompatActivity() {

    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_tutor_expanded)

        // Declaring tutor's profile
        val tutorProfilePicture: ImageView = findViewById(R.id.ivProfilePic)
        val tutorName: TextView = findViewById(R.id.tvTutorName)
        val tutorRating: TextView = findViewById(R.id.tvRating)
        val tutorRate: TextView = findViewById(R.id.tvRate)
        val tutorLocation: TextView = findViewById(R.id.tvLocation)
        val tutorLanguages: TextView = findViewById(R.id.tvLanguage)
        val tutorDescription: TextView = findViewById(R.id.tvDescription)

        // Declaring tutor about
        val tutorWorkHeading: TextView = findViewById(R.id.tvAboutTutorWorkExpHeading)
        val tutorWork: TextView = findViewById(R.id.tvAboutTutorWorkExp)
        val tutorEducationHeading: TextView = findViewById(R.id.tvAboutTutorEducationHeading)
        val tutorEducation: TextView = findViewById(R.id.tvAboutTutorEducation)
        val tutorSkillsHeading: TextView = findViewById(R.id.tvAboutTutorSkillsHeading)
        val tutorSkills: TextView = findViewById(R.id.tvAboutTutorSkills)
        val tutorCoursesHeading: TextView = findViewById(R.id.tvAboutTutorCoursesHeading)
        val tutorCourses: TextView = findViewById(R.id.tvAboutTutorCourses)

        // Declaring tutor levels and subject
        val tutorLevelHeading: TextView = findViewById(R.id.tvLevelsAndSubjects1)
        val tutorLevel: TextView = findViewById(R.id.tvLevels)
        val tutorSubjectsHeading: TextView = findViewById(R.id.tvLevelsAndSubjects2)
        val tutorSubjects: TextView = findViewById(R.id.tvSubjects)

        // Declaring buttons in linear layout
        val btnAbout: Button = findViewById(R.id.btnAbout)
        val btnLevelsSubjects: Button = findViewById(R.id.btnLevelsSubjects)
        val btnReviews: Button = findViewById(R.id.btnReviews)

        // Corresponding views
        val tvLevelsAndSubjects: TextView = findViewById(R.id.tvLevelsAndSubjects1)
        reviewRecyclerView = findViewById(R.id.rvReviews)

        // Setting up the initial view state
        tutorCoursesHeading.visibility = View.VISIBLE
        tutorCourses.visibility = View.VISIBLE
        tutorSkillsHeading.visibility = View.VISIBLE
        tutorSkills.visibility = View.VISIBLE
        tutorEducationHeading.visibility = View.VISIBLE
        tutorEducation.visibility = View.VISIBLE
        tutorWorkHeading.visibility = View.VISIBLE
        tutorWork.visibility = View.VISIBLE

        tvLevelsAndSubjects.visibility = View.GONE
        tutorLevelHeading.visibility = View.GONE
        tutorLevel.visibility = View.GONE
        tutorSubjectsHeading.visibility = View.GONE
        tutorSubjects.visibility = View.GONE

        reviewRecyclerView.visibility = View.GONE

        // OnClickListeners for buttons
        btnAbout.setOnClickListener {
            tutorCoursesHeading.visibility = View.VISIBLE
            tutorCourses.visibility = View.VISIBLE
            tutorSkillsHeading.visibility = View.VISIBLE
            tutorSkills.visibility = View.VISIBLE
            tutorEducationHeading.visibility = View.VISIBLE
            tutorEducation.visibility = View.VISIBLE
            tutorWorkHeading.visibility = View.VISIBLE
            tutorWork.visibility = View.VISIBLE

            tvLevelsAndSubjects.visibility = View.GONE
            tutorLevelHeading.visibility = View.GONE
            tutorLevel.visibility = View.GONE
            tutorSubjectsHeading.visibility = View.GONE
            tutorSubjects.visibility = View.GONE

            reviewRecyclerView.visibility = View.GONE
        }

        btnLevelsSubjects.setOnClickListener {
            tutorCoursesHeading.visibility = View.GONE
            tutorCourses.visibility = View.GONE
            tutorSkillsHeading.visibility = View.GONE
            tutorSkills.visibility = View.GONE
            tutorEducationHeading.visibility = View.GONE
            tutorEducation.visibility = View.GONE
            tutorWorkHeading.visibility = View.GONE
            tutorWork.visibility = View.GONE

            tvLevelsAndSubjects.visibility = View.VISIBLE
            tutorLevelHeading.visibility = View.VISIBLE
            tutorLevel.visibility = View.VISIBLE
            tutorSubjectsHeading.visibility = View.VISIBLE
            tutorSubjects.visibility = View.VISIBLE

            reviewRecyclerView.visibility = View.GONE
        }

        btnReviews.setOnClickListener {
            tutorCoursesHeading.visibility = View.GONE
            tutorCourses.visibility = View.GONE
            tutorSkillsHeading.visibility = View.GONE
            tutorSkills.visibility = View.GONE
            tutorEducationHeading.visibility = View.GONE
            tutorEducation.visibility = View.GONE
            tutorWorkHeading.visibility = View.GONE
            tutorWork.visibility = View.GONE

            tvLevelsAndSubjects.visibility = View.GONE
            tutorLevelHeading.visibility = View.GONE
            tutorLevel.visibility = View.GONE
            tutorSubjectsHeading.visibility = View.GONE
            tutorSubjects.visibility = View.GONE

            reviewRecyclerView.visibility = View.VISIBLE
        }

        // Retrieving data from intent
        val bundle: Bundle? = intent.extras
        val name = bundle?.getString("TUTOR_NAME")
        val profilePic = bundle?.getInt("ProfilePic")
        val rating = bundle?.getString("Rating")
        val rate = bundle?.getString("Rate")
        val location = bundle?.getString("Location")
        val language = bundle?.getString("Language")
        val description = bundle?.getString("Description")
        val workExp = bundle?.getString("WorkExperience")
        val education = bundle?.getString("Education")
        val coursesCertification = bundle?.getString("CoursesCertifications")
        val skills = bundle?.getString("Skills")
        val subjectsArray = bundle?.getStringArray("Subjects") // Get subjects array
        val subjectsString = subjectsArray?.joinToString(", ") ?: "Subjects not available" // Convert to string

        // Setting tutor data
        tutorProfilePicture.setImageResource(profilePic ?: R.drawable.icon)
        tutorName.text = name
        tutorRating.text = rating
        tutorRate.text = "R $rate/hour"
        tutorLocation.text = location
        tutorLanguages.text = language
        tutorDescription.text = description
        tutorSkills.text = skills
        tutorCourses.text = coursesCertification
        tutorEducation.text = education
        tutorWork.text = workExp
        tutorSubjects.text = subjectsString

        Log.d(TAG, "Activity created")

        // Setting up RecyclerView for reviews
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load reviews for the specific tutor
        if (name != null) {
            loadReviewsFromFirebase(name)
        }
    }

    private fun loadReviewsFromFirebase(tutorName: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("student_reviews")

        // Fetch reviews for a specific tutor
        databaseRef.child(tutorName)  // Use the tutor's name passed in the intent
            .get().addOnSuccessListener { snapshot ->
                val reviews = mutableListOf<Review>()
                for (reviewSnapshot in snapshot.children) {
                    val review = reviewSnapshot.getValue(Review::class.java)
                    if (review != null) {
                        reviews.add(review)
                    }
                }

                // Pass reviews to the adapter
                reviewAdapter = ReviewAdapter(reviews)
                reviewRecyclerView.adapter = reviewAdapter
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load reviews", Toast.LENGTH_SHORT).show()
            }
    }
}
