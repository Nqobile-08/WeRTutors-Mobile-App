package com.ratjatji.eskhathinitutors.Tutors

import android.animation.ObjectAnimator
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.ratjatji.eskhathinitutors.NotificationHelper
import com.ratjatji.eskhathinitutors.R
import java.util.concurrent.TimeUnit

class SessionTimer : Fragment() {

    private lateinit var sessionTitle: TextView
    private lateinit var tutorName: TextView
    private lateinit var sessionStatus: TextView
    private lateinit var timerDisplay: TextView
    private lateinit var startSessionButton: Button
    private lateinit var stopSessionButton: Button
    private lateinit var submitSessionButton: Button
    private lateinit var notesInput: EditText
    private lateinit var commentInput: EditText
    private lateinit var timeSlider: Slider
    private lateinit var selectedTimeDisplay: TextView
    private lateinit var sessionProgress: ProgressBar
    private lateinit var sessionDurationSpinner: Spinner
    private lateinit var linkedStudentsSpinner: Spinner
    private lateinit var sessionTypeSpinner: Spinner
    private lateinit var practicalWorkSpinner: Spinner
    private lateinit var uploadProofButton: Button
    private lateinit var addMoreTimeButton: Button

    private var startTime: Long = 0L
    private var running = false
    private var sessionDuration: Long = 0L
    private var remainingTime: Long = 0L
    private val handler = Handler(Looper.getMainLooper())
    private var selectedFileUri: Uri? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var progressAnimator: ObjectAnimator

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_session_timer, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Create notification channels
        NotificationHelper.createNotificationChannels(requireContext())

        initializeViews(view)
        setupListeners()
        displayCurrentUser()
        setupSpinners()

