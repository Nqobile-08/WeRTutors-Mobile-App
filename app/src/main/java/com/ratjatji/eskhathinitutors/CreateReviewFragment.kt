package com.ratjatji.eskhathinitutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateReviewFragment : Fragment() {

    private lateinit var spinnerTutors: Spinner
    private lateinit var spinnerCourses: Spinner
    private lateinit var ratingBar: RatingBar
    private lateinit var etReviewSubject: EditText
    private lateinit var etReviewDescription: EditText
    private lateinit var btnSubmitReview: Button
    private lateinit var ratingText: TextView
    private lateinit var goodRemarksChipContainer: ChipGroup
    private lateinit var tvGoodReviewWarningText: TextView
    private lateinit var tvPositives: TextView
    private lateinit var badRemarksChipContainer: ChipGroup
    private lateinit var tvBadReviewWarningText: TextView
    private lateinit var tvIssues: TextView
    private lateinit var tvSubject: TextView

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var userDbRef: DatabaseReference
    private var currentUserName: String = ""

    private val tutorOptions = listOf(
        Tutor("Sipho Mthethwa", listOf("Algebra", "Trigonometry", "Geometry", "Calculus")),
        Tutor("Zanele Mhlongo", listOf("Business Management", "Economics", "Statistics")),
        Tutor("Kabelo Phiri", listOf("Python", "C++", "Java", "Algorithms")),
        Tutor("Lerato Mokwoena", listOf("Data Science", "Machine Learning", "Statistics", "Big Data")),
        Tutor("Sizwe Nkosi", listOf("Mathematics", "Physics", "Engineering Mechanics")),
        Tutor("Mbali Mkhize", listOf("English", "Mandarin", "Communication Skills")),
        Tutor("Ayanda Ndlovu", listOf("Mathematics", "Physical Sciences", "Biology")),
        Tutor("Bianca Moodley", listOf("History", "Geography", "Civics")),
        Tutor("Christina Goncalves", listOf("Biology", "Environmental Sciences", "Ecology"))
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "Review a tutor"
        val view = inflater.inflate(R.layout.fragment_create_review, container, false)

        // Initialize Firebase Auth and Database references
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("student_reviews")
        // Update the reference path to match your structure
        userDbRef = FirebaseDatabase.getInstance().getReference("Users/Students")

        // Fetch current user's name
        fetchCurrentUserName()

        initializeViews(view)
        setupSpinners()
        setupRatingBar()
        setupChips()
        setupSubmitButton()

        return view
    }
    private fun fetchCurrentUserName() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            userDbRef.child(currentUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Get both name and surname to create full name
                        val name = snapshot.child("name").getValue(String::class.java) ?: ""
                        val surname = snapshot.child("surname").getValue(String::class.java) ?: ""

                        // Combine name and surname
                        currentUserName = "$name $surname".trim()

                        if (currentUserName.isBlank()) {
                            // Fallback to email if name is not available
                            currentUserName = snapshot.child("email").getValue(String::class.java)
                                ?.substringBefore('@')
                                ?: "Anonymous User"
                        }
                    } else {
                        Toast.makeText(context, "User profile not found", Toast.LENGTH_SHORT).show()
                        currentUserName = "Anonymous User"
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Failed to fetch user data: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // Handle case where user is not logged in
            Toast.makeText(context, "Please log in to submit a review", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initializeViews(view: View) {
        spinnerTutors = view.findViewById(R.id.spinnerTutors)
        spinnerCourses = view.findViewById(R.id.spinnerCourses)
        ratingBar = view.findViewById(R.id.ratingBar)
        etReviewSubject = view.findViewById(R.id.etReviewSubject)
        etReviewDescription = view.findViewById(R.id.etReviewDescription)
        btnSubmitReview = view.findViewById(R.id.btnSubmitReview)
        ratingText = view.findViewById(R.id.tvRatingText)
        goodRemarksChipContainer = view.findViewById(R.id.cp_good_remarks)
        tvGoodReviewWarningText = view.findViewById(R.id.tvGoodReviewWarningText)
        tvPositives = view.findViewById(R.id.tvPositives)
        tvSubject = view.findViewById(R.id.tvSubject)

        badRemarksChipContainer = view.findViewById(R.id.cp_bad_remarks)
        tvBadReviewWarningText = view.findViewById(R.id.tvBadReviewWarningText)
        tvIssues = view.findViewById(R.id.tvIssues)

        // Initially hide the chips and related views
        goodRemarksChipContainer.visibility = View.GONE
        tvPositives.visibility = View.GONE
        tvGoodReviewWarningText.visibility = View.GONE

        goodRemarksChipContainer.visibility = View.GONE
        tvPositives.visibility = View.VISIBLE
        tvGoodReviewWarningText.visibility = View.GONE
    }

    private fun setupSpinners() {
        val tutorNames = tutorOptions.map { it.name }
        val tutorAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tutorNames)
        tutorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTutors.adapter = tutorAdapter

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
    }

    private fun setupRatingBar() {
        ratingBar.setOnRatingBarChangeListener { _, rating, _ ->
            ratingText.text = when (rating.toInt()) {
                1 -> "Very poor"
                2 -> "Poor"
                3 -> "Decent"
                4 -> "Good"
                5 -> "Great"
                else -> " "
            }

        }
    }
    private fun getSelectedChips(chipGroup: ChipGroup): List<String> {
        val selectedChips = mutableListOf<String>()
        for (id in chipGroup.checkedChipIds) {
            val chip = chipGroup.findViewById<Chip>(id)
            chip?.text?.toString()?.let { selectedChips.add(it) }
        }
        return selectedChips
    }
    private fun setupChips() {
        // Toggle good remarks chip group visibility when tvPositives is clicked
        tvPositives.setOnClickListener {
            if (goodRemarksChipContainer.visibility == View.VISIBLE) {
                goodRemarksChipContainer.visibility = View.GONE
            } else {
                goodRemarksChipContainer.visibility = View.VISIBLE
            }
        }

        // Toggle bad remarks chip group visibility when tvIssues is clicked
        tvIssues.setOnClickListener {
            if (badRemarksChipContainer.visibility == View.VISIBLE) {
                badRemarksChipContainer.visibility = View.GONE
            } else {
                badRemarksChipContainer.visibility = View.VISIBLE
            }
        }

        // Good review chip container listener
        goodRemarksChipContainer.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedGReviewList = checkedIds.map { id ->
                view?.findViewById<Chip>(id)?.text.toString()
            }

            when {
                checkedIds.isEmpty() -> tvGoodReviewWarningText.text = "No good reviews selected"
                checkedIds.size > 5 -> {
                    tvGoodReviewWarningText.text = "Maximum 5 reviews allowed"
                    group.findViewById<Chip>(checkedIds.last()).isChecked = false
                }
                else -> tvGoodReviewWarningText.text = ""
            }
        }

        // Bad review chip container listener
        badRemarksChipContainer.setOnCheckedStateChangeListener { group, checkedIds ->
            val checkedBReviewList = checkedIds.map { id ->
                view?.findViewById<Chip>(id)?.text.toString()
            }

            when {
                checkedIds.isEmpty() -> tvBadReviewWarningText.text = "No bad reviews selected"
                checkedIds.size > 5 -> {
                    tvBadReviewWarningText.text = "Maximum 5 reviews allowed"
                    group.findViewById<Chip>(checkedIds.last()).isChecked = false
                }
                else -> tvBadReviewWarningText.text = ""
            }
        }
    }

    private fun setupSubmitButton() {
        dbRef = FirebaseDatabase.getInstance().getReference("student_reviews")
        btnSubmitReview.setOnClickListener {
            saveReviewData()
        }
    }private fun saveReviewData() {
        val tutorName = spinnerTutors.selectedItem?.toString()
        val subject = spinnerCourses.selectedItem?.toString()
        val rating = ratingBar.rating.toInt()
        val reviewSubject = etReviewSubject.text.toString()
        val reviewDescription = etReviewDescription.text.toString()

        // Get selected good and bad remarks
        val selectedGoodRemarks = getSelectedChips(goodRemarksChipContainer)
        val selectedBadRemarks = getSelectedChips(badRemarksChipContainer)

        if (tutorName.isNullOrBlank() || subject.isNullOrBlank() ||
            reviewSubject.isBlank() || reviewDescription.isBlank()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (currentUserName.isBlank() || currentUserName == "Anonymous User") {
            Toast.makeText(requireContext(), "Unable to fetch user information. Please try again.", Toast.LENGTH_SHORT).show()
            return
        }

        val reviewID = dbRef.push().key ?: ""
        val review = Review(
            reviewID = reviewID,
            tutorName = tutorName,
            rating = rating,
            studentName = currentUserName,
            subject = reviewSubject,
            description = reviewDescription,
            goodRemarks = selectedGoodRemarks,
            badRemarks = selectedBadRemarks
        )

        dbRef.child(tutorName).child(reviewID).setValue(review)
            .addOnCompleteListener {
                Toast.makeText(requireContext(), "Review submitted successfully", Toast.LENGTH_LONG).show()
                clearFields()
            }.addOnFailureListener { err ->
                Toast.makeText(requireContext(), "${err.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun clearFields() {
        spinnerTutors.setSelection(0)
        spinnerCourses.setSelection(0)
        ratingBar.rating = 0f
        etReviewSubject.text.clear()
        etReviewDescription.text.clear()

        // Clear chip selections
        goodRemarksChipContainer.clearCheck()
        badRemarksChipContainer.clearCheck()

        goodRemarksChipContainer.visibility = View.GONE
        tvGoodReviewWarningText.visibility = View.GONE

        badRemarksChipContainer.visibility = View.GONE
        tvBadReviewWarningText.visibility = View.GONE
    }

    // Optional: Add validation for minimum chip selection if needed
    private fun validateChipSelections(): Boolean {
        val goodRemarks = getSelectedChips(goodRemarksChipContainer)
        val badRemarks = getSelectedChips(badRemarksChipContainer)

        if (goodRemarks.isEmpty() && badRemarks.isEmpty()) {
            Toast.makeText(requireContext(), "Please select at least one remark", Toast.LENGTH_SHORT).show()
            return false
        }

        if (goodRemarks.size > 5 || badRemarks.size > 5) {
            Toast.makeText(requireContext(), "Maximum 5 remarks allowed per category", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


    data class Tutor(val name: String, val subjects: List<String>)
}