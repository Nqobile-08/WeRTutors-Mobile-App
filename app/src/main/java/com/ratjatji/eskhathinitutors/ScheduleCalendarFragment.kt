package com.ratjatji.eskhathinitutors

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.applandeo.materialcalendarview.CalendarDay
import com.applandeo.materialcalendarview.CalendarView
import com.applandeo.materialcalendarview.listeners.OnCalendarDayClickListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.Calendar

class ScheduleCalendarFragment : Fragment() {

    private lateinit var calendarView: CalendarView
    private var events: MutableMap<String, EventInfo> = mutableMapOf()
    private lateinit var auth: FirebaseAuth
    private val database = FirebaseDatabase.getInstance()

    data class EventInfo(
        val title: String,
        val description: String,
        val time: String,
        val subject: String,
        val sessionType: String,
        val tutor: String
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_schedule_calendar, container, false)
        auth = FirebaseAuth.getInstance()

        calendarView = view.findViewById(R.id.calendar)

        // Get current user's full name and fetch booking requests
        getCurrentUserFullName()

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

    private fun getCurrentUserFullName() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userRef = database.getReference("Users/Students/${currentUser.uid}")

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val firstName = snapshot.child("name").getValue(String::class.java) ?: ""
                    val surname = snapshot.child("surname").getValue(String::class.java) ?: ""
                    val fullName = "$firstName $surname"

                    // Now fetch booking requests using the full name
                    fetchBookingRequests(fullName)
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "Error loading user data: ${error.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }

    private fun fetchBookingRequests(studentFullName: String) {
        val bookingsRef = database.getReference("StudentBookingRequest").child(studentFullName)

        bookingsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                events.clear()
                val calendars = ArrayList<CalendarDay>()

                for (tutorSnapshot in snapshot.children) {
                    for (bookingSnapshot in tutorSnapshot.children) {
                        val date = bookingSnapshot.child("date").getValue(String::class.java) ?: continue
                        val time = bookingSnapshot.child("time").getValue(String::class.java) ?: continue
                        val subject = bookingSnapshot.child("subject").getValue(String::class.java) ?: continue
                        val sessionType = bookingSnapshot.child("sessionType").getValue(String::class.java) ?: continue
                        val tutorName = bookingSnapshot.child("tutor").getValue(String::class.java) ?: continue
                        val details = bookingSnapshot.child("details").getValue(String::class.java) ?: ""

                        // Parse the date (assuming format is DD/MM/YYYY)
                        val dateParts = date.split("/")
                        if (dateParts.size == 3) {
                            val day = dateParts[0].toInt()
                            val month = dateParts[1].toInt() - 1 // Calendar months are 0-based
                            val year = dateParts[2].toInt()

                            val calendar = Calendar.getInstance()
                            calendar.set(year, month, day)

                            val calendarDay = CalendarDay(calendar)
                            calendarDay.labelColor = R.color.red

                            calendars.add(calendarDay)

                            // Store event details
                            val eventKey = String.format("%02d-%02d-%d", day, month + 1, year)
                            events[eventKey] = EventInfo(
                                title = "Tutoring Session",
                                description = details,
                                time = time,
                                subject = subject,
                                sessionType = sessionType,
                                tutor = tutorName
                            )
                        }
                    }
                }

                // Update calendar with all events
                calendarView.setCalendarDays(calendars)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Error loading bookings: ${error.message}", Toast.LENGTH_LONG).show()
            }
        })
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
                tvEventTitle.text = "${it.title} - ${it.subject}"
                tvEventDate.text = """
                    Date: $formattedDate
                    Time: ${it.time}
                    Session Type: ${it.sessionType}
                    Tutor: ${it.tutor}
                """.trimIndent()
                tvEventDescription.text = it.description
            }

            btnClose.setOnClickListener {
                dialog.dismiss()
            }

            val window = dialog.window
            window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            dialog.show()
        }
    }
}