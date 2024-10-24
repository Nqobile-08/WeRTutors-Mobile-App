package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView



class showTutorFragment : Fragment() {

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
        val view = inflater.inflate(R.layout.fragment_show_tutor, container, false)

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

// Initialize names of tutors
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

// Initialize ratings for each tutor
        rating = arrayOf(
            3.9, 4.7, 4.3, 4.5, 4.9, 3.8, 4.2, 3.6, 4.95
        )

// Initialize hourly rates for each tutor
        rate = arrayOf(
            100.0, 200.0, 150.0, 250.0, 80.0, 120.0, 90.0, 300.0, 350.0
        )

// Initialize locations of tutors
        location = arrayOf(
            "Soweto, JHB", "Durban, KZN", "Polokwane, LP", "Pretoria, GP",
            "Durban, KZN", "Midrand, JHB", "Cape Town, WC", "Stellenbosch, WC", "Randburg, JHB"
        )

// Initialize languages spoken by each tutor
        languages = arrayOf(
            "English, Xhosa", "English, Portuguese", "English, Sotho",
            "English, Sotho", "English, Mandarin", "English, Xhosa", "English, Afrikaans",
            "English, Chinese", "English, Zulu"
        )

// Initialize descriptions for each tutor
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

// Array of subjects for each tutor
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

// Work Experience array
        workExperience = arrayOf(
            "I am a high school math teacher with over 8 years of experience, focusing on advanced mathematics and exam preparation.", // Sipho
            "I have 7 years of experience in tutoring business management and economics, specializing in university-level students.", // Zanele
            "As a programming tutor with 6 years of experience, I focus on algorithms and software development, with real-world project experience.", // Kabelo
            "With a Master's in Data Science, I have 5 years of industry experience in machine learning, providing students with practical insights.", // Lerato
            "I have over 10 years of experience in tutoring mathematics and physics, helping students excel in their understanding of core concepts.", // Sizwe
            "I specialize in teaching English and Mandarin, drawing on 5 years of experience in interactive learning methods.", // Mbali
            "I have tutored mathematics and physical science for over 4 years, focusing on foundational concepts for high school students.", // Ayanda
            "With over 5 years of tutoring experience in history and geography, I strive to make lessons engaging and informative.", // Bianca
            "I am an expert in ecology and environmental science, with 7 years of tutoring experience in practical and theoretical approaches." // Christina
        )

// Education array
        education = arrayOf(
            "Bachelor’s degree in Education, specializing in Mathematics from the University of Johannesburg.", // Sipho
            "Bachelor of Commerce in Economics from Durban University of Technology.", // Zanele
            "Bachelor of Science in Computer Science from the University of Limpopo.", // Kabelo
            "Master’s degree in Data Science from the University of Pretoria.", // Lerato
            "Bachelor’s degree in Mechanical Engineering from Durban University of Technology.", // Sizwe
            "Bachelor’s degree in Linguistics from the University of KwaZulu-Natal.", // Mbali
            "Bachelor’s degree in Mathematics and Science from the University of Cape Town.", // Ayanda
            "Master’s in History from Stellenbosch University.", // Bianca
            "Bachelor of Science in Environmental Science from the University of the Witwatersrand." // Christina
        )

// Courses & Certifications array
        coursesCertifications = arrayOf(
            "Certified in Advanced Mathematical Pedagogy by SACE. Completed courses on Data Analysis from Coursera.", // Sipho
            "Certification in Financial Analysis from Udemy and certified Economics Tutor by TutorABC.", // Zanele
            "Certified Java Developer by Oracle University and completed courses in Data Structures and Algorithms.", // Kabelo
            "Data Science Specialization on Coursera (Johns Hopkins University) and certified in Machine Learning by Stanford University.", // Lerato
            "Certified in Fluid Mechanics by edX and completed a course in Applied Mathematics from MIT OpenCourseWare.", // Sizwe
            "Certified in Teaching English as a Foreign Language (TEFL) and completed a course in Mandarin teaching.", // Mbali
            "Certification in Advanced Mathematics and completed a course in Science Teaching Methods.", // Ayanda
            "Certified in Historical Research Methods and completed a course in Geography Education from Oxford Online.", // Bianca
            "Certified in Climate Change Studies from Coursera and completed courses in Biodiversity Conservation." // Christina
        )
// Initialize levels for each tutor
        levels = arrayOf(
            "Secondary, Tertiary",    // Sipho Mthethwa: High school math teacher, advanced calculus and algebra.
            "Tertiary",               // Zanele Mhlongo: Expert in business and economics, tutoring at university level.
            "Tertiary, Professional", // Kabelo Phiri: Skilled in programming (Python, Java, C++), worked on software projects.
            "Tertiary, Professional", // Lerato Mokoena: Data science professional, specializing in machine learning and data analysis.
            "Secondary, Tertiary",    // Sizwe Nkosi: Experienced in mathematics, physics, and mechanical engineering principles.
            "Primary, Secondary",     // Mbali Mkhize: Language tutor (English, Mandarin), interactive methods.
            "Secondary",              // Ayanda Ndlovu: Mathematics and physical science tutor, simplifies complex concepts.
            "Secondary, Tertiary",    // Bianca Moodley: History and geography expert, global cultures and historical events.
            "Tertiary, Professional"  // Christina Goncalves: Computer engineering graduate specializing in hardware systems.
        )

// Skills array
        skills = arrayOf(
            "Curriculum development, personalized learning strategies, advanced calculus and algebra.", // Sipho
            "Financial modeling, micro and macroeconomics, business management.", // Zanele
            "Object-oriented programming, algorithm optimization, project management.", // Kabelo
            "Machine learning algorithms, data visualization, statistical analysis.", // Lerato
            "Mechanical design, physics tutoring, engineering problem-solving.", // Sizwe
            "Language acquisition strategies, intercultural communication, lesson planning.", // Mbali
            "Critical thinking, problem-solving techniques, foundational mathematics.", // Ayanda
            "Historical analysis, geographic systems, curriculum design.", // Bianca
            "Ecological studies, environmental policy, research methodologies." // Christina
        )
        reviewSubject = arrayOf(
            "Great planning", "Helpful sessions", "Clear guidance",
            "Amazing tutor", "Clear explanations", "Not very engaging", "Patient and understanding",
            "Good at programming", "Helpful with algorithms", "Strong with logic", "Confusing at times", "Knowledgeable",
            "Creative writing expert", "Helpful feedback", "Excellent feedback", "Could improve", "Writing master",
            "Data science skills", "Well-prepared", "Expert tutor", "Good but busy", "Rushed sessions",
            "Passion for languages", "Very encouraging", "Good but slow", "Great teacher", "Lacked structure",
            "Great math skills", "A bit technical", "Fantastic tutor", "Good with numbers", "Rushed lessons",
            "History expert", "Well-organized", "Good tutor", "Engaging lessons", "Not for me",
            "Biology genius", "Great teacher", "Helpful but slow", "Excellent"
        )

// Descriptions
        reviewDescription = arrayOf(
            "Very structured and easy to follow.", "Helped me understand concepts better.", "Simplified difficult concepts very well.",
            "Very patient and knowledgeable.", "Explained things in a simple way.", "Could be more interactive.", "Really took the time to explain things.",
            "Assisted me with coding tasks.", "Really knows his programming.", "Great tutor for programming logic.", "Could have explained more clearly.", "Explained everything with precision.",
            "Great insights on writing.", "Helped me improve my writing style.", "Gave clear advice on my creative pieces.", "Sometimes her feedback was hard to implement.", "She really helped me shape my writing career.",
            "Very knowledgeable in data science.", "Had well-structured lessons.", "Taught me things beyond the syllabus.", "Sessions were often cut short due to his schedule.", "Felt like he was hurrying through the material.",
            "Helped me improve my Xhosa.", "Made learning a new language fun.", "Good tutor, but sometimes slow to respond.", "Very understanding and patient.", "I found her lessons disorganized at times.",
            "Explains math concepts really well.", "Sometimes went into too much detail.", "Helped me ace my math exams.", "Very good with breaking down difficult problems.", "Felt like we were moving too fast for my pace.",
            "Helped with my history assignments.", "Very organized and knew her content well.", "Helpful, but could improve time management.", "Made history really interesting.", "Felt like I didn't connect with her teaching style.",
            "Very thorough in biology concepts.", "Helped me understand complex biology topics.", "Took time to explain things but sometimes too slow.", "Best tutor I've ever had for biology."
        )

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

