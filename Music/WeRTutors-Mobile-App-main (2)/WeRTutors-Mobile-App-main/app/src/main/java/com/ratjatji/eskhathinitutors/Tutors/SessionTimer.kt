package com.ratjatji.eskhathinitutors.Tutors

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.fragment.app.Fragment
import com.google.android.material.slider.Slider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
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

    private var sessionDuration: Long = 0L
    private var startTime: Long = 0L
    private var isRunning = false
    private val handler = Handler(Looper.getMainLooper())

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var sessionRef: DatabaseReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private var selectedFileUri: Uri? = null
    private var sessionId: String = ""
    private var bookedStudent: String = ""

    private val getFile = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            selectedFileUri = it
            Toast.makeText(requireContext(), "File selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_session_timer, container, false)
        initializeFirebase()
        initializeViews(view)
        setupListeners()
        setupRealTimeSync()
        loadLinkedStudent()
        return view
    }

    private fun initializeFirebase() {
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        auth.currentUser?.let { user ->
            sessionId = user.uid
            sessionRef = database.reference.child("sessions").child(sessionId)
        }

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
    }

    private fun initializeViews(view: View) {
        sessionTitle = view.findViewById(R.id.sessionTitle)
        tutorName = view.findViewById(R.id.tutorName)
        sessionStatus = view.findViewById(R.id.sessionStatus)
        timerDisplay = view.findViewById(R.id.timerDisplay)
        sessionProgress = view.findViewById(R.id.sessionProgress)
        startSessionButton = view.findViewById(R.id.startSessionButton)
        stopSessionButton = view.findViewById(R.id.stopSessionButton)
        submitSessionButton = view.findViewById(R.id.submitSessionButton)
        notesInput = view.findViewById(R.id.notesInput)
        commentInput = view.findViewById(R.id.commentInput)
        timeSlider = view.findViewById(R.id.timeSlider)
        selectedTimeDisplay = view.findViewById(R.id.selectedTimeDisplay)
        sessionDurationSpinner = view.findViewById(R.id.sessionDurationSpinner)
        linkedStudentsSpinner = view.findViewById(R.id.linkedStudentsSpinner)
        sessionTypeSpinner = view.findViewById(R.id.sessionTypeSpinner)
        practicalWorkSpinner = view.findViewById(R.id.practicalWorkSpinner)
        uploadProofButton = view.findViewById(R.id.uploadProofButton)
        addMoreTimeButton = view.findViewById(R.id.addMoreTimeButton)

        stopSessionButton.isEnabled = false
        submitSessionButton.isEnabled = false
        addMoreTimeButton.isEnabled = false

        setupSpinners()
    }

    private fun setupSpinners() {
        val durations = resources.getStringArray(R.array.session_durations)
        sessionDurationSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, durations)

        val practicalOptions = resources.getStringArray(R.array.practical_options)
        practicalWorkSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, practicalOptions)

        val sessionTypes = resources.getStringArray(R.array.session_types)
        sessionTypeSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sessionTypes)

        sessionDurationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                sessionDuration = TimeUnit.MINUTES.toMillis((position + 1) * 15L)
                sessionDuration = TimeUnit.MINUTES.toMillis((position + 1) * 15L)
                saveSpinnerSelection("sessionDuration", parent.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        practicalWorkSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                saveSpinnerSelection("practicalWork", parent.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        sessionTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                saveSpinnerSelection("sessionType", parent.getItemAtPosition(position).toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun saveSpinnerSelection(fieldName: String, value: String) {
        firestore.collection("sessions").document(sessionId)
            .set(mapOf(fieldName to value), SetOptions.merge())
    }

    private fun loadLinkedStudent() {
        val userId = auth.currentUser?.uid ?: return
        val tutorRef = database.getReference("Tutors/$userId/bookedStudents")

        tutorRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                bookedStudent = snapshot.children.firstOrNull()?.child("name")?.getValue(String::class.java) ?: ""
                linkedStudentsSpinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, listOf(bookedStudent))
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error loading booked students: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun setupListeners() {
        startSessionButton.setOnClickListener { startSession() }
        stopSessionButton.setOnClickListener { stopSession() }
        submitSessionButton.setOnClickListener { validateAndSubmitSession() }
        uploadProofButton.setOnClickListener { getFile.launch("*/*") }

        timeSlider.addOnChangeListener { _, value, _ ->
            sessionDuration = TimeUnit.MINUTES.toMillis(value.toLong())
            updateSelectedTimeDisplay()
            sessionRef.child("sessionDuration").setValue(sessionDuration)
        }
    }

    private fun setupRealTimeSync() {
        sessionRef.child("status").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                when (snapshot.getValue(String::class.java)) {
                    "started" -> {
                        isRunning = true
                        startTime = snapshot.child("startTime").getValue(Long::class.java) ?: System.currentTimeMillis()
                        startSessionButton.isEnabled = false
                        stopSessionButton.isEnabled = true
                        handler.post(updateTimer)
                    }
                    "stopped" -> stopSession()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error syncing session", Toast.LENGTH_SHORT).show()
            }
        })

        sessionRef.child("sessionDuration").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sessionDuration = snapshot.getValue(Long::class.java) ?: sessionDuration
                updateSelectedTimeDisplay()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error syncing session duration", Toast.LENGTH_SHORT).show()
            }
        })

        sessionRef.child("progress").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                sessionProgress.progress = snapshot.getValue(Int::class.java) ?: 0
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error syncing progress", Toast.LENGTH_SHORT).show()
            }
        })

        sessionRef.child("elapsedTime").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                updateTimerDisplay(snapshot.getValue(Long::class.java) ?: 0L)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Error syncing elapsed time", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun startSession() {
        sessionStatus.text = "Running"
        startTime = System.currentTimeMillis()
        isRunning = true
        handler.post(updateTimer)

        sessionRef.child("status").setValue("started")
        sessionRef.child("startTime").setValue(startTime)
        startSessionButton.isEnabled = false
        stopSessionButton.isEnabled = true
        showSessionNotification("Session Started", "Your tutor has started the session.")
    }

    private fun stopSession() {
        isRunning = false
        handler.removeCallbacks(updateTimer)

        sessionStatus.text = "Stopped"
        stopSessionButton.isEnabled = false
        submitSessionButton.isEnabled = true
        sessionRef.child("status").setValue("stopped")
        showSessionNotification("Session Stopped", "Your tutor has stopped the session.")
    }

    private fun validateAndSubmitSession() {
        val notes = notesInput.text.toString()
        if (notes.split("\\s+".toRegex()).size < 80) {
            Toast.makeText(requireContext(), "Please enter at least 80 words for session notes", Toast.LENGTH_SHORT).show()
            return
        }
        uploadProofOfWork { fileUrl -> submitSession(fileUrl) }
    }

    private fun submitSession(fileUrl: String?) {
        val sessionData = mapOf(
            "startTime" to startTime,
            "duration" to sessionDuration,
            "status" to "completed",
            "notes" to notesInput.text.toString(),
            "comment" to commentInput.text.toString(),
            "fileUrl" to fileUrl,
            "elapsedTime" to (System.currentTimeMillis() - startTime)
        )

        firestore.collection("sessions").document(sessionId)
            .set(sessionData, SetOptions.merge())
            .addOnSuccessListener { Toast.makeText(requireContext(), "Session submitted successfully", Toast.LENGTH_SHORT).show(); resetSession() }
            .addOnFailureListener { Toast.makeText(requireContext(), "Failed to submit session", Toast.LENGTH_SHORT).show() }
    }

    private fun uploadProofOfWork(onUploadComplete: (String?) -> Unit) {
        selectedFileUri?.let { uri ->
            val storageRef = storage.reference.child("proof_of_work/${System.currentTimeMillis()}")
            storageRef.putFile(uri)
                .addOnSuccessListener { it.storage.downloadUrl.addOnSuccessListener { onUploadComplete(it.toString()) } }
                .addOnFailureListener { Toast.makeText(requireContext(), "Failed to upload file", Toast.LENGTH_SHORT).show(); onUploadComplete(null) }
        } ?: onUploadComplete(null)
    }

    private val updateTimer = object : Runnable {
        override fun run() {
            if (isRunning) {
                val elapsedMillis = System.currentTimeMillis() - startTime
                timerDisplay.text = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(elapsedMillis),
                    TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) % 60,
                    TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60
                )
                sessionProgress.progress = ((elapsedMillis * 100) / sessionDuration).toInt()
                sessionRef.child("progress").setValue(sessionProgress.progress)
                sessionRef.child("elapsedTime").setValue(elapsedMillis)
                handler.postDelayed(this, 1000)
            }
        }
    }

    private fun updateTimerDisplay(elapsedMillis: Long) {
        timerDisplay.text = String.format("%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toHours(elapsedMillis),
            TimeUnit.MILLISECONDS.toMinutes(elapsedMillis) % 60,
            TimeUnit.MILLISECONDS.toSeconds(elapsedMillis) % 60
        )
    }

    private fun updateSelectedTimeDisplay() {
        selectedTimeDisplay.text = "${TimeUnit.MILLISECONDS.toMinutes(sessionDuration)} mins"
    }

    private fun resetSession() {
        isRunning = false
        handler.removeCallbacks(updateTimer)
        sessionStatus.text = "Ready"
        startSessionButton.isEnabled = true
        stopSessionButton.isEnabled = false
        submitSessionButton.isEnabled = false
        addMoreTimeButton.isEnabled = false
        timerDisplay.text = "00:00:00"
        sessionProgress.progress = 0
        notesInput.text.clear()
        commentInput.text.clear()
    }

    private fun showSessionNotification(title: String, message: String) {
        context?.let { ctx ->
            val channelId = "session_notifications"
            val notificationId = (0..9999).random()

            val notificationBuilder = NotificationCompat.Builder(ctx, channelId)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(ctx)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(channelId, "Session Notifications", NotificationManager.IMPORTANCE_HIGH).apply {
                    description = "Notifications for session updates"
                    enableLights(true)
                    enableVibration(true)
                }
                notificationManager.createNotificationChannel(channel)
            }

            if (ActivityCompat.checkSelfPermission(ctx, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) return
            notificationManager.notify(notificationId, notificationBuilder.build())
        }
    }
}
