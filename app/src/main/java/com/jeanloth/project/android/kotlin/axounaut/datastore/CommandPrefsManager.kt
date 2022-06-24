package com.jeanloth.project.android.kotlin.axounaut.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandDisplayMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CommandPrefsManager(val context: Context) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "command_prefs")

    companion object{
        val COMMAND_LAST_FILTER = stringPreferencesKey("COMMAND_LAST_FILTER")
    }

    suspend fun saveLastCommandFilter(mode: CommandDisplayMode){
        context.dataStore.edit { commandPrefs ->
            commandPrefs[COMMAND_LAST_FILTER] = mode.name
        }
    }

    fun getLastCommandFilter(): Flow<String> {
        val result = context.dataStore.data.map { commandPrefs ->
            // No type safety.
            commandPrefs[COMMAND_LAST_FILTER] ?: CommandDisplayMode.TO_COME.name
        }
        return result
    }

}