package com.ratjatji.eskhathinitutors

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

object NotificationHelper {

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channels = listOf(
                NotificationChannel(
                    "session_reminders",
                    "Session Reminders",
                    NotificationManager.IMPORTANCE_HIGH
                ),
                NotificationChannel(
                    "messages",
                    "Messages",
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                NotificationChannel(
                    "billing",
                    "Billing Notifications",
                    NotificationManager.IMPORTANCE_LOW
                ),
                NotificationChannel(
                    "progress_reports",
                    "Progress Reports",
                    NotificationManager.IMPORTANCE_DEFAULT
                ),
                NotificationChannel(
                    "startSessionButton",
                    "Sessionstarted",
                    NotificationManager.IMPORTANCE_DEFAULT
                ) ,
                NotificationChannel(
                    "LoggedOut",
                    "LoggedOut",
                    NotificationManager.IMPORTANCE_DEFAULT
                ) , NotificationChannel(
                    "Dark Mode",
                    "Dark Mode",
                    NotificationManager.IMPORTANCE_DEFAULT
                ) ,
                NotificationChannel(
                    "Chat Message",
                    "Chat Message",
                    NotificationManager.IMPORTANCE_DEFAULT
                ) ,
                NotificationChannel(
                    "Chat Message",
                    "Chat Message",
                    NotificationManager.IMPORTANCE_DEFAULT
                ) ,
                NotificationChannel(
                    "Reviews",
                    "User Reviews",
                    NotificationManager.IMPORTANCE_DEFAULT
                ) ,
            )

            val notificationManager = context.getSystemService(NotificationManager::class.java)
            channels.forEach { channel ->
                notificationManager?.createNotificationChannel(channel)
            }
        }
    }

    fun sendNotification(context: Context, channelId: String, title: String, message: String) {
        // Step 1: Build the notification
        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.adnotifications)  // Step 2: Set your notification icon here (replace with your icon)
            .setContentTitle(title)                    // Step 3: Title of the notification
            .setContentText(message)                   // Step 4: The main text message
            .setPriority(NotificationCompat.PRIORITY_HIGH) // Step 5: Set the notification priority

        // Step 6: Use NotificationManager to send the notification
        with(NotificationManagerCompat.from(context)) {
            val notificationId =
                (0..9999).random()   // Generate a random ID for each notification to avoid replacing older ones
            if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                this.notify(notificationId, builder.build())   // Send the notification
            } else {
                // Handle the case where notifications are not enabled
                Toast.makeText(context, "Notifications are disabled", Toast.LENGTH_SHORT).show()
            }
        }
    }
}