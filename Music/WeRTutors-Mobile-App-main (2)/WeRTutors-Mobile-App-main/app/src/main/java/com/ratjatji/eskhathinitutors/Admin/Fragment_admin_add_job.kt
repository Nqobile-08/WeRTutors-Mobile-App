package com.ratjatji.eskhathinitutors.Admin



import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ratjatji.eskhathinitutors.R
import com.ratjatji.eskhathinitutors.Tutors.Job

class AdminAddJobFragment : Fragment() {

    private lateinit var editTextStudentName: EditText
    private lateinit var editTextGrade: EditText
    private lateinit var editTextRate: EditText
    private lateinit var editTextType: EditText
    private lateinit var editTextSchool: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var editTextSubjects: EditText
    private lateinit var buttonAddJob: Button

    private val jobList = mutableListOf<Job>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_admin_add_job, container, false)

        // Initialize views
        editTextStudentName = view.findViewById(R.id.editTextStudentName)
        editTextGrade = view.findViewById(R.id.editTextGrade)
        editTextRate = view.findViewById(R.id.editTextRate)
        editTextType = view.findViewById(R.id.editTextType)
        editTextSchool = view.findViewById(R.id.editTextSchool)
        editTextAddress = view.findViewById(R.id.editTextAddress)
        editTextSubjects = view.findViewById(R.id.editTextSubjects)
        buttonAddJob = view.findViewById(R.id.buttonAddJob)

        // Set up button click to add job
        buttonAddJob.setOnClickListener {
            addJob()
        }

        return view
    }

    private fun addJob() {
        // Get the data from the input fields
        val studentName = editTextStudentName.text.toString().trim()
        val grade = editTextGrade.text.toString().toIntOrNull()
        val rate = editTextRate.text.toString().toDoubleOrNull()
        val type = editTextType.text.toString().trim()
        val school = editTextSchool.text.toString().trim()
        val address = editTextAddress.text.toString().trim()
        val subjects = editTextSubjects.text.toString().split(",").map { it.trim() }

        // Validate the data
        if (studentName.isEmpty() || grade == null || rate == null || type.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new Job instance
        val newJob = Job(
            jobId = "job_${jobList.size + 1}",
            studentName = studentName,
            grade = grade,
            rate = rate,
            type = type,
            school = school,
            address = address,
            subjectList = subjects
        )

        // Add the job to the list (you can replace this with saving to a database)
        jobList.add(newJob)

        // Confirm the job has been added
        Toast.makeText(requireContext(), "Job added successfully!", Toast.LENGTH_SHORT).show()

        // Clear input fields
        editTextStudentName.text.clear()
        editTextGrade.text.clear()
        editTextRate.text.clear()
        editTextType.text.clear()
        editTextSchool.text.clear()
        editTextAddress.text.clear()
        editTextSubjects.text.clear()
    }
}