package com.ratjatji.eskhathinitutors

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private const val TAG = "TutorExpandedActivity"

class TutorExpandedActivity : AppCompatActivity() {

    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var linearLayout: LinearLayout
    private lateinit var btnAbout: Button
    private lateinit var btnLevelsSubjects: Button
    private lateinit var btnReviews: Button
    private lateinit var btnBooking: Button

    private lateinit var calendarView: CalendarView
    private lateinit var appointmentEditText: EditText
    private lateinit var addButton: Button
    private lateinit var removeButton: Button
    private lateinit var appointmentsTextView: TextView
    private lateinit var textViewSubject: TextView
    private lateinit var textViewTutor: TextView
    private lateinit var textViewTime: TextView
    private lateinit var txtSelectType: TextView

    private lateinit var tutorSpinner: Spinner
    private lateinit var subjectSpinner: Spinner
    private lateinit var timeSpinner: Spinner
    private lateinit var sessionTypeSpinner: Spinner
    private val appointments = mutableMapOf<String, String>()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var selectedCalendarDate: Calendar = Calendar.getInstance()
    private var selectedTutor: TutorBooking? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    companion object {
        private const val TAG = "SchedulesFragment"
        private val AVAILABLE_DAYS = listOf(
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY
        )
    }
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

        // Declaring tutor about
        val tutorWorkHeading: TextView = findViewById(R.id.tvAboutTutorWorkExpHeading)
        val tutorWork: TextView = findViewById(R.id.tvAboutTutorWorkExp)
        val tutorEducationHeading: TextView = findViewById(R.id.tvAboutTutorEducationHeading)
        val tutorEducation: TextView = findViewById(R.id.tvAboutTutorEducation)
        val tutorSkillsHeading: TextView = findViewById(R.id.tvAboutTutorSkillsHeading)
        val tutorSkills: TextView = findViewById(R.id.tvAboutTutorSkills)
        val tutorCoursesHeading: TextView = findViewById(R.id.tvAboutTutorCoursesHeading)
        val tutorCourses: TextView = findViewById(R.id.tvAboutTutorCourses)

        // Declaring booking *
        appointmentsTextView= findViewById(R.id.appointmentsTextView)
        textViewTutor= findViewById(R.id.textViewTutor)
        textViewSubject= findViewById(R.id.textViewSubject)
        textViewTime= findViewById(R.id.textViewTime)
        txtSelectType= findViewById(R.id.txtSelectType)
        removeButton= findViewById(R.id.removeButton)
        addButton= findViewById(R.id.addButton)
        appointmentEditText= findViewById(R.id.appointmentEditText)
        sessionTypeSpinner = findViewById(R.id.spinnerSessionType)
        calendarView = findViewById(R.id.calendarView)
        appointmentEditText = findViewById(R.id.appointmentEditText)
        addButton = findViewById(R.id.addButton)
        removeButton = findViewById(R.id.removeButton)
        tutorSpinner = findViewById(R.id.spinnerTutors)
        subjectSpinner = findViewById(R.id.spinnerSubjects)
        timeSpinner = findViewById(R.id.spinnerTimes)
        sessionTypeSpinner = findViewById(R.id.spinnerSessionType)

        setupTutorSpinner()

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedCalendarDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val selectedDate = dateFormat.format(selectedCalendarDate.time)
            val dayOfWeek = selectedCalendarDate.get(Calendar.DAY_OF_WEEK)

