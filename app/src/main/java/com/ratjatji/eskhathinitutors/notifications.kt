package com.ratjatji.eskhathinitutors

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.ratjatji.eskhathinitutors.Tutors.MainActivity


class NotificationsFragment : Fragment() {

    private lateinit var notificationManager: NotificationManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val sendNotificationButton = view.findViewById<Button>(R.id.sendNotificationButton)
        sendNotificationButton.setOnClickListener {
            sendNotification()
            val btnSendNotification = view.findViewById<Button>(R.id.sendNotificationButton)
            btnSendNotification.setOnClickListener {
                requestPermissionLauncher
            }
        }

        return view
    }

    private fun sendNotification() {
        val channelId = "notifications_channel"
        val channelName = "User Notifications"

        // Create notification channel if API level 26+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Channel for user notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Set up the notification content
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(requireContext(), channelId)
            .setSmallIcon(R.drawable.adnotifications)
            .setContentTitle("New Activity")
            .setContentText("You have new activity to check!")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        // Notify the user
        notificationManager.notify(1, notification)

        // For a simple feedback, you might want to show a toast as well
        Toast.makeText(requireContext(), "Notification sent!", Toast.LENGTH_SHORT).show()
    }
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(

        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("firebaselogs", "Fetching FCM registration token failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new FCM registration token
                val token = task.result
                Log.w("firebaselogs", "Token: $token")



            })
        } else {

        }

    }
}
