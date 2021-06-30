package com.jeanloth.project.android.kotlin.axounaut.application

import android.app.Application
import android.content.Context
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
}