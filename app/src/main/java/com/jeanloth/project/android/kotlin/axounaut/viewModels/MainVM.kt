package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.DeleteArticleUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.GetAllArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.SaveArticleUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainVM: ViewModel() {

    private val headerTitleMutableLiveData = MutableLiveData<Pair<String, String>>()
    fun headerTitleLiveData(): LiveData<Pair<String, String>> = headerTitleMutableLiveData

    init {
        headerTitleMutableLiveData.postValue("Kreyol Baker" to "")
    }

    fun setHeaderTitle(title : String, subtitle : String = "") {
        headerTitleMutableLiveData.value = title to subtitle
    }

}