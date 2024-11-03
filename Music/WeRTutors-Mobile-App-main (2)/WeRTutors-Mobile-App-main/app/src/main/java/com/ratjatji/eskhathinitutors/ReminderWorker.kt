package com.ratjatji.eskhathinitutors

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ratjatji.eskhathinitutors.NotificationHelper.sendNotification

class ReminderWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        // Trigger the notification
        sendNotification(
            context = applicationContext,
            channelId = "session_reminders",
            title = "Tutoring Reminder",
            message = "Your session starts in one hour."
        )
        Log.d("ReminderWorker", "Notification triggered successfully.")
        return Result.success()
    }
}
