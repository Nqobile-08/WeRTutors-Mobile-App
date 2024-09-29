package com.nqobza.wetutors

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.nqobza.wertutors.R
import com.nqobza.wertutors.TutorBooking
import com.nqobza.wertutors.tutorOptions
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SchedulesFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private lateinit var appointmentEditText: EditText
    private lateinit var addButton: Button
    private lateinit var removeButton: Button
    private lateinit var appointmentsTextView: TextView

    private lateinit var tutorSpinner: Spinner
    private lateinit var subjectSpinner: Spinner
    private lateinit var timeSpinner: Spinner

    private val appointments = mutableMapOf<String, String>() // Store appointments by date
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    private var selectedTutor: TutorBooking? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedules, container, false)

        // Initialize views
        calendarView = view.findViewById(R.id.calendarView)
        appointmentEditText = view.findViewById(R.id.appointmentEditText)
        addButton = view.findViewById(R.id.addButton)
        removeButton = view.findViewById(R.id.removeButton)
        appointmentsTextView = view.findViewById(R.id.appointmentsTextView)

        tutorSpinner = view.findViewById(R.id.spinnerTutors)
        subjectSpinner = view.findViewById(R.id.spinnerSubjects)
        timeSpinner = view.findViewById(R.id.spinnerTimes)

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

    private fun setupTutorSpinner() {
        val tutorNames = tutorOptions.map { it.name }
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, tutorNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        tutorSpinner.adapter = adapter

        tutorSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectedTutor = tutorOptions[position]
                updateSubjectSpinner(selectedTutor!!.subjects)
                updateTimeSpinner(selectedTutor!!.availableTimeSlots)
                updateCalendarAvailability(selectedTutor!!.availableDays)
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

    private fun updateCalendarAvailability(availableDays: List<Int>) {
        // Disable days that are not available
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance().apply {
                set(year, month, dayOfMonth)
            }
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)

            if (!availableDays.contains(dayOfWeek)) {
                Toast.makeText(requireContext(), "This tutor is not available on this day", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addAppointment() {
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

    private fun removeAppointment() {
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

    private fun updateAppointmentsTextView() {
        val appointmentsText = StringBuilder("Appointments:\n")
        for ((date, appointment) in appointments) {
            appointmentsText.append("$date: $appointment\n")
        }
        appointmentsTextView.text = appointmentsText.toString()
    }
}
