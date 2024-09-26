package com.nqobza.wertutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class CreateReviewFragment : Fragment() {

    private lateinit var spinnerTutors: Spinner
    private lateinit var spinnerCourses: Spinner
    private lateinit var ratingBar: RatingBar
    private lateinit var etReviewSubject: EditText
    private lateinit var etReviewDescription: EditText
    private lateinit var btnSubmitReview: Button

    private val tutorOptions = listOf(
        Tutor("Sipho Mthethwa", listOf("Algebra", "Trigonometry", "Geometry", "Calculus")),
        Tutor("Zanele Mhlongo", listOf("Business Management", "Economics", "Statistics")),
        Tutor("Kabelo Phiri", listOf("Python", "C++", "Java", "Algorithms")),
        Tutor("Lerato Mokwoena", listOf("Data SCience", "Machine Learning", " Statistics", "Big Data")),
        Tutor("Sizwe Nkosi", listOf("Mathematics", "Physics", "Engineering Mechanics")),
        Tutor("Mbali Mkhize", listOf("English", "Manadarin", "Communication Skills")),
        Tutor("Ayanda Ndlovu", listOf("Mathematics", "Physical Sciences", "Biology")),
        Tutor("Bianca Moodley", listOf("History", "Geography", "Civics")),
        Tutor("Christina Goncalves", listOf("Biology", "Environmental Sciences", "Ecology")),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_create_review, container, false)

        spinnerTutors = view.findViewById(R.id.spinnerTutors)
        spinnerCourses = view.findViewById(R.id.spinnerCourses)
        ratingBar = view.findViewById(R.id.ratingBar)
        etReviewSubject = view.findViewById(R.id.etReviewSubject)
        etReviewDescription = view.findViewById(R.id.etReviewDescription)
        btnSubmitReview = view.findViewById(R.id.btnSubmitReview)

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

        // Step 3: Handle Review Submission
        btnSubmitReview.setOnClickListener {
            val tutorName = spinnerTutors.selectedItem?.toString()
            val subject = spinnerCourses.selectedItem?.toString()
            val rating = ratingBar.rating.toInt()
            val reviewSubject = etReviewSubject.text.toString()
            val reviewDescription = etReviewDescription.text.toString()

            if (tutorName.isNullOrBlank() || subject.isNullOrBlank() ||
                reviewSubject.isBlank() || reviewDescription.isBlank()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                val review = Review(
                    tutorName = tutorName,
                    rating = rating,
                    studentName = "Student Name", // Replace with actual student name
                    subject = reviewSubject,
                    description = reviewDescription
                )

                // Save or handle the review
                Toast.makeText(requireContext(), "Review submitted!", Toast.LENGTH_SHORT).show()

                // Optionally, clear fields after submission
                clearFields()
            }
        }

        return view
    }

    private fun clearFields() {
        spinnerTutors.setSelection(0)
        spinnerCourses.setSelection(0)
        ratingBar.rating = 0f
        etReviewSubject.text.clear()
        etReviewDescription.text.clear()
    }

    // Dummy tutor class for the spinner
    data class Tutor(val name: String, val subjects: List<String>)
}
