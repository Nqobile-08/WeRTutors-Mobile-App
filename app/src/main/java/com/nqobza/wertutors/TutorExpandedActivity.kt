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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class TutorExpandedActivity : AppCompatActivity() {

    // RecyclerView for Reviews
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var dbRef: DatabaseReference
    private lateinit var reviewList: ArrayList<Review>

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

        //Declaring tutor about
        var tutorWorkHeading: TextView = findViewById(R.id.tvAboutTutorWorkExpHeading)
        var tutorWork: TextView = findViewById(R.id.tvAboutTutorWorkExp)
        var tutorEducationHeading: TextView = findViewById(R.id.tvAboutTutorEducationHeading)
        var tutorEducation: TextView = findViewById(R.id.tvAboutTutorEducation)
        var tutorSkillsHeading: TextView = findViewById(R.id.tvAboutTutorSkillsHeading)
        var tutorSkills: TextView = findViewById(R.id.tvAboutTutorSkills)
        var tutorCoursesHeading: TextView = findViewById(R.id.tvAboutTutorCoursesHeading)
        var tutorCourses: TextView = findViewById(R.id.tvAboutTutorCourses)

        //Declaring tutor levels and subject
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
        val rvReviews: RecyclerView = findViewById(R.id.rvReviews)


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
        tutorLevelHeading.visibility =View.GONE
        tutorLevel.visibility =View.GONE
        tutorSubjectsHeading.visibility =View.GONE
        tutorSubjects.visibility =View.GONE

        rvReviews.visibility = View.GONE

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
            tutorLevelHeading.visibility =View.GONE
            tutorLevel.visibility =View.GONE
            tutorSubjectsHeading.visibility =View.GONE
            tutorSubjects.visibility =View.GONE

            rvReviews.visibility = View.GONE
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
            tutorLevelHeading.visibility =View.VISIBLE
            tutorLevel.visibility =View.VISIBLE
            tutorSubjectsHeading.visibility =View.VISIBLE
            tutorSubjects.visibility =View.VISIBLE

            rvReviews.visibility = View.GONE

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
            tutorLevelHeading.visibility =View.GONE
            tutorLevel.visibility =View.GONE
            tutorSubjectsHeading.visibility =View.GONE
            tutorSubjects.visibility =View.GONE

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
        tutorSkills.text = skills // Set the subjects text
        tutorCourses.text = coursesCertification // Set the subjects text
        tutorEducation.text = education // Set the subjects text
        tutorWork.text = workExp // Set the subjects text
        tutorSubjects.text = subjectsString

        // Setting up RecyclerView for reviews
        reviewRecyclerView = findViewById(R.id.rvReviews)
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewRecyclerView.setHasFixedSize(true)

        reviewList = arrayListOf<Review>()
        getReviewData()

    }

    private fun getReviewData() {
        dbRef = FirebaseDatabase.getInstance().getReference("student_reviews")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    for( reviewSnapshot in snapshot.children){
                        val review = reviewSnapshot.getValue(Review::class.java)
                        reviewList.add(review!!)

                    }
                    reviewRecyclerView.adapter = ReviewAdapter(reviewList)
                }}

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}

