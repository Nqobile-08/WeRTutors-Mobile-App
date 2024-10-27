package com.ratjatji.eskhathinitutors.Tutors

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.ratjatji.eskhathinitutors.R
import com.ratjatji.eskhathinitutors.TutorBooking
import com.ratjatji.eskhathinitutors.tutorOptions
import java.text.SimpleDateFormat
import java.util.*

class SchedulesFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var appointmentEditText: EditText
    private lateinit var addButton: Button
    private lateinit var removeButton: Button
    private lateinit var appointmentsTextView: TextView

    private lateinit var tutorSpinner: Spinner
    private lateinit var subjectSpinner: Spinner
    private lateinit var timeSpinner: Spinner
    private lateinit var sessionTypeSpinner: Spinner

    private val appointments = mutableMapOf<String, String>()
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var selectedTutor: TutorBooking? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    companion object {
        private const val TAG = "SchedulesFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "Book a tutor"
        val view = inflater.inflate(R.layout.fragment_schedules, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        FirebaseDatabase.getInstance().setLogLevel(com.google.firebase.database.Logger.Level.DEBUG)

        initializeViews(view)
        setupTutorSpinner()

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = dateFormat.format(Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time)

            // Display existing appointment for selected date
            val existingAppointment = appointments[selectedDate]
            appointmentEditText.setText(existingAppointment)
        }

        addButton.setOnClickListener {
            addAppointment()
        }

        removeButton.setOnClickListener {
            removeAppointment()
        }

        return view
    }

    private fun initializeViews(view: View) {
        calendarView = view.findViewById(R.id.calendarView)
        appointmentEditText = view.findViewById(R.id.appointmentEditText)
        addButton = view.findViewById(R.id.addButton)
        removeButton = view.findViewById(R.id.removeButton)
        appointmentsTextView = view.findViewById(R.id.appointmentsTextView)
        tutorSpinner = view.findViewById(R.id.spinnerTutors)
        subjectSpinner = view.findViewById(R.id.spinnerSubjects)
        timeSpinner = view.findViewById(R.id.spinnerTimes)
        sessionTypeSpinner = view.findViewById(R.id.spinnerSessionType)
    }

    private fun addAppointment() {
        val selectedDate = dateFormat.format(Calendar.getInstance().apply {
            timeInMillis = calendarView.date
        }.time)

        val calendar = Calendar.getInstance().apply {
            timeInMillis = calendarView.date
        }
        val selectedDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

        if (selectedTutor != null && selectedTutor!!.availableDays.contains(selectedDayOfWeek)) {
            val appointment = appointmentEditText.text.toString()

            if (appointment.isNotBlank()) {
                saveAppointmentToFirebase(selectedDate, appointment)
            } else {
                Toast.makeText(requireContext(), "Appointment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(requireContext(), "This tutor is not available on the selected day", Toast.LENGTH_SHORT).show()
        }
    }
    private fun saveAppointmentToFirebase(selectedDate: String, appointment: String) {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            // Fetch the user's first name and surname from the database
            val userRef = database.reference.child("Users").child("Students").child(userId)
            userRef.get().addOnSuccessListener { dataSnapshot ->
                val firstName = dataSnapshot.child("name").value as? String ?: "Unknown"
                val lastName = dataSnapshot.child("surname").value as? String ?: "User"
                val fullName = "$firstName $lastName"

                val tutorName = selectedTutor?.name ?: "Unknown Tutor"

                // Add the status field and set it to "Pending"
                val appointmentData = hashMapOf(
                    "date" to selectedDate,
                    "tutor" to tutorName,
                    "subject" to subjectSpinner.selectedItem.toString(),
                    "time" to timeSpinner.selectedItem.toString(),
                    "sessionType" to sessionTypeSpinner.selectedItem.toString(),
                    "details" to appointment,
                    "status" to "Pending"  // Add status field here
                )

                Log.d(TAG, "Attempting to save appointment: $appointmentData")

                database.reference.child("StudentBookingRequest").child(fullName).child(tutorName).push()
                    .setValue(appointmentData)
                    .addOnSuccessListener {
                        Log.d(TAG, "Appointment saved successfully")
                        Toast.makeText(requireContext(), "Appointment saved successfully", Toast.LENGTH_SHORT).show()
                        appointments[selectedDate] = appointment
                        updateAppointmentsTextView()
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Failed to save appointment", e)
                        Toast.makeText(requireContext(), "Failed to save appointment: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }.addOnFailureListener { e ->
                Log.e(TAG, "Failed to fetch user details", e)
                Toast.makeText(requireContext(), "Failed to fetch user details: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Log.w(TAG, "User not logged in")
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
    private fun removeAppointment() {
        val selectedDate = dateFormat.format(Calendar.getInstance().apply {
            timeInMillis = calendarView.date
        }.time)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userId = currentUser.uid

            // Fetch the user's name from the database
            database.reference.child("Users").child("Students").child(userId).child("name")
                .get()
                .addOnSuccessListener { dataSnapshot ->
                    val userName = dataSnapshot.value as? String ?: userId
                    val tutorName = selectedTutor?.name ?: "Unknown Tutor"

                    Log.d(TAG, "Attempting to remove appointment for date: $selectedDate")

                    database.reference.child("StudentBookingRequest").child(userName).child(tutorName)
                        .orderByChild("date")
                        .equalTo(selectedDate)
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (!snapshot.exists()) {
                                    Log.d(TAG, "No appointment found for date: $selectedDate")
                                    Toast.makeText(requireContext(), "No appointment found for $selectedDate", Toast.LENGTH_SHORT).show()
                                    return
                                }
                                for (appointmentSnapshot in snapshot.children) {
                                    appointmentSnapshot.ref.removeValue()
                                        .addOnSuccessListener {
                                            Log.d(TAG, "Appointment removed successfully")
                                            Toast.makeText(requireContext(), "Appointment removed for $selectedDate", Toast.LENGTH_SHORT).show()
                                            appointments.remove(selectedDate)
                                            appointmentEditText.setText("")
                                            updateAppointmentsTextView()
                                        }
                                        .addOnFailureListener { e ->
                                            Log.e(TAG, "Failed to remove appointment", e)
                                            Toast.makeText(requireContext(), "Failed to remove appointment: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.e(TAG, "Database error: ${error.message}", error.toException())
                                Toast.makeText(requireContext(), "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to fetch user name", e)
                    Toast.makeText(requireContext(), "Failed to fetch user name: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.w(TAG, "User not logged in")
            Toast.makeText(requireContext(), "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupTutorSpinner() {
        val tutorNames = tutorOptions.map { it.name }
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tutorNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tutorSpinner.adapter = adapter

        tutorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedTutor = tutorOptions[position]
                updateSubjectSpinner(selectedTutor!!.subjects)
                updateTimeSpinner(selectedTutor!!.availableTimeSlots)
                updateCalendarAvailability(selectedTutor!!.availableDays)
                val sessionTypes = listOf("In-person session", "Virtual session")
                updateSessionTypeSpinner(sessionTypes)
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun updateSubjectSpinner(subjects: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, subjects)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        subjectSpinner.adapter = adapter
    }

    private fun updateTimeSpinner(times: List<String>) {
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, times)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        timeSpinner.adapter = adapter
    }

    private fun updateSessionTypeSpinner(sessionTypes: List<String>) {
        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sessionTypes)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sessionTypeSpinner.adapter = adapter
    }

    private fun updateCalendarAvailability(availableDays: List<Int>) {
        // Disable days that are not available
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            if (!availableDays.contains(dayOfWeek)) {
                Toast.makeText(
                    requireContext(),
                    "This tutor is not available on this day",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun updateAppointmentsTextView() {

        val appointmentsText = StringBuilder("Appointments:\n")
        for ((date, appointment) in appointments) {
            // Get the selected tutor's name
            val tutorName = selectedTutor?.name ?: "No Tutor Selected"

            // Get the selected subject
            val selectedSubject = subjectSpinner.selectedItem.toString()

            // Get the selected time
            val selectedTime = timeSpinner.selectedItem.toString()

            // Get the session type
            val selectedSessionType = sessionTypeSpinner.selectedItem.toString()

            // Append all the details to the appointmentsTextView
            appointmentsText.append("\nTutor: $tutorName\n")
            appointmentsText.append("Date: $date\n")
            appointmentsText.append("Time: $selectedTime\n")
            appointmentsText.append("Subject: $selectedSubject\n")
            appointmentsText.append("Session Type: $selectedSessionType\n")
            appointmentsText.append("Details: $appointment\n")
        }
        appointmentsTextView.text = appointmentsText.toString()
    }
}