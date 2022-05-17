 package com.jeanloth.project.android.kotlin.axounaut.application

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.jeanloth.project.android.kotlin.axounaut.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class AxounautApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)

        startKoin {
            androidContext(base!!)
            // declare modules
            modules(appModule)
        }
    }

    override fun onCreate() {
        super.onCreate()

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                "notification_channel", "channel for notifications", NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}