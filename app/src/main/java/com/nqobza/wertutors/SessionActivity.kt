import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.nqobza.wertutors.R

class SessionActivity : AppCompatActivity() {

    private lateinit var timerDisplay: TextView
    private lateinit var startSessionButton: Button
    private lateinit var stopSessionButton: Button
    private lateinit var submitSessionButton: Button
    private lateinit var notesInput: EditText

    private var startTime: Long = 0L
    private var running = false
    private var handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_session_activity)

        timerDisplay = findViewById(R.id.timerDisplay)
        startSessionButton = findViewById(R.id.startSessionButton)
        stopSessionButton = findViewById(R.id.stopSessionButton)
        submitSessionButton = findViewById(R.id.submitSessionButton)
        notesInput = findViewById(R.id.notesInput)

        startSessionButton.setOnClickListener {
            startSession()
        }

        stopSessionButton.setOnClickListener {
            stopSession()
        }

        submitSessionButton.setOnClickListener {
            submitSession()
        }
    }

    private fun startSession() {
        startTime = System.currentTimeMillis()
        running = true
        handler.post(updateTimer)
    }

    private val updateTimer = object : Runnable {
        override fun run() {
            if (running) {
                val elapsedTime = System.currentTimeMillis() - startTime
                val hours = (elapsedTime / (1000 * 60 * 60)).toInt()
                val minutes = ((elapsedTime / (1000 * 60)) % 60).toInt()
                val seconds = (elapsedTime / 1000 % 60).toInt()
                timerDisplay.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                handler.postDelayed(this, 1000)
            }
        }
    }

    private fun stopSession() {
        running = false
        handler.removeCallbacks(updateTimer)
    }

    private fun submitSession() {
        val notes = notesInput.text.toString()
        val elapsedTime = System.currentTimeMillis() - startTime
        val sessionSummary = "Session completed in $elapsedTime ms with notes: $notes"
        // Here you would send sessionSummary to the API
    }
}