        return view
    }

    private fun initializeViews(view: View) {
        sessionTitle = view.findViewById(R.id.sessionTitle)
        tutorName = view.findViewById(R.id.tutorName)
        sessionStatus = view.findViewById(R.id.sessionStatus)
        timerDisplay = view.findViewById(R.id.timerDisplay)
        startSessionButton = view.findViewById(R.id.startSessionButton)
        stopSessionButton = view.findViewById(R.id.stopSessionButton)
        submitSessionButton = view.findViewById(R.id.submitSessionButton)
        notesInput = view.findViewById(R.id.notesInput)
        commentInput = view.findViewById(R.id.commentInput)
        timeSlider = view.findViewById(R.id.timeSlider)
        selectedTimeDisplay = view.findViewById(R.id.selectedTimeDisplay)
        sessionProgress = view.findViewById(R.id.sessionProgress)
        sessionDurationSpinner = view.findViewById(R.id.sessionDurationSpinner)
        linkedStudentsSpinner = view.findViewById(R.id.linkedStudentsSpinner)
        sessionTypeSpinner = view.findViewById(R.id.sessionTypeSpinner)
        practicalWorkSpinner = view.findViewById(R.id.practicalWorkSpinner)
        uploadProofButton = view.findViewById(R.id.uploadProofButton)
        addMoreTimeButton = view.findViewById(R.id.addMoreTimeButton)

        stopSessionButton.isEnabled = false
        submitSessionButton.isEnabled = false
        addMoreTimeButton.isEnabled = false
    }

    private fun setupListeners() {
        startSessionButton.setOnClickListener { startSession() }
        stopSessionButton.setOnClickListener { stopSession() }
        submitSessionButton.setOnClickListener { validateAndSubmitSession() }
        addMoreTimeButton.setOnClickListener { addMoreTime() }
        uploadProofButton.setOnClickListener { getFile.launch("*/*") }

        timeSlider.addOnChangeListener { _, value, _ ->
            sessionDuration = TimeUnit.MINUTES.toMillis(value.toLong())
            updateSelectedTimeDisplay()
        }

        sessionDurationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                // Update session duration based on spinner selection
                val selectedDuration = parent.getItemAtPosition(position) as String
                sessionDuration = when (selectedDuration) {
                    "15 mins" -> TimeUnit.MINUTES.toMillis(15)
                    "30 mins" -> TimeUnit.MINUTES.toMillis(30)
                    "45 mins" -> TimeUnit.MINUTES.toMillis(45)
                    "1 hour" -> TimeUnit.HOURS.toMillis(1)
                    "1 hour 15 mins" -> TimeUnit.MINUTES.toMillis(75)
                    "1 hour 30 mins" -> TimeUnit.MINUTES.toMillis(90)
                    "1 hour 45 mins" -> TimeUnit.MINUTES.toMillis(105)
                    "2 hours" -> TimeUnit.HOURS.toMillis(2)
                    "2 hours 15 mins" -> TimeUnit.MINUTES.toMillis(135)
                    "2 hours 30 mins" -> TimeUnit.MINUTES.toMillis(150)
                    "2 hours 45 mins" -> TimeUnit.MINUTES.toMillis(165)
                    "3 hours" -> TimeUnit.HOURS.toMillis(3)
                    else -> 0L
                }
                updateSelectedTimeDisplay()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // Do nothing
            }
        }
    }

    private fun displayCurrentUser() {
        val user = auth.currentUser
        if (user != null) {
            tutorName.text = "Tutor: ${user.displayName ?: user.email}"
        } else {
            tutorName.text = "Tutor: Not signed in"
            startSessionButton.isEnabled = false
            timeSlider.isEnabled = false
        }
    }

    private fun updateSelectedTimeDisplay() {
        val hours = TimeUnit.MILLISECONDS.toHours(sessionDuration)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(sessionDuration) % 60
        selectedTimeDisplay.text = String.format("%02d:%02d:00", hours, minutes)
    }

    private fun startSession() {
        if (sessionDuration == 0L) {
            Toast.makeText(requireContext(), "Please set a session duration", Toast.LENGTH_SHORT).show()
            return
        }

        startTime = System.currentTimeMillis()
        remainingTime = sessionDuration
        running = true
        handler.post(updateTimer)

        sessionStatus.text = "Status: Running"
        startSessionButton.isEnabled = false
        stopSessionButton.isEnabled = true
        addMoreTimeButton.isEnabled = true
        timeSlider.isEnabled = false

        startProgressAnimation()
        createSessionInFirebase()

        // Send notification
        NotificationHelper.sendNotification(requireContext(), "session_reminders", "Session Started", "Your session has started successfully.")
    }

    private fun startProgressAnimation() {
        progressAnimator = ObjectAnimator.ofInt(sessionProgress, "progress", 0, 100)
        progressAnimator.duration = sessionDuration
        progressAnimator.interpolator = LinearInterpolator()
        progressAnimator.start()
    }

    private fun createSessionInFirebase() {
        val user = auth.currentUser
        if (user != null) {
            val sessionRef = database.reference.child("tutor_sessions").child(user.uid).push()
            val sessionData = hashMapOf(
                "startTime" to startTime,
                "duration" to sessionDuration,
                "status" to "running",
                "tutorName" to (user.displayName ?: user.email),
                "tutorEmail" to (user.email ?: "Unknown")
            )
            sessionRef.setValue(sessionData)
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Session started and saved to Firebase", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to save session to Firebase", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "User not signed in", Toast.LENGTH_SHORT).show()
        }
    }

    private val updateTimer = object : Runnable {
        override fun run() {
            if (running) {
                remainingTime -= 1000
                if (remainingTime <= 0) {
                    stopSession()
                    return
                }

                updateTimerDisplay(remainingTime)
                handler.postDelayed(this, 1000)
            }
        }
    }

    private fun updateTimerDisplay(time: Long) {
        val hours = TimeUnit.MILLISECONDS.toHours(time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60
        timerDisplay.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun stopSession() {
        running = false
        handler.removeCallbacks(updateTimer)
        sessionStatus.text = "Status: Stopped"
        stopSessionButton.isEnabled = false
        submitSessionButton.isEnabled = true

        progressAnimator.cancel()

        updateSessionStatusInFirebase("stopped")

        // Send notification
        NotificationHelper.sendNotification(requireContext(), "session_reminders", "Session Stopped", "Your session has been stopped.")
    }

    private fun addMoreTime() {
        val additionalTime = TimeUnit.MINUTES.toMillis(15)
        sessionDuration += additionalTime
        remainingTime += additionalTime

        updateSelectedTimeDisplay()
        Toast.makeText(requireContext(), "15 minutes added to the session.", Toast.LENGTH_SHORT).show()

        if (!running) {
            running = true
            handler.post(updateTimer)
        }
    }

    private fun updateSessionStatusInFirebase(status: String) {
        val user = auth.currentUser
        if (user != null) {
            val sessionRef = database.reference.child("tutor_sessions").child(user.uid).limitToLast(1)
            sessionRef.get().addOnSuccessListener { dataSnapshot ->
                for (sessionSnapshot in dataSnapshot.children) {
                    sessionSnapshot.ref.child("status").setValue(status)
                }
            }
        }
    }

    private val getFile = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedFileUri = uri
            Toast.makeText(requireContext(), "File selected: ${uri.lastPathSegment}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateAndSubmitSession() {
        val notes = notesInput.text.toString()
        if (notes.split("\\s+".toRegex()).size < 80) {
            Toast.makeText(requireContext(), "Please enter at least 80 words for session notes", Toast.LENGTH_SHORT).show()
            return
        }
        submitSession()
    }

    private fun submitSession() {
        val notes = notesInput.text.toString()
        val comment = commentInput.text.toString()
        val elapsedTime = System.currentTimeMillis() - startTime
        val sessionSummary = "Session completed in ${formatTime(elapsedTime)} with notes: $notes"

        submitSessionToFirebase(notes, comment, elapsedTime)

        // Send notification
        NotificationHelper.sendNotification(requireContext(), "session_reminders", "Session Submitted", "Your session has been submitted successfully.")
    }

    private fun submitSessionToFirebase(notes: String, comment: String, elapsedTime: Long) {
        val user = auth.currentUser
        if (user != null) {
            val sessionRef = database.reference.child("tutor_sessions").child(user.uid).limitToLast(1)
            sessionRef.get().addOnSuccessListener { dataSnapshot ->
                for (sessionSnapshot in dataSnapshot.children) {
                    val sessionUpdates = hashMapOf<String, Any>(
                        "notes" to notes,
                        "elapsedTime" to elapsedTime,
                        "actualDuration" to sessionDuration,
                        "status" to "completed",
                        "comment" to comment
                    )
                    sessionSnapshot.ref.updateChildren(sessionUpdates)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Session submitted successfully", Toast.LENGTH_SHORT).show()
                            resetSession()
                        }
                        .addOnFailureListener {
                            Toast.makeText(requireContext(), "Failed to submit session", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        } else {
            Toast.makeText(requireContext(), "User not signed in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetSession() {
        timerDisplay.text = "00:00:00"
        sessionStatus.text = "Status: Ready"
        notesInput.text.clear()
        commentInput.text.clear()
        startSessionButton.isEnabled = true
        submitSessionButton.isEnabled = false
        addMoreTimeButton.isEnabled = false
        timeSlider.isEnabled = true
        sessionDuration = 0L
        updateSelectedTimeDisplay()
        sessionProgress.progress = 0
    }

    private fun formatTime(time: Long): String {
        val hours = TimeUnit.MILLISECONDS.toHours(time)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(time) % 60
        val seconds = TimeUnit.MILLISECONDS.toSeconds(time) % 60
        return String.format("%02d:%02d:%02d", hours, minutes, seconds)
    }

    private fun setupSpinners() {
        val durationOptions = arrayOf(
            "15 mins", "30 mins", "45 mins", "1 hour", "1 hour 15 mins", "1 hour 30 mins",
            "1 hour 45 mins", "2 hours", "2 hours 15 mins", "2 hours 30 mins",
            "2 hours 45 mins", "3 hours"
        )
        sessionDurationSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, durationOptions)

        val sessionTypes = arrayOf("Online", "In-Person")
        sessionTypeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, sessionTypes)

        val practicalOptions = arrayOf("Yes", "No")
        practicalWorkSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, practicalOptions)

        fetchLinkedStudents()
    }

    private fun fetchLinkedStudents() {
        val user = auth.currentUser
        if (user != null) {
            val studentsRef = database.reference.child("tutor_students").child(user.uid)
            studentsRef.get().addOnSuccessListener { dataSnapshot ->
                val studentsList = mutableListOf<String>()
                for (studentSnapshot in dataSnapshot.children) {
                    val studentName = studentSnapshot.child("name").value as? String
                    if (studentName != null) {
                        studentsList.add(studentName)
                    }
                }
                linkedStudentsSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, studentsList)
            }
        }
    }
}