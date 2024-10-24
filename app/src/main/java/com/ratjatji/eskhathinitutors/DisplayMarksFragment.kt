package com.ratjatji.eskhathinitutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.db.williamchart.view.BarChartView
import com.db.williamchart.view.LineChartView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*

data class Mark(
    val assessmentDate: String = "",
    val assessmentType: String = "",
    val category: String = "",
    val mark: Float = 0f,
    val subjectName: String = ""
)

class DisplayMarksFragment : Fragment() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var chartContainer: LinearLayout
    private val marksList = mutableListOf<Mark>()

    companion object {
        private const val Y_AXIS_MIN = 0f
        private const val Y_AXIS_MAX = 100f
        private const val Y_AXIS_STEP = 20f  // Will show steps of 20 (0, 20, 40, 60, 80, 100)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "View your progress"
        val view = inflater.inflate(R.layout.fragment_display_marks, container, false)

        // Initialize Firebase
        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("Marks")

        chartContainer = view.findViewById(R.id.chartContainer)


        // Fetch data from Firebase
        fetchMarksData()

        return view
    }

    private fun fetchMarksData() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            // Handle not logged in state
            return
        }

        dbRef.child(currentUser.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                marksList.clear()

                for (markSnapshot in snapshot.children) {
                    val mark = Mark(
                        assessmentDate = markSnapshot.child("assessmentDate").getValue(String::class.java) ?: "",
                        assessmentType = markSnapshot.child("assessmentType").getValue(String::class.java) ?: "",
                        category = markSnapshot.child("category").getValue(String::class.java) ?: "",
                        mark = (markSnapshot.child("mark").getValue(Long::class.java) ?: 0).toFloat(),
                        subjectName = markSnapshot.child("subjectName").getValue(String::class.java)?.toLowerCase() ?: ""
                    )
                    marksList.add(mark)
                }

                // Sort marks by date
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                marksList.sortBy {
                    try {
                        dateFormat.parse(it.assessmentDate)
                    } catch (e: Exception) {
                        Date(0) // Default to oldest date if parsing fails
                    }
                }

                // Update charts
                updateCharts()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }

    private fun updateCharts() {
        // Clear existing views
        chartContainer.removeAllViews()

        // Group marks by subjectName
        val groupedMarks = marksList.groupBy { it.subjectName }

        // Create bar chart for overall averages
        createBarChart(groupedMarks)

        // Create line charts for each subject
        createLineCharts(groupedMarks)
    }

    private fun createBarChart(groupedMarks: Map<String, List<Mark>>) {
        val barData = groupedMarks.map { (subject, marks) ->
            subject to marks.map { it.mark }.average().toFloat()
        }

        val barChartView = BarChartView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                700
            )
            val barColors = intArrayOf(
                android.graphics.Color.parseColor("#98FF98") // Mint green color
            )
            labelsFormatter = { String.format("%.1f%%", it) }
            labelsColor = android.graphics.Color.BLACK
            animation.duration = 1500L
            labelsFormatter = { String.format("%.1f%%", it) }
        }
        // Add heading for bar chart
        val overallHeading = TextView(requireContext()).apply {
            text = "Overall Subject Averages"
            textSize = 20f
            setPadding(16, 32, 16, 16)
        }
        chartContainer.addView(overallHeading)
        chartContainer.addView(barChartView)

        barChartView.show(barData)
    }
    private fun createLineCharts(groupedMarks: Map<String, List<Mark>>) {
        for ((subject, marks) in groupedMarks) {
            // Create subject heading
            val subjectHeading = TextView(requireContext()).apply {
                text = "${subject.capitalize()} progress "
                textSize = 18f
                setPadding(16, 32, 16, 16)
            }
            chartContainer.addView(subjectHeading)

            // Add statistics for this subject
            val statsView = TextView(requireContext()).apply {
                val average = marks.map { it.mark }.average()
                val highest = marks.maxOf { it.mark }
                val lowest = marks.minOf { it.mark }
                text = String.format(
                    "Average: %.1f%% | Highest: %.1f%% | Lowest: %.1f%%",
                    average, highest, lowest
                )
                textSize = 14f
                setPadding(16, 0, 16, 16)
            }
            chartContainer.addView(statsView)

            // Create line chart with standardized axis
            val lineChartView = LineChartView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    400
                )

                labelsFormatter = { String.format("%.1f%%", it) }
                labelsSize = 22f
                labelsColor = android.graphics.Color.BLACK
                // Enable grid lines for better readability
                // This will show the default grid lines (not configurable)
                animation.duration = 1500L
                labelsFormatter = { String.format("%.0f%%", it) }

                // Optional: Apply gradient for line chart fill
                gradientFillColors = intArrayOf(
                    android.graphics.Color.parseColor("#81D4FA"),
                    android.graphics.Color.TRANSPARENT
                )
            }

            // Prepare data for line chart
            val lineData = marks.map {
                it.assessmentDate.toMonthDay() to it.mark
            }

            chartContainer.addView(lineChartView)
            lineChartView.show(lineData)
        }
    }
    private fun String.toMonthDay(): String {
        val inputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM", Locale.getDefault())
        return try {
            val date = inputFormat.parse(this)
            outputFormat.format(date ?: Date())
        } catch (e: Exception) {
            this
        }
    }
}

