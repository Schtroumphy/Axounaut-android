package com.jeanloth.project.android.kotlin.axounaut.workers

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

object AxounautScheduler {

    const val TAG = "AxounautScheduler"
    const val TAG_NOTIFICATION_WORKER = "NOTIFICATION_WORKER"
    const val INTERVAL_TIMER = 15L

    /** Launch notification worker **/
    fun launchNotificationWorker(context : Context){
        try {
            val constraints = Constraints.Builder().build()

            val request = PeriodicWorkRequest.Builder(NotificationWorker::class.java, INTERVAL_TIMER, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .addTag(TAG_NOTIFICATION_WORKER)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                TAG_NOTIFICATION_WORKER,
                ExistingPeriodicWorkPolicy.KEEP,
                request
            )
        } catch (ex: Exception){
            Log.e(TAG, "Erreur lors du lancement du scheduler - $TAG_NOTIFICATION_WORKER")
        }
    }
}