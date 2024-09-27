package com.nqobza.wertutors
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.slider.Slider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.nqobza.wertutors.R
import java.util.concurrent.TimeUnit

class SessionActivity : AppCompatActivity() {

    private lateinit var sessionTitle: TextView
    private lateinit var tutorName: TextView
    private lateinit var sessionStatus: TextView
    private lateinit var timerDisplay: TextView
    private lateinit var startSessionButton: Button
    private lateinit var stopSessionButton: Button
    private lateinit var submitSessionButton: Button
    private lateinit var notesInput: EditText
    private lateinit var timeSlider: Slider
    private lateinit var selectedTimeDisplay: TextView
    private lateinit var sessionProgress: ProgressBar

    private var startTime: Long = 0L
    private var running = false
    private var sessionDuration: Long = 0L
    private var remainingTime: Long = 0L
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    private lateinit var progressAnimator: ObjectAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_session_activity)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        initializeViews()
        setupListeners()
        displayCurrentUser()
    }

    private fun initializeViews() {
        sessionTitle = findViewById(R.id.sessionTitle)
        tutorName = findViewById(R.id.tutorName)
        sessionStatus = findViewById(R.id.sessionStatus)
        timerDisplay = findViewById(R.id.timerDisplay)
        startSessionButton = findViewById(R.id.startSessionButton)
        stopSessionButton = findViewById(R.id.stopSessionButton)
        submitSessionButton = findViewById(R.id.submitSessionButton)
        notesInput = findViewById(R.id.notesInput)
        timeSlider = findViewById(R.id.timeSlider)
        selectedTimeDisplay = findViewById(R.id.selectedTimeDisplay)
        sessionProgress = findViewById(R.id.sessionProgress)

        stopSessionButton.isEnabled = false
        submitSessionButton.isEnabled = false
    }

    private fun setupListeners() {
        startSessionButton.setOnClickListener { startSession() }
        stopSessionButton.setOnClickListener { stopSession() }
        submitSessionButton.setOnClickListener { submitSession() }

        timeSlider.addOnChangeListener { _, value, _ ->
            sessionDuration = TimeUnit.MINUTES.toMillis(value.toLong())
            updateSelectedTimeDisplay()
        }
    }

    private fun displayCurrentUser() {
        val user = auth.currentUser
        if (user != null) {
            tutorName.text = "Tutor: ${user.displayName ?: user.email}"
        } else {
            tutorName.text = "Tutor: Not signed in"
            // Disable all functionality if not signed in
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
            Toast.makeText(this, "Please set a session duration", Toast.LENGTH_SHORT).show()
            return
        }

        startTime = System.currentTimeMillis()
        remainingTime = sessionDuration
        running = true
        handler.post(updateTimer)

        sessionStatus.text = "Status: Running"
        startSessionButton.isEnabled = false
        stopSessionButton.isEnabled = true
        timeSlider.isEnabled = false

        // Start progress bar animation
        startProgressAnimation()

        // Create a new session in Firebase
        createSessionInFirebase()
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
                "tutorName" to (user.displayName ?: user.email)
            )
            sessionRef.setValue(sessionData)
                .addOnSuccessListener {
                    Toast.makeText(this, "Session started and saved to Firebase", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save session to Firebase", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show()
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

        // Stop progress bar animation
        progressAnimator.cancel()

        // Update session status in Firebase
        updateSessionStatusInFirebase("stopped")
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

    private fun submitSession() {
        val notes = notesInput.text.toString()
        val elapsedTime = sessionDuration - remainingTime
        val sessionSummary = "Session completed in ${formatTime(elapsedTime)} with notes: $notes"

        // Submit session data to Firebase
        submitSessionToFirebase(notes, elapsedTime)
    }

    private fun submitSessionToFirebase(notes: String, elapsedTime: Long) {
        val user = auth.currentUser
        if (user != null) {
            val sessionRef = database.reference.child("tutor_sessions").child(user.uid).limitToLast(1)
            sessionRef.get().addOnSuccessListener { dataSnapshot ->
                for (sessionSnapshot in dataSnapshot.children) {
                    val sessionUpdates = hashMapOf<String, Any>(
                        "notes" to notes,
                        "elapsedTime" to elapsedTime,
                        "status" to "completed"
                    )
                    sessionSnapshot.ref.updateChildren(sessionUpdates)
                        .addOnSuccessListener {
                            Toast.makeText(this, "Session submitted successfully", Toast.LENGTH_SHORT).show()
                            resetSession()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Failed to submit session", Toast.LENGTH_SHORT).show()
                        }
                }
            }
        } else {
            Toast.makeText(this, "User not signed in", Toast.LENGTH_SHORT).show()
        }
    }

    private fun resetSession() {
        timerDisplay.text = "00:00:00"
        sessionStatus.text = "Status: Ready"
        notesInput.text.clear()
        startSessionButton.isEnabled = true
        submitSessionButton.isEnabled = false
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
}
