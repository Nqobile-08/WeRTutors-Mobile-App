package com.ratjatji.eskhathinitutors

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import java.util.Calendar

class ScheduleCalendarFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private var events: MutableMap<String, EventInfo> = mutableMapOf() // Changed to store EventInfo instead of just String

    // Data class to store event information
    data class EventInfo(
        val title: String,
        val description: String,
        val time: String
    )
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_calendar, container, false)

        calendarView = view.findViewById(R.id.calendar)

        val calendars: ArrayList<CalendarDay> = ArrayList()
        val calendar = Calendar.getInstance()

        // Set calendar to October 20, 2024 (month is 0-based, so 9 is October)
        calendar.set(2024, 9, 20)

        val calendarDay = CalendarDay(calendar)
        calendarDay.labelColor = R.color.red
        calendarDay.imageResource = R.drawable.ic_scheduled_session
        calendars.add(calendarDay)

        // Store event with more details
        val eventKey = String.format("%02d-%02d-%d", 20, 10, 2024)
        events[eventKey] = EventInfo(
            title = "Tutoring Session",
            description = "Session with King",
            time = "14:00 - 15:00"
        )

        calendarView.setCalendarDays(calendars)

        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                val day = String.format("%02d", calendarDay.calendar.get(Calendar.DAY_OF_MONTH))
                val month = String.format("%02d", calendarDay.calendar.get(Calendar.MONTH) + 1)
                val year = calendarDay.calendar.get(Calendar.YEAR)

                val eventKey = "$day-$month-$year"

                if (events.containsKey(eventKey)) {
                    showEventPopup(eventKey, "$day/$month/$year")
                }
            }
        })

        return view
    }
    private fun showEventPopup(eventKey: String, formattedDate: String) {
        context?.let { ctx ->
            val dialog = Dialog(ctx)
            dialog.setContentView(R.layout.layout_event_popup)

            // Initialize views
            val tvEventTitle = dialog.findViewById<TextView>(R.id.tvEventTitle)
            val tvEventDate = dialog.findViewById<TextView>(R.id.tvEventDate)
            val tvEventDescription = dialog.findViewById<TextView>(R.id.tvEventDescription)
            val btnClose = dialog.findViewById<Button>(R.id.btnClose)

            // Get event details
            val eventInfo = events[eventKey]

            // Set the data
            eventInfo?.let {
                tvEventTitle.text = it.title
                tvEventDate.text = "Date: $formattedDate\nTime: ${it.time}"
                tvEventDescription.text = it.description
            }

            // Set click listener for close button
            btnClose.setOnClickListener {
                dialog.dismiss()
            }

            // Optional: Make the dialog width match parent with margins
            val window = dialog.window
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            dialog.show()
        }
    }
}