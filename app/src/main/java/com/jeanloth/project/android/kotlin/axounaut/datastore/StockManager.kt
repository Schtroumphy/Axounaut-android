package com.jeanloth.project.android.kotlin.axounaut.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

class StockManager(val context: Context) {

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "stock_prefs")

    companion object{
        val STOCK_LAST_UPDATE_DATE = stringPreferencesKey("STOCK_LAST_UPDATE_DATE")
    }

    suspend fun saveStockLastUpdateDate(date: String){
        context.dataStore.edit { stockPrefs ->
            stockPrefs[STOCK_LAST_UPDATE_DATE] = date
        }
    }


}