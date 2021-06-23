package com.jeanloth.project.android.kotlin.axounaut.application

import android.app.Application
import io.realm.Realm

class AxounautApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Realm.init(this)
    }
}