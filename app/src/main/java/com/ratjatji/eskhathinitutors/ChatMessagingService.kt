package com.ratjatji.eskhathinitutors

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class ChatMessagingService : FirebaseMessagingService() {

    companion object {
        private const val TAG = "ChatMessagingService"
        private const val CHANNEL_ID = "chat_notifications"
        private const val NOTIFICATION_ID = 100
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)

        Log.d(TAG, "From: ${remoteMessage.from}")

        // Log data payload
        Log.d(TAG, "Message data payload: ${remoteMessage.data}")

        // Check if message contains data payload
        if (remoteMessage.data.isNotEmpty()) {
            val title = remoteMessage.data["title"]
            val message = remoteMessage.data["message"]
            val senderUid = remoteMessage.data["senderUid"]

            Log.d(TAG, "Title: $title")
            Log.d(TAG, "Message: $message")
            Log.d(TAG, "Sender UID: $senderUid")

            if (title != null && message != null) {
                showNotification(title, message, senderUid)
            } else {
                Log.e(TAG, "Required notification data missing")
            }
        }

        // Check if message contains notification payload
        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
            showNotification(it.title ?: "New Message", it.body ?: "", null)
        }
    }

    private fun showNotification(title: String, message: String, senderUid: String?) {
        try {
            val intent = Intent(this, commsAct::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                putExtra("uid", senderUid)
            }

            val pendingIntent = PendingIntent.getActivity(
                this,
                0,
                intent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
                } else {
                    PendingIntent.FLAG_ONE_SHOT
                }
            )

            val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

            val notificationBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            // Create notification channel for Android O and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(
                    CHANNEL_ID,
                    "Chat Notifications",
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Notifications for new chat messages"
                    enableLights(true)
                    enableVibration(true)
                    setShowBadge(true)
                }
                notificationManager.createNotificationChannel(channel)
            }

            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            Log.d(TAG, "Notification displayed successfully")

        } catch (e: Exception) {
            Log.e(TAG, "Error showing notification", e)
        }
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        updateTokenInDatabase(token)
    }

    private fun updateTokenInDatabase(token: String) {
        try {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                FirebaseDatabase.getInstance().reference
                    .child("users")
                    .child(userId)
                    .child("fcmToken")
                    .setValue(token)
                    .addOnSuccessListener {
                        Log.d(TAG, "Token updated successfully")
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Failed to update token", e)
                    }
            } else {
                Log.e(TAG, "User not logged in, cannot update token")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating token in database", e)
        }
    }
}