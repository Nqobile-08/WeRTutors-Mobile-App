package com.ratjatji.eskhathinitutors.Admin

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.ratjatji.eskhathinitutors.R
import com.ratjatji.eskhathinitutors.Tutors.MainActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class FcmToken : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed token: $token")
        sendRegistrationToServer(token)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "Message received from: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            val messageTitle = it.title ?: "New Message"
            val messageBody = it.body ?: "You have a new notification"
            generateNotification(messageTitle, messageBody)
        }
    }

    private fun sendRegistrationToServer(token: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://yourserver.com/") // Replace with your server URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(ApiService::class.java)
        val tokenRequest = TokenRequest(token)

        apiService.sendToken(tokenRequest).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Log.d(TAG, "Token sent successfully")
                } else {
                    Log.e(TAG, "Failed to send token to server: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Log.e(TAG, "Error sending token to server", t)
            }
        })
    }

    private fun generateNotification(title: String, message: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE
        )

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "Dark Mode"
        val channelName = "Dark Mode"

        // Create Notification Channel for Android O+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH).apply {
                description = "Channel for Eskhathini Tutors notifications"
            }
            notificationManager.createNotificationChannel(channel)
        }

        // Build and show notification
        val notification = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.notification) // Ensure this icon exists
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(0, notification)
    }

    companion object {
        private const val TAG = "FcmToken"
    }
}

