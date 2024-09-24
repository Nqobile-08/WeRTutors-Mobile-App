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

class TutorExpandedActivity : AppCompatActivity() {

    // RecyclerView for Reviews
    private lateinit var recyclerView: RecyclerView
    private lateinit var reviewList: ArrayList<Review>
    private lateinit var adapter: ReviewAdapter
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
        recyclerView = findViewById(R.id.rvReviews)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Initialize the review list and load reviews for the selected tutor
        reviewList = ArrayList()
        loadReviews(name ?: "")
        adapter = ReviewAdapter(reviewList)
        recyclerView.adapter = adapter
    }

    private fun loadReviews(tutorName: String) {
        val allReviews = arrayOf(
            // Reviews for Sipho Mthethwa
            Review("Sipho Mthethwa", 5, "Lungelo M.", "Great planning", "Very structured and easy to follow."),
            Review("Sipho Mthethwa", 4, "Nolwazi Z.", "Helpful sessions", "Helped me understand concepts better."),
            Review("Sipho Mthethwa", 5, "Bongani T.", "Clear guidance", "Simplified difficult concepts very well."),

            // Reviews for Zanele Mhlongo
            Review("Zanele Mhlongo", 5, "Thabo K.", "Amazing tutor", "Very patient and knowledgeable."),
            Review("Zanele Mhlongo", 4, "Phindiwe D.", "Clear explanations", "Explained things in a simple way."),
            Review("Zanele Mhlongo", 3, "Nomvula R.", "Not very engaging", "Could be more interactive."),
            Review("Zanele Mhlongo", 5, "Sam L.", "Patient and understanding", "Really took the time to explain things."),

            // Reviews for Kabelo Phiri
            Review("Kabelo Phiri", 3, "John P.", "Good at programming", "Assisted me with coding tasks."),
            Review("Kabelo Phiri", 5, "Matthew V.", "Helpful with algorithms", "Really knows his programming."),
            Review("Kabelo Phiri", 4, "Nandi S.", "Strong with logic", "Great tutor for programming logic."),
            Review("Kabelo Phiri", 2, "Lebo M.", "Confusing at times", "Could have explained more clearly."),
            Review("Kabelo Phiri", 5, "Sam G.", "Knowledgeable", "Explained everything with precision."),

            // Reviews for Lerato Mokoena
            Review("Lerato Mokoena", 5, "Angela R.", "Creative writing expert", "Great insights on writing."),
            Review("Lerato Mokoena", 4, "Jane K.", "Helpful feedback", "Helped me improve my writing style."),
            Review("Lerato Mokoena", 5, "Thomas Z.", "Excellent feedback", "Gave clear advice on my creative pieces."),
            Review("Lerato Mokoena", 3, "David N.", "Could improve", "Sometimes her feedback was hard to implement."),
            Review("Lerato Mokoena", 5, "Chris B.", "Writing master", "She really helped me shape my writing career."),

            // Reviews for Sizwe Nkosi
            Review("Sizwe Nkosi", 5, "Michael S.", "Data science skills", "Very knowledgeable in data science."),
            Review("Sizwe Nkosi", 4, "Kevin R.", "Well-prepared", "Had well-structured lessons."),
            Review("Sizwe Nkosi", 5, "Zinhle N.", "Expert tutor", "Taught me things beyond the syllabus."),
            Review("Sizwe Nkosi", 3, "Lerato S.", "Good but busy", "Sessions were often cut short due to his schedule."),
            Review("Sizwe Nkosi", 2, "Andy T.", "Rushed sessions", "Felt like he was hurrying through the material."),

            // Reviews for Mbali Mkhize
            Review("Mbali Mkhize", 4, "Sarah N.", "Passion for languages", "Helped me improve my Xhosa."),
            Review("Mbali Mkhize", 5, "Grace L.", "Very encouraging", "Made learning a new language fun."),
            Review("Mbali Mkhize", 3, "Lindiwe P.", "Good but slow", "Good tutor, but sometimes slow to respond."),
            Review("Mbali Mkhize", 4, "Peter O.", "Great teacher", "Very understanding and patient."),
            Review("Mbali Mkhize", 2, "Candice T.", "Lacked structure", "I found her lessons disorganized at times."),

            // Reviews for Ayanda Ndlovu
            Review("Ayanda Ndlovu", 4, "Nkosi Z.", "Great math skills", "Explains math concepts really well."),
            Review("Ayanda Ndlovu", 3, "Lucas F.", "A bit technical", "Sometimes went into too much detail."),
            Review("Ayanda Ndlovu", 5, "Mary J.", "Fantastic tutor", "Helped me ace my math exams."),
            Review("Ayanda Ndlovu", 4, "Thandi N.", "Good with numbers", "Very good with breaking down difficult problems."),
            Review("Ayanda Ndlovu", 2, "Tebogo M.", "Rushed lessons", "Felt like we were moving too fast for my pace."),

            // Reviews for Bianca Moodley
            Review("Bianca Moodley", 5, "Eve A.", "History expert", "Helped with my history assignments."),
            Review("Bianca Moodley", 4, "Jenna M.", "Well-organized", "Very organized and knew her content well."),
            Review("Bianca Moodley", 3, "Philip G.", "Good tutor", "Helpful, but could improve time management."),
            Review("Bianca Moodley", 5, "Simphiwe S.", "Engaging lessons", "Made history really interesting."),
            Review("Bianca Moodley", 2, "Lesego N.", "Not for me", "Felt like I didn't connect with her teaching style."),

            // Reviews for Christina Goncalves
            Review("Christina Goncalves", 5, "David G.", "Biology genius", "Very thorough in biology concepts."),
            Review("Christina Goncalves", 4, "Ivy B.", "Great teacher", "Helped me understand complex biology topics."),
            Review("Christina Goncalves", 3, "James L.", "Helpful but slow", "Took time to explain things but sometimes too slow."),
            Review("Christina Goncalves", 5, "Linda N.", "Excellent", "Best tutor I've ever had for biology."),
            Review("Christina Goncalves", 2, "Brian T.", "Not engaging", "Felt like the lessons were a bit boring.")
        )

        reviewList.addAll(allReviews.filter { it.tutorName == tutorName })

    }
}
