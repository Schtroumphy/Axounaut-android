package com.jeanloth.project.android.kotlin.axounaut.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainVM: ViewModel() {

    private val headerTitleMutableLiveData = MutableLiveData<Pair<String, String>>()
    fun headerTitleLiveData(): LiveData<Pair<String, String>> = headerTitleMutableLiveData

    fun setHeaderTitle(title : String, subtitle : String = "") {
        headerTitleMutableLiveData.value = title to subtitle
    }

}