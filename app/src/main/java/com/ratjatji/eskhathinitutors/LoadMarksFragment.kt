package com.ratjatji.eskhathinitutors

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

import java.text.SimpleDateFormat
import java.util.*

class LoadMarksFragment : Fragment() {

    private lateinit var etSubjectName: TextInputEditText
    private lateinit var spinnerAssessmentType: Spinner
    private lateinit var etCategory: TextInputEditText
    private lateinit var etAssessmentDate: TextInputEditText
    private lateinit var etMark: TextInputEditText
    private lateinit var btnSubmitAssessment: Button

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private val assessmentTypes = listOf("Test", "Assignment", "Exam")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "Load marks"
        val view = inflater.inflate(R.layout.fragment_load_marks, container, false)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize views
        etSubjectName = view.findViewById(R.id.etSubjectName)
        spinnerAssessmentType = view.findViewById(R.id.spinnerAssessmentType)
        etCategory = view.findViewById(R.id.etCategory)
        etAssessmentDate = view.findViewById(R.id.etAssessmentDate)
        etMark = view.findViewById(R.id.etMark)
        btnSubmitAssessment = view.findViewById(R.id.btnSubmitAssessment)

        // Initialize Firebase Database reference
        dbRef = FirebaseDatabase.getInstance().getReference("Marks")

        // Populate the spinner with assessment types
        val assessmentAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            assessmentTypes
        )
        assessmentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAssessmentType.adapter = assessmentAdapter

        // Set click listener for the submit button
        btnSubmitAssessment.setOnClickListener {
            checkAuthenticationAndSubmit()
        }

        return view
    }

    private fun checkAuthenticationAndSubmit() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(requireContext(), "Please log in to submit assessment", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        } else {
            // Proceed with submission using the user's UID
            submitAssessment(currentUser.uid)
        }
    }

    private fun submitAssessment(userUID: String) {
        // Get input values
        val subjectName = etSubjectName.text.toString().trim()
        val assessmentType = spinnerAssessmentType.selectedItem?.toString()
        val category = etCategory.text.toString().trim()
        val assessmentDate = etAssessmentDate.text.toString().trim()
        val mark = etMark.text.toString().trim()

        // Input validation
        if (subjectName.isEmpty() || category.isEmpty() || assessmentDate.isEmpty() || mark.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate date format
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.US)
        try {
            dateFormat.parse(assessmentDate)
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Please enter a valid date (DD/MM/YYYY)", Toast.LENGTH_SHORT).show()
            return
        }

        // Validate mark input
        val markValue = try {
            mark.toInt()
        } catch (e: NumberFormatException) {
            -1
        }

        if (markValue !in 0..100) {
            Toast.makeText(requireContext(), "Please enter a valid mark (0 - 100)", Toast.LENGTH_SHORT).show()
            return
        }

        // Generate a new key for this assessment under the user's UID
        val assessmentID = dbRef.child(userUID).push().key ?: ""

        // Create assessment data
        val assessmentData = mapOf(
            "subjectName" to subjectName,
            "assessmentType" to assessmentType,
            "category" to category,
            "assessmentDate" to assessmentDate,
            "mark" to markValue
        )

        // Save the data under the user's UID
        dbRef.child(userUID).child(assessmentID).setValue(assessmentData)
            .addOnCompleteListener {
                Toast.makeText(requireContext(), "Assessment saved successfully", Toast.LENGTH_LONG).show()
                clearInputFields()
            }
            .addOnFailureListener { error ->
                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun clearInputFields() {
        etSubjectName.text?.clear()
        etCategory.text?.clear()
        etAssessmentDate.text?.clear()
        etMark.text?.clear()
    }
}