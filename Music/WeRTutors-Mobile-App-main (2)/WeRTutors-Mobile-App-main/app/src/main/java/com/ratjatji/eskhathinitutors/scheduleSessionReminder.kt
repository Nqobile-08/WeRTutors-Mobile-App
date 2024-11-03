package com.ratjatji.eskhathinitutors

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import java.util.concurrent.TimeUnit

fun scheduleSessionReminder(context: Context, delayMinutes: Long) {
    // Step 1: Create a work request for ReminderWorker
    val reminderWorkRequest: WorkRequest = OneTimeWorkRequestBuilder<ReminderWorker>()
        .setInitialDelay(delayMinutes, TimeUnit.MINUTES) // Step 2: Specify delay before triggering
        .build()

    // Step 3: Enqueue the work request with WorkManager
    WorkManager.getInstance(context).enqueue(reminderWorkRequest)


}
