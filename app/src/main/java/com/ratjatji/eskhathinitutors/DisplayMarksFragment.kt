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

import java.text.SimpleDateFormat
import java.util.*

data class Mark(
    val assessmentDate: String,
    val assessmentType: String,
    val category: String,
    val mark: Float,
    val subjectName: String
)

class DisplayMarksFragment : Fragment() {

    private val marksList = listOf(
        Mark("04/01/2024", "Test", "Pythag", 65f, "maths"),
        Mark("04/17/2024", "Test", "Pythag", 72f, "maths"),
        Mark("04/10/2024", "Assignment", "Euclidean", 82f, "maths"),
        Mark("04/25/2024", "Assignment", "Euclidean", 1f, "maths"),
        Mark("04/06/2024", "Test", "Shakespeare", 85f, "english"),
        Mark("04/15/2024", "Assignment", "Poetry", 90f, "english"),
        Mark("04/15/2024", "Assignment", "Poetry", 90f, "drama"),
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().title = "View your progress"
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_display_marks, container, false)

        // Find the LinearLayout container where charts will be added
        val chartContainer = view.findViewById<LinearLayout>(R.id.chartContainer)

        // Group marks by subjectName
        val groupedMarks = marksList.groupBy { it.subjectName }

        // Prepare data for the BarChartView (subject name vs average mark)
        val barData = groupedMarks.map { (subject, marks) ->
            subject to marks.map { it.mark }.average().toFloat()
        }

        // Create and display the BarChartView for subject vs average mark
        val barChartView = BarChartView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                700
            )
        }
        chartContainer.addView(barChartView)

        // Show the bar data
        barChartView.show(barData)

        // Iterate through each subject group and plot their marks on a LineChartView
        for ((subject, marks) in groupedMarks) {
            // Create a LineChartView for each subject
            val lineChartView = LineChartView(requireContext())
            val lineData = marks.map { it.assessmentDate.toMonthDay() to it.mark }

            // Set up LineChartView
            lineChartView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                300
            )

            // Add a heading TextView for each subject
            val subjectHeading = TextView(requireContext()).apply {
                text = subject.capitalize()
                textSize = 18f
                setPadding(16, 16, 16, 0)
            }
            chartContainer.addView(subjectHeading)

            // Display data on the LineChartView
            lineChartView.show(lineData)
            chartContainer.addView(lineChartView)
        }

        return view
    }

    // Extension function to convert date to "MM/dd" format
    private fun String.toMonthDay(): String {
        val inputFormat = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MM/dd", Locale.getDefault())
        return try {
            val date = inputFormat.parse(this)
            outputFormat.format(date ?: "")
        } catch (e: Exception) {
            this // fallback to original string if parsing fails
        }
    }
}
