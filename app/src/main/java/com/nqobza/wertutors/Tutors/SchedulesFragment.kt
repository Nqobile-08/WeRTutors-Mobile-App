package com.nqobza.wertutors.Tutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nqobza.wertutors.R

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SchedulesFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var appointmentEditText: EditText
    private lateinit var addButton: Button
    private lateinit var removeButton: Button
    private lateinit var appointmentsTextView: TextView

    private val appointments = mutableMapOf<String, String>() // Store appointments by date

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedules, container, false)

        calendarView = view.findViewById(R.id.calendarView)
        appointmentEditText = view.findViewById(R.id.appointmentEditText)
        addButton = view.findViewById(R.id.addButton)
        removeButton = view.findViewById(R.id.removeButton)
        appointmentsTextView = view.findViewById(R.id.appointmentsTextView)

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = dateFormat.format(Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }.time)

            // Display existing appointment for selected date
            val existingAppointment = appointments[selectedDate]
            appointmentEditText.setText(existingAppointment)
        }

        addButton.setOnClickListener {
            val selectedDate = dateFormat.format(Calendar.getInstance().apply {
                timeInMillis = calendarView.date
            }.time)

            val appointment = appointmentEditText.text.toString()
            if (appointment.isNotBlank()) {
                appointments[selectedDate] = appointment
                Toast.makeText(requireContext(), "Appointment added for $selectedDate", Toast.LENGTH_SHORT).show()
                updateAppointmentsTextView()
            } else {
                Toast.makeText(requireContext(), "Appointment cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        removeButton.setOnClickListener {
            val selectedDate = dateFormat.format(Calendar.getInstance().apply {
                timeInMillis = calendarView.date
            }.time)

            if (appointments.containsKey(selectedDate)) {
                appointments.remove(selectedDate)
                appointmentEditText.setText("")
                Toast.makeText(requireContext(), "Appointment removed for $selectedDate", Toast.LENGTH_SHORT).show()
                updateAppointmentsTextView()
            } else {
                Toast.makeText(requireContext(), "No appointment found for $selectedDate", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }

    private fun updateAppointmentsTextView() {
        val appointmentsText = StringBuilder("Appointments:\n")
        for ((date, appointment) in appointments) {
            appointmentsText.append("$date: $appointment\n")
        }
        appointmentsTextView.text = appointmentsText.toString()
    }
}
