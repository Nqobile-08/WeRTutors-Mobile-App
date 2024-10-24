package com.ratjatji.eskhathinitutors

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.applandeo.materialcalendarview.listeners.OnCalendarPageChangeListener
import java.util.Calendar

class ScheduleCalendarFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private var events: MutableMap<String, String> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_schedule_calendar, container, false)

        // Find view by ID using the inflated 'view'
        calendarView = view.findViewById(R.id.calendar)

        val calendars: ArrayList<CalendarDay> = ArrayList()
        val calendar = Calendar.getInstance()

        calendar.set(2024, 10, 20)

        val calendarDay = CalendarDay(calendar)
        calendarDay.labelColor = R.color.red
        calendarDay.imageResource = R.drawable.ic_scheduled_session
        calendars.add(calendarDay)
        events["20-10-2024"] = "Session with King"

        calendarView.setCalendarDays(calendars)

        // Handle calendar day click event
        calendarView.setOnCalendarDayClickListener(object : OnCalendarDayClickListener {
            override fun onClick(calendarDay: CalendarDay) {
                val day = String.format("%02d", calendarDay.calendar.get(Calendar.DAY_OF_MONTH))
                val month = String.format("%02d", calendarDay.calendar.get(Calendar.MONTH) + 1) // Months are 0-indexed
                val year = calendarDay.calendar.get(Calendar.YEAR)

                val eventKey = "$day-$month-$year"
                if (events.containsKey(eventKey)) {
                    Toast.makeText(requireContext(), events[eventKey], Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(requireContext(), "Nothing to show", Toast.LENGTH_LONG).show()
                }
            }
        })

        // Handle previous page change event
        calendarView.setOnPreviousPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                val month = String.format("%02d", calendarView.currentPageDate.get(Calendar.MONTH) + 1) // Adjusted for 0-indexing
                val year = calendarView.currentPageDate.get(Calendar.YEAR)
                Toast.makeText(requireContext(), "$month/$year", Toast.LENGTH_LONG).show()
            }
        })

        // Handle forward page change event
        calendarView.setOnForwardPageChangeListener(object : OnCalendarPageChangeListener {
            override fun onChange() {
                val month = String.format("%02d", calendarView.currentPageDate.get(Calendar.MONTH) + 1) // Adjusted for 0-indexing
                val year = calendarView.currentPageDate.get(Calendar.YEAR)
                Toast.makeText(requireContext(), "$month/$year", Toast.LENGTH_LONG).show()
            }
        })

        return view
    }
}
