package com.ratjatji.eskhathinitutors

import android.app.Fragment
import android.content.ContentValues.TAG
import android.content.Intent

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class tutorOptions_1 : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var tutorList: ArrayList<Tutors1>

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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView started")
        val view = inflater.inflate(R.layout.tutor_options_1, container, false)

        recyclerView = view.findViewById(R.id.rvTutors)

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
            "Lerato Mokoena",
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
            "Secondary, Tertiary",
            "Tertiary",
            "Tertiary, Professional",
            "Tertiary, Professional",
            "Secondary, Tertiary",
            "Primary, Secondary",
            "Secondary",
            "Secondary, Tertiary",
            "Tertiary, Professional"
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

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        tutorList = arrayListOf()
        getUserData(subjects)

        Log.d(TAG, "onCreateView completed")
        return view
    }

    private fun getUserData(subjects: Array<String>) {
        Log.d(TAG, "getUserData started")
        for (i in imageId.indices) {
            val tutor = Tutors1(
                imageId[i], name[i], rating[i], rate[i], location[i], languages[i], description[i], subjects[i],
                workExperience[i], education[i], coursesCertifications[i], skills[i], levels[i]
            )
            tutorList.add(tutor)
            Log.d(TAG, "Added tutor: ${name[i]}")
        }

        // Adapter setup
        val adapter = TutorAdapter(tutorList)
        recyclerView.adapter = adapter

        // Set item click listener to expand the tutor's profile that was clicked
        adapter.setOnItemClickListener(object : TutorAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                Log.d(TAG, "Tutor clicked: ${tutorList[position].Name}")
                val intent = Intent(context, TutorExpandedActivity::class.java)
                intent.putExtra("TUTOR_NAME", tutorList[position].Name)
                intent.putExtra("ProfilePic", tutorList[position].ProfilePic)
                intent.putExtra("Name", tutorList[position].Name)
                intent.putExtra("Rating", tutorList[position].Rating.toString())
                intent.putExtra("Rate", tutorList[position].Rate.toString())
                intent.putExtra("Location", tutorList[position].Location)
                intent.putExtra("Language", tutorList[position].Language)
                intent.putExtra("Description", tutorList[position].Description)
                intent.putExtra("WorkExperience", tutorList[position].WorkExperience)
                intent.putExtra("Education", tutorList[position].Education)
                intent.putExtra("CoursesCertifications", tutorList[position].CoursesCertifications)
                intent.putExtra("Skills", tutorList[position].Skills)
                intent.putExtra("Subjects", tutorList[position].Subjects)
                intent.putExtra("Levels", tutorList[position].Levels)

                Log.d(TAG, "Intent extras added. Starting TutorExpandedActivity")
                try {
                    startActivity(intent)
                } catch (e: Exception) {
                    Log.e(TAG, "Error starting TutorExpandedActivity", e)
                }
            }
        })
        Log.d(TAG, "getUserData completed")
    }
}