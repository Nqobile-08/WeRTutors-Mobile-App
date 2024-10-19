package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateReviewFragment : Fragment() {

    private lateinit var spinnerTutors: Spinner
    private lateinit var spinnerCourses: Spinner
    private lateinit var ratingBar: RatingBar
    private lateinit var etReviewSubject: EditText
    private lateinit var etReviewDescription: EditText
    private lateinit var btnSubmitReview: Button
    private lateinit var ratingText: TextView
    private lateinit var recyclerView: RecyclerView

    private lateinit var BadReviewList: ArrayList<BadReview>
    private lateinit var dbRef: DatabaseReference

    lateinit var reviewId: Array<Int>
    lateinit var goodReviewOption: Array<String>
    lateinit var badReviewOption: Array<String>

    private val tutorOptions = listOf(
        Tutor("Sipho Mthethwa", listOf("Algebra", "Trigonometry", "Geometry", "Calculus")),
        Tutor("Zanele Mhlongo", listOf("Business Management", "Economics", "Statistics")),
        Tutor("Kabelo Phiri", listOf("Python", "C++", "Java", "Algorithms")),
        Tutor("Lerato Mokwoena", listOf("Data Science", "Machine Learning", "Statistics", "Big Data")),
        Tutor("Sizwe Nkosi", listOf("Mathematics", "Physics", "Engineering Mechanics")),
        Tutor("Mbali Mkhize", listOf("English", "Mandarin", "Communication Skills")),
        Tutor("Ayanda Ndlovu", listOf("Mathematics", "Physical Sciences", "Biology")),
        Tutor("Bianca Moodley", listOf("History", "Geography", "Civics")),
        Tutor("Christina Goncalves", listOf("Biology", "Environmental Sciences", "Ecology")),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_review, container, false)

        var badReviewOptions = arrayOf(
            "Incompatible teaching style",
            "Late arrival",
            "Lack of subject expertise",
            "Overload of information",
            "Inadequate preparations",
            "Unprofessional behaviour",
            "Ineffective communication",
            "Struggles to adapt",
            "Difficulty with virtual lessons"
        )

        var goodReviewOptions = arrayOf(
            "Effective communication", "strong subject knowledge", "arrived on time", "Explains topics well",
            "Preparation & organisation", "Adaptability", "Provides additional work & resources", "Good time management",
            "Professional", "Continuous assessment", "Encouraging")

        var reviewID = arrayOf(
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

        spinnerTutors = view.findViewById(R.id.spinnerTutors)
        spinnerCourses = view.findViewById(R.id.spinnerCourses)
        ratingBar = view.findViewById(R.id.ratingBar)
        etReviewSubject = view.findViewById(R.id.etReviewSubject)
        etReviewDescription = view.findViewById(R.id.etReviewDescription)
        btnSubmitReview = view.findViewById(R.id.btnSubmitReview)
        ratingText = view.findViewById(R.id.tvRatingText)

        // Step 1: Populate the Tutor spinner
        val tutorNames = tutorOptions.map { it.name }
        val tutorAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tutorNames)
        tutorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTutors.adapter = tutorAdapter

        // Step 2: Update the Subject spinner when a Tutor is selected
        spinnerTutors.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedTutor = tutorOptions[position]
                val subjectAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, selectedTutor.subjects)
                subjectAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinnerCourses.adapter = subjectAdapter
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }

        ratingBar.setOnRatingBarChangeListener { rBar, fl, b ->
            ratingText.text = fl.toString()
            when (rBar.rating.toInt()) {
                1 -> ratingText.text = "Very poor"
                2 -> ratingText.text = "Poor"
                3 -> ratingText.text = "Decent"
                4 -> ratingText.text = "Good"
                5 -> ratingText.text = "Great"
                else -> ratingText.text = " "
            }
        }

        dbRef = FirebaseDatabase.getInstance().getReference("student_reviews")

        // Step 3: Handle Review Submission
        btnSubmitReview.setOnClickListener {
            saveReviewData()
        }
        BadReviewList = arrayListOf()
        //getReviewData()
        return view
    }
    private fun getReviewData(badReviewOptions: Array<String>) {

        for (i in reviewId.indices) {
            val badReview = BadReview(
                reviewId[i],
                badReviewOptions[i])
            BadReviewList.add(badReview)

        }

        // Adapter setup
        val adapter = ReviewDescriptionAdapter(BadReviewList)
        recyclerView.adapter = adapter

        // Set item click listener to expand the tutor's profile that was clicked
        adapter.setOnItemClickListener(object : ReviewDescriptionAdapter.onItemClickListener {
            override fun onItemClick(position: Int) {

                try {
                } catch (e: Exception) {

                }
            }
        })

    }
    private fun clearFields() {
        spinnerTutors.setSelection(0)
        spinnerCourses.setSelection(0)
        ratingBar.rating = 0f
        etReviewSubject.text.clear()
        etReviewDescription.text.clear()
    }

    private fun saveReviewData() {
        val tutorName = spinnerTutors.selectedItem?.toString()
        val subject = spinnerCourses.selectedItem?.toString()
        val rating = ratingBar.rating.toInt() // Change to Int for rating
        val reviewSubject = etReviewSubject.text.toString()
        val reviewDescription = etReviewDescription.text.toString()

        if (tutorName.isNullOrBlank() || subject.isNullOrBlank() ||
            reviewSubject.isBlank() || reviewDescription.isBlank()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
        } else {
            val reviewID = dbRef.push().key ?: ""
            val review = Review(
                reviewID = reviewID,
                tutorName = tutorName,
                rating = rating,
                studentName = "Student Name", // Replace with actual student name
                subject = reviewSubject,
                description = reviewDescription
            )

            // Grouping by tutor's name
            dbRef.child(tutorName).child(reviewID).setValue(review)
                .addOnCompleteListener {
                    Toast.makeText(requireContext(), "Review submitted successfully", Toast.LENGTH_LONG).show()
                    clearFields()
                }.addOnFailureListener { err ->
                    Toast.makeText(requireContext(), "${err.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    // Dummy tutor class for the spinner
    data class Tutor(val name: String, val subjects: List<String>)
}