        tutorList = arrayListOf()
        getUserData(subjects)

        return view
    }

    private fun getUserData(subjects: Array<String>) {
        for (i in imageId.indices) {
            val tutor = Tutors1(
                imageId[i], name[i], rating[i], rate[i], location[i], languages[i], description[i], subjects[i],
                workExperience[i], education[i], coursesCertifications[i], skills[i], levels[i]
            )
            tutorList.add(tutor)
        }

        // Adapter setup
        val adapter = TutorAdapter(tutorList)
        recyclerView.adapter = adapter

        // Set item click listener to expand the tutor's profile that was clicked
        adapter.setOnItemClickListener(object : TutorAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {
                val intent = Intent(requireContext(), TutorExpandedActivity::class.java)
                intent.putExtra("ProfilePic", tutorList[position].ProfilePic)
                intent.putExtra("Name", tutorList[position].Name)
                intent.putExtra("Rating", tutorList[position].Rating)
                intent.putExtra("Rate", tutorList[position].Rate)
                intent.putExtra("Location", tutorList[position].Location)
                intent.putExtra("Languages", tutorList[position].Language)
                intent.putExtra("Description", tutorList[position].Description)
                intent.putExtra("WorkExperience", tutorList[position].WorkExperience)
                intent.putExtra("Education", tutorList[position].Education)
                intent.putExtra("CoursesCertifications", tutorList[position].CoursesCertifications)
                intent.putExtra("Skills", tutorList[position].Skills)
                intent.putExtra("Subjects", tutorList[position].Subjects)
                intent.putExtra("Levels", tutorList[position].Levels)

                startActivity(intent)
            }
        })
    }

}