package com.jeanloth.project.android.kotlin.axounaut.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.jeanloth.project.android.kotlin.axounaut.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}