package com.jeanloth.project.android.kotlin.axounaut.extensions

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.preference.PreferenceManager
import java.util.*

class AxounautApplication : Application() {
    override fun onCreate() {
        updateLanguage(this);
        super.onCreate();
    }

    fun updateLanguage(ctx : Context)
    {
        val prefs : SharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx)
        val lang : String? = prefs.getString("locale_override", "")
        updateLanguage(ctx, lang)
    }

    fun updateLanguage( ctx : Context, lang : String?)
    {
        val cfg = Configuration()
        if (!lang.isNullOrEmpty())
            cfg.locale = Locale(lang)
        else
            cfg.locale = Locale.FRENCH

        ctx.resources.updateConfiguration(cfg, null)
    }
}