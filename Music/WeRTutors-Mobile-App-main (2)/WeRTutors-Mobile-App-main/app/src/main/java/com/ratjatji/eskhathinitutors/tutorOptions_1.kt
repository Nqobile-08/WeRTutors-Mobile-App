package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Locale.filter


class tutorOptions_1 : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var etSearchBar: EditText
    private lateinit var tutorList: ArrayList<Tutors1>
    private lateinit var filteredTutorList: ArrayList<Tutors1>
    private lateinit var adapter: TutorAdapter
    private lateinit var database: DatabaseReference

    //Tutor profile declarations
    lateinit var imageId: Array<Int>
    lateinit var name: Array<String>
    lateinit var rating: Array<Double>
    lateinit var rate: Array<Double>
    lateinit var location: Array<String>
    lateinit var languages: Array<String>
    lateinit var description: Array<String>
    lateinit var workExperience: Array<String>
    lateinit var education: Array<String>
    lateinit var coursesCertifications: Array<String>
    lateinit var skills: Array<String>
    lateinit var reviewTutorName: Array<String>
    lateinit var reviewStudentName: Array<String>
    lateinit var reviewRating: Array<Int>
    lateinit var reviewSubject: Array<String>
    lateinit var reviewDescription: Array<String>
    lateinit var subjects: Array<String>
    lateinit var levels: Array<String>
    lateinit var sessionTypes: Array<String>
    private lateinit var icFilter: ImageView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tutor_options_1, container, false)
        requireActivity().title = "View tutors"
        database = FirebaseDatabase.getInstance().reference.child("student_reviews")

        recyclerView = view.findViewById(R.id.rvTutors)
        etSearchBar = view.findViewById(R.id.etSearchBar)

        imageId = arrayOf(
            R.drawable.si,
            R.drawable.icon,
            R.drawable.ka,
            R.drawable.le,
            R.drawable.icon,
            R.drawable.le,
            R.drawable.th,
            R.drawable.bi,
            R.drawable.ch
        )

        name = arrayOf(
            "Sipho Mthethwa",
            "Zanele Mhlongo",
            "Kabelo Phiri",
            "Lerato Mokwoena",
            "Sizwe Nkosi",
            "Mbali Mkhize",
            "Ayanda Ndlovu",
            "Bianca Moodley",
            "Christina Goncalves"
        )

        rating = arrayOf(3.9, 4.7, 4.3, 4.5, 4.9, 3.8, 4.2, 3.6, 4.95)

        rate = arrayOf(100.0, 200.0, 150.0, 250.0, 80.0, 120.0, 90.0, 300.0, 350.0)

        location = arrayOf(
            "Soweto, JHB", "Durban, KZN", "Polokwane, LP", "Pretoria, GP",
            "Durban, KZN", "Midrand, JHB", "Cape Town, WC", "Stellenbosch, WC", "Randburg, JHB"
        )

        languages = arrayOf(
            "English, Xhosa", "English, Portuguese", "English, Sotho",
            "English, Sotho", "English, Mandarin", "English, Xhosa", "English, Afrikaans",
            "English, Chinese", "English, Zulu"
        )

        description = arrayOf(
            "High school math teacher with a focus on advanced calculus and algebra.",
            "Expert in business and economics with extensive tutoring experience at university level.",
            "Skilled computer programming tutor with expertise in Python, Java, and C++. Has worked on various software projects.",
            "Data science professional specializing in machine learning and data analysis with hands-on experience in industry.",
            "Experienced mathematics tutor with a strong background in physics and mechanical engineering principles.",
            "Dedicated language tutor with a passion for teaching English and Mandarin through interactive methods.",
            "Enthusiastic tutor in mathematics and physical science with a unique ability to simplify complex concepts.",
            "History and geography expert, providing comprehensive insights into global cultures and historical events.",
            "Computer engineering graduate specializing in hardware systems, offering advanced tutoring for university students."
        )

        subjects = arrayOf(
            "Algebra, Trigonometry, Geometry, Calculus",
            "Business Management, Economics, Statistics",
            "Python, C++, Java, Algorithms",
            "Data Science, Machine Learning, Statistics, Big Data",
            "Mathematics, Physics, Engineering Mechanics",
            "English, Mandarin, Communication Skills",
            "Mathematics, Physical Science, Biology",
            "History, Geography, Civics",
            "Biology, Environmental Science, Ecology"
        )

        workExperience = arrayOf(
            "I am a high school math teacher with over 8 years of experience, focusing on advanced mathematics and exam preparation.",
            "I have 7 years of experience in tutoring business management and economics, specializing in university-level students.",
            "As a programming tutor with 6 years of experience, I focus on algorithms and software development, with real-world project experience.",
            "With a Master's in Data Science, I have 5 years of industry experience in machine learning, providing students with practical insights.",
            "I have over 10 years of experience in tutoring mathematics and physics, helping students excel in their understanding of core concepts.",
            "I specialize in teaching English and Mandarin, drawing on 5 years of experience in interactive learning methods.",
            "I have tutored mathematics and physical science for over 4 years, focusing on foundational concepts for high school students.",
            "With over 5 years of tutoring experience in history and geography, I strive to make lessons engaging and informative.",
            "I am an expert in ecology and environmental science, with 7 years of tutoring experience in practical and theoretical approaches."
        )

        education = arrayOf(
            "Bachelor's degree in Education, specializing in Mathematics from the University of Johannesburg.",
            "Bachelor of Commerce in Economics from Durban University of Technology.",
            "Bachelor of Science in Computer Science from the University of Limpopo.",
            "Master's degree in Data Science from the University of Pretoria.",
            "Bachelor's degree in Mechanical Engineering from Durban University of Technology.",
            "Bachelor's degree in Linguistics from the University of KwaZulu-Natal.",
            "Bachelor's degree in Mathematics and Science from the University of Cape Town.",
            "Master's in History from Stellenbosch University.",
            "Bachelor of Science in Environmental Science from the University of the Witwatersrand."
        )

        coursesCertifications = arrayOf(
            "Certified in Advanced Mathematical Pedagogy by SACE. Completed courses on Data Analysis from Coursera.",
            "Certification in Financial Analysis from Udemy and certified Economics Tutor by TutorABC.",
            "Certified Java Developer by Oracle University and completed courses in Data Structures and Algorithms.",
            "Data Science Specialization on Coursera (Johns Hopkins University) and certified in Machine Learning by Stanford University.",
            "Certified in Fluid Mechanics by edX and completed a course in Applied Mathematics from MIT OpenCourseWare.",
            "Certified in Teaching English as a Foreign Language (TEFL) and completed a course in Mandarin teaching.",
            "Certification in Advanced Mathematics and completed a course in Science Teaching Methods.",
            "Certified in Historical Research Methods and completed a course in Geography Education from Oxford Online.",
            "Certified in Climate Change Studies from Coursera and completed courses in Biodiversity Conservation."
        )

        levels = arrayOf(
            "Secondary school, Tertiary school",
            "Tertiary school",
            "Tertiary school, Professional ",
            "Tertiary school, Professional ",
            "Secondary school, Tertiary school",
            "Primary school, Secondary school",
            "Secondary school",
            "Secondary school, Tertiary school",
            "Tertiary school, Professional "
        )

        skills = arrayOf(
            "Curriculum development, personalized learning strategies, advanced calculus and algebra.",
            "Financial modeling, micro and macroeconomics, business management.",
            "Object-oriented programming, algorithm optimization, project management.",
            "Machine learning algorithms, data visualization, statistical analysis.",
            "Mechanical design, physics tutoring, engineering problem-solving.",
            "Language acquisition strategies, intercultural communication, lesson planning.",
            "Critical thinking, problem-solving techniques, foundational mathematics.",
            "Historical analysis, geographic systems, curriculum design.",
            "Ecological studies, environmental policy, research methodologies."
        )
        sessionTypes = arrayOf(
            "In-person session",
            "Virtual session"
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        tutorList = arrayListOf()
        filteredTutorList = arrayListOf()

        getUserData()

        etSearchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                filter(s.toString())
            }
        })
        return view
    }
    private fun fetchTutorRating(tutorName: String, callback: (Double,Int) -> Unit) {
        database.child(tutorName).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var totalRating = 0.0
                var numberOfReviews = 0

                for (reviewSnapshot in snapshot.children) {
                    val rating = reviewSnapshot.child("rating").getValue(Int::class.java) ?: 0
                    totalRating += rating
                    numberOfReviews++
                }

                val averageRating = if (numberOfReviews > 0) {
                    (totalRating / numberOfReviews).round(2)
                } else {
                    0.0
                }

                callback(averageRating, numberOfReviews)
            }

            override fun onCancelled(error: DatabaseError) {
                callback(0.0, 0)
            }
        })
    }

    private fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }

    private fun getUserData() {
        var completedTutors = 0
        val totalTutors = name.size

        for (i in imageId.indices) {
            fetchTutorRating(name[i]) { averageRating, reviewCount ->
                val tutor = Tutors1(
                    imageId[i],
                    name[i],
                    averageRating, // Use the calculated average rating from Firebase
                    rate[i],
                    location[i],
                    languages[i],
                    description[i],
                    subjects[i],
                    workExperience[i],
                    education[i],
                    coursesCertifications[i],
                    skills[i],
                    levels[i],
                    reviewCount
                )
                tutorList.add(tutor)
                completedTutors++

                // Only set up the adapter once all tutors are loaded
                if (completedTutors == totalTutors) {
                    // Sort tutors by rating (optional)
                    tutorList.sortByDescending { it.Rating }
                    filteredTutorList.clear()
                    filteredTutorList.addAll(tutorList)

                    adapter = TutorAdapter(filteredTutorList)
                    recyclerView.adapter = adapter
                    setupAdapterClickListener()
                }
            }
        }
    }

    private fun setupAdapterClickListener() {
        adapter.setOnItemClickListener(object : TutorAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(context, TutorExpandedActivity::class.java)
                val selectedTutor = filteredTutorList[position]

                intent.putExtra("TUTOR_NAME", selectedTutor.Name)
                intent.putExtra("ProfilePic", selectedTutor.ProfilePic)
                intent.putExtra("Name", selectedTutor.Name)
                intent.putExtra("Rating", selectedTutor.Rating.toString())
                intent.putExtra("Rate", selectedTutor.Rate.toString())
                intent.putExtra("Location", selectedTutor.Location)
                intent.putExtra("Language", selectedTutor.Language)
                intent.putExtra("Description", selectedTutor.Description)
                intent.putExtra("WorkExperience", selectedTutor.WorkExperience)
                intent.putExtra("Education", selectedTutor.Education)
                intent.putExtra("CoursesCertifications", selectedTutor.CoursesCertifications)
                intent.putExtra("Skills", selectedTutor.Skills)
                intent.putExtra("Subjects", selectedTutor.Subjects)
                intent.putExtra("Levels", selectedTutor.Levels)
                intent.putExtra("RatingCount", selectedTutor.RatingCount)

                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }
    // Filter method to search through tutors
    private fun filter(query: String) {
        filteredTutorList.clear()

        if (query.isEmpty()) {
            filteredTutorList.addAll(tutorList)
        } else {
            val searchQuery = query.lowercase()
            tutorList.filterTo(filteredTutorList) { tutor ->
                tutor.Name.lowercase().contains(searchQuery) ||
                        tutor.Location.lowercase().contains(searchQuery) ||
                        tutor.Language.lowercase().contains(searchQuery) ||
                        tutor.Description.lowercase().contains(searchQuery) ||
                        tutor.Subjects.lowercase().contains(searchQuery) ||
                        tutor.Levels.lowercase().contains(searchQuery)
            }
        }

        adapter.notifyDataSetChanged()
    }

}
//REFERENCES - tutorOptions
//https://www.youtube.com/watch?v=WSp8DYQxjEA - firebase reading and writting
//https://www.youtube.com/watch?v=dB9JOsVx-yY - tutors profiles are expanded when clicked  part 2
//https://www.youtube.com/watch?v=UbP8E6I91NA - all tutors displayed in the recyclerview part 1
//https://www.youtube.com/watch?v=ZK9_qMxpqvk - search filter
//https://stackoverflow.com/questions/55433399/i-want-to-get-the-average-my-firebase-data - dynamically update rating average and count