            if (AVAILABLE_DAYS.contains(dayOfWeek)) {
                val existingAppointment = appointments[selectedDate]
                appointmentEditText.setText(existingAppointment)
            } else {
                appointmentEditText.setText("")
                Toast.makeText(
                    this,
                    "Tutors are only available Monday to Thursday",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        addButton.setOnClickListener {
            addAppointment()
        }

        removeButton.setOnClickListener {
            removeAppointment()
        }


        // Declaring tutor levels and subject
        val tutorLevelHeading: TextView = findViewById(R.id.tvLevelsAndSubjects1)
        val tutorLevel: TextView = findViewById(R.id.tvLevels)
        val tutorSubjectsHeading: TextView = findViewById(R.id.tvLevelsAndSubjects2)
        val tutorSubjects: TextView = findViewById(R.id.tvSubjects)
        val tutorLevels: TextView = findViewById(R.id.tvLevels)


        // Declaring buttons in linear layout
        btnAbout = findViewById(R.id.btnAbout)
        btnLevelsSubjects=findViewById(R.id.btnLevelsSubjects)
        btnReviews=findViewById(R.id.btnReviews)
        btnBooking=findViewById(R.id.btnBooking)

        linearLayout = findViewById(R.id.linearLayout)
        btnAbout.setOnClickListener { selectButton(btnAbout) }
        btnLevelsSubjects.setOnClickListener { selectButton(btnLevelsSubjects) }
        btnReviews.setOnClickListener { selectButton(btnReviews) }
        // Corresponding views
        val tvLevelsAndSubjects: TextView = findViewById(R.id.tvLevelsAndSubjects1)
        reviewRecyclerView = findViewById(R.id.rvReviews)

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
        tutorLevelHeading.visibility = View.GONE
        tutorLevel.visibility = View.GONE
        tutorSubjectsHeading.visibility = View.GONE
        tutorSubjects.visibility = View.GONE
calendarView.visibility = View.GONE
        reviewRecyclerView.visibility = View.GONE

        btnBooking.setOnClickListener {
            // Hide About section views
            tutorCoursesHeading.visibility = View.GONE
            tutorCourses.visibility = View.GONE
            tutorSkillsHeading.visibility = View.GONE
            tutorSkills.visibility = View.GONE
            tutorEducationHeading.visibility = View.GONE
            tutorEducation.visibility = View.GONE
            tutorWorkHeading.visibility = View.GONE
            tutorWork.visibility = View.GONE

            // Show booking container
            appointmentsTextView.visibility = View.VISIBLE
            removeButton.visibility = View.VISIBLE
            addButton.visibility = View.VISIBLE
            appointmentEditText.visibility = View.VISIBLE
            calendarView.visibility = View.VISIBLE
            tutorSpinner .visibility = View.VISIBLE
            subjectSpinner .visibility = View.VISIBLE
            timeSpinner .visibility = View.VISIBLE
            sessionTypeSpinner .visibility = View.VISIBLE
            textViewSubject.visibility = View.VISIBLE
            textViewTutor.visibility = View.VISIBLE
            textViewTime.visibility = View.VISIBLE
            txtSelectType.visibility = View.VISIBLE

            // Hide Levels and Subjects views
            tvLevelsAndSubjects.visibility = View.GONE
            tutorLevelHeading.visibility = View.GONE
            tutorLevel.visibility = View.GONE
            tutorSubjectsHeading.visibility = View.GONE
            tutorSubjects.visibility = View.GONE

            // Hide Reviews
            reviewRecyclerView.visibility = View.GONE
        }
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
            tutorLevelHeading.visibility = View.GONE
            tutorLevel.visibility = View.GONE
            tutorSubjectsHeading.visibility = View.GONE
            tutorSubjects.visibility = View.GONE

            reviewRecyclerView.visibility = View.GONE

            appointmentsTextView.visibility = View.GONE
            removeButton.visibility = View.GONE
            addButton.visibility = View.GONE
            appointmentEditText.visibility = View.GONE
            calendarView.visibility = View.GONE
            tutorSpinner .visibility = View.GONE
            subjectSpinner .visibility = View.GONE
            timeSpinner .visibility = View.GONE
            sessionTypeSpinner .visibility = View.GONE
            textViewSubject.visibility = View.GONE
            textViewTutor.visibility = View.GONE
            textViewTime.visibility = View.GONE
            txtSelectType.visibility = View.GONE
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
            tutorLevelHeading.visibility = View.VISIBLE
            tutorLevel.visibility = View.VISIBLE
            tutorSubjectsHeading.visibility = View.VISIBLE
            tutorSubjects.visibility = View.VISIBLE

            reviewRecyclerView.visibility = View.GONE

            appointmentsTextView.visibility = View.GONE
            removeButton.visibility = View.GONE
            addButton.visibility = View.GONE
            appointmentEditText.visibility = View.GONE
            calendarView.visibility = View.GONE
            tutorSpinner .visibility = View.GONE
            subjectSpinner .visibility = View.GONE
            timeSpinner .visibility = View.GONE
            sessionTypeSpinner .visibility = View.GONE
            textViewSubject.visibility = View.GONE
            textViewTutor.visibility = View.GONE
            textViewTime.visibility = View.GONE
            txtSelectType.visibility = View.GONE
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
            tutorLevelHeading.visibility = View.GONE
            tutorLevel.visibility = View.GONE
            tutorSubjectsHeading.visibility = View.GONE
            tutorSubjects.visibility = View.GONE

            reviewRecyclerView.visibility = View.VISIBLE

            appointmentsTextView.visibility = View.GONE
            removeButton.visibility = View.GONE
            addButton.visibility = View.GONE
            appointmentEditText.visibility = View.GONE
            calendarView.visibility = View.GONE
            tutorSpinner .visibility = View.GONE
            subjectSpinner .visibility = View.GONE
            timeSpinner .visibility = View.GONE
            sessionTypeSpinner .visibility = View.GONE
            textViewSubject.visibility = View.GONE
            textViewTutor.visibility = View.GONE
            textViewTime.visibility = View.GONE
            txtSelectType.visibility = View.GONE
        }

        // Retrieving data from intent
        val bundle: Bundle? = intent.extras
        val name = bundle?.getString("TUTOR_NAME")
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
        val subjects = bundle?.getString("Subjects") // Get subjects array
        val levels = bundle?.getString("Levels") // Get subjects array

        // Setting tutor data
        tutorProfilePicture.setImageResource(profilePic ?: R.drawable.icon)
        tutorName.text = name
        tutorRating.text = rating
        tutorRate.text = "R $rate/hour"
        tutorLocation.text = location
        tutorLanguages.text = language
        tutorDescription.text = description
        tutorSkills.text = skills
        tutorCourses.text = coursesCertification
        tutorEducation.text = education
        tutorWork.text = workExp
        tutorSubjects.text = subjects
        tutorLevels.text = levels

        Log.d(TAG, "Activity created")

        // Setting up RecyclerView for reviews
        reviewRecyclerView.layoutManager = LinearLayoutManager(this)

        // Load reviews for the specific tutor
        if (name != null) {
            loadReviewsFromFirebase(name)
        }
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        //initializeViews()
        setupTutorSpinner()

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedCalendarDate = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }

            val selectedDate = dateFormat.format(selectedCalendarDate.time)
            val dayOfWeek = selectedCalendarDate.get(Calendar.DAY_OF_WEEK)

            if (AVAILABLE_DAYS.contains(dayOfWeek)) {
                val existingAppointment = appointments[selectedDate]
                appointmentEditText.setText(existingAppointment)
            } else {
                appointmentEditText.setText("")
                Toast.makeText(
                    this,
                    "Tutors are only available Monday to Thursday",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        addButton.setOnClickListener {
            addAppointment()
        }

        removeButton.setOnClickListener {
            removeAppointment()
        }

    }
    private fun addAppointment() {
        val selectedDate = dateFormat.format(selectedCalendarDate.time)
        val selectedDayOfWeek = selectedCalendarDate.get(Calendar.DAY_OF_WEEK)

        if (AVAILABLE_DAYS.contains(selectedDayOfWeek)) {
            val appointment = appointmentEditText.text.toString()

            if (appointment.isNotBlank()) {
                saveAppointmentToFirebase(selectedDate, appointment)
            } else {
                Toast.makeText(this, "Appointment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Tutors are only available Monday to Thursday", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveAppointmentToFirebase(selectedDate: String, appointment: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            val userRef = database.reference.child("Users").child("Students").child(userId)
            userRef.get().addOnSuccessListener { dataSnapshot ->
                val firstName = dataSnapshot.child("name").value as? String ?: "Unknown"
                val lastName = dataSnapshot.child("surname").value as? String ?: "User"
                val fullName = "$firstName $lastName"

                val tutorName = selectedTutor?.name ?: "Unknown Tutor"

                val appointmentData = hashMapOf(
                    "date" to selectedDate,
                    "tutor" to tutorName,
                    "subject" to subjectSpinner.selectedItem.toString(),
                    "time" to timeSpinner.selectedItem.toString(),
                    "sessionType" to sessionTypeSpinner.selectedItem.toString(),
                    "details" to appointment,
                    "status" to "Pending"
                )

                database.reference.child("StudentBookingRequest").child(fullName).child(tutorName).push()
                    .setValue(appointmentData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Appointment saved successfully", Toast.LENGTH_SHORT).show()
                        appointments[selectedDate] = appointment
                        updateAppointmentsTextView()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to save appointment: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener { e ->
                Toast.makeText(this, "Failed to fetch user details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeAppointment() {
        val selectedDate = dateFormat.format(selectedCalendarDate.time)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            database.reference.child("Users").child("Students").child(userId).child("name")
                .get()
                .addOnSuccessListener { dataSnapshot ->
                    val userName = dataSnapshot.value as? String ?: userId
                    val tutorName = selectedTutor?.name ?: "Unknown Tutor"

                    database.reference.child("StudentBookingRequest").child(userName).child(tutorName)
                        .orderByChild("date")
                        .equalTo(selectedDate)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (!snapshot.exists()) {
                                    Toast.makeText(this@TutorExpandedActivity, "No appointment found for $selectedDate", Toast.LENGTH_SHORT).show()
                                    return
                                }
                                for (appointmentSnapshot in snapshot.children) {
                                    appointmentSnapshot.ref.removeValue()
                                        .addOnSuccessListener {
                                            Toast.makeText(this@TutorExpandedActivity, "Appointment removed for $selectedDate", Toast.LENGTH_SHORT).show()
                                            appointments.remove(selectedDate)
                                            appointmentEditText.setText("")
                                            updateAppointmentsTextView()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(this@TutorExpandedActivity, "Failed to remove appointment: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@TutorExpandedActivity, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to fetch user name: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateAppointmentsTextView() {
        val appointmentsText = StringBuilder("Appointments:\n")
        for ((date, appointment) in appointments) {
            val tutorName = selectedTutor?.name ?: "No Tutor Selected"
            val selectedSubject = subjectSpinner.selectedItem.toString()
            val selectedTime = timeSpinner.selectedItem.toString()
            val selectedSessionType = sessionTypeSpinner.selectedItem.toString()

            appointmentsText.append("\nTutor: $tutorName\n")
            appointmentsText.append("Date: $date\n")
            appointmentsText.append("Time: $selectedTime\n")
            appointmentsText.append("Subject: $selectedSubject\n")
            appointmentsText.append("Session Type: $selectedSessionType\n")
            appointmentsText.append("Details: $appointment\n")
        }
        appointmentsTextView.text = appointmentsText.toString()
    }

   private fun setupTutorSpinner() {
        val tutorNames = tutorOptions.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, tutorNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tutorSpinner.adapter = adapter

        tutorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedTutor = tutorOptions[position]
                updateSubjectSpinner(selectedTutor!!.subjects)
                updateTimeSpinner(selectedTutor!!.availableTimeSlots)
                val sessionTypes = listOf("In-person session", "Virtual session")
                updateSessionTypeSpinner(sessionTypes)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }

    private fun updateSubjectSpinner(subjects: List<String>) {
        val adapter = ArrayAdapter(this@TutorExpandedActivity, android.R.layout.simple_spinner_item, subjects)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subjectSpinner.adapter = adapter
    }

    private fun updateTimeSpinner(times: List<String>) {
        val adapter = ArrayAdapter(this@TutorExpandedActivity, android.R.layout.simple_spinner_item, times)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = adapter
    }

    private fun updateSessionTypeSpinner(sessionTypes: List<String>) {
        val adapter = ArrayAdapter(this@TutorExpandedActivity, android.R.layout.simple_spinner_item, sessionTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sessionTypeSpinner.adapter = adapter
    }
}
    }

    private fun selectButton(selectedButton: Button) {
        // Reset the color of all buttons to black
        btnAbout.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        btnLevelsSubjects.setTextColor(ContextCompat.getColor(this, android.R.color.black))
        btnReviews.setTextColor(ContextCompat.getColor(this, android.R.color.black))

        // Change the text color of the selected button to blue
        selectedButton.setTextColor(ContextCompat.getColor(this, R.color.WRTBlue)) // Replace with your blue color
    }

    private fun loadReviewsFromFirebase(tutorName: String) {
        val databaseRef = FirebaseDatabase.getInstance().getReference("student_reviews")

        // Fetch reviews for a specific tutor
        databaseRef.child(tutorName)  // Use the tutor's name passed in the intent
            .get().addOnSuccessListener { snapshot ->
                val reviews = mutableListOf<Review>()
                for (reviewSnapshot in snapshot.children) {
                    val review = reviewSnapshot.getValue(Review::class.java)
                    if (review != null) {
                        reviews.add(review)
                    }
                }
// Pass reviews to the adapter
                reviewAdapter = ReviewAdapter(reviews)
                reviewRecyclerView.adapter = reviewAdapter
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to load reviews", Toast.LENGTH_SHORT).show()
            }
    }
}