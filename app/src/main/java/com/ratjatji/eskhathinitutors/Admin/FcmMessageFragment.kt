package com.ratjatji.eskhathinitutors.Admin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.ratjatji.eskhathinitutors.R
import com.ratjatji.eskhathinitutors.Tutors.MainActivity

const val CHANNEL_ID = "com.ratjatji.eskhathinitutors"
const val CHANNEL_NAME = "Eskhathini Tutors"

class FcmMessageFragment : Fragment() {
    private lateinit var intent: Intent

    fun generateNotification(message: String) {
        val context = requireContext()

        // Create an Intent for the activity you want to start
        intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create Notification Channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for Eskhathini Tutors notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Build the notification
        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification) // Add a suitable icon here
            .setContentTitle("New Notification from We R Tutors")
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        // Show the notification
        notificationManager.notify(0, notification)
    }
}
