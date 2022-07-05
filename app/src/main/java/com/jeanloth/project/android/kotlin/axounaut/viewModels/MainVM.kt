package com.jeanloth.project.android.kotlin.axounaut.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainVM(
    private val observeCommandsUseCase: ObserveCommandsUseCase,
    private val observeArticlesUseCase: ObserveArticlesUseCase,
): ViewModel() {

    private val headerTitleMutableLiveData = MutableLiveData<Pair<String, String>>()
    fun headerTitleLiveData(): LiveData<Pair<String, String>> = headerTitleMutableLiveData

    private var allCommandCountMutableLiveData : MutableLiveData<Int> = MutableLiveData(0)
    fun allCommandCountLiveData() : LiveData<Int> = allCommandCountMutableLiveData

    private var caMutableLiveData : MutableLiveData<Int> = MutableLiveData(0)
    fun caLiveData() : LiveData<Int> = caMutableLiveData

    private var unPayedCommandSumMutableLiveData : MutableLiveData<Int> = MutableLiveData(0)
    fun unPayedCommandSumLiveData() : LiveData<Int> = unPayedCommandSumMutableLiveData

    private var allArticleMutableLiveData : MutableLiveData<List<Article>> = MutableLiveData(emptyList())
    fun allArticleLiveData() : LiveData<List<Article>> = allArticleMutableLiveData

    init {

        viewModelScope.launch {
            observeCommandsUseCase.invoke().collect {
                allCommandCountMutableLiveData.postValue(it.count())
                caMutableLiveData.postValue(it.filter { it.statusCode in listOf(CommandStatusType.PAYED.code, CommandStatusType.INCOMPLETE_PAYMENT.code) }.map { it.paymentAmount }.sum())
                unPayedCommandSumMutableLiveData.postValue(it.filter { it.statusCode == CommandStatusType.INCOMPLETE_PAYMENT.code }.map { it.dueAmount}.sum())
            }
        }

        viewModelScope.launch {
            observeArticlesUseCase.invoke().collect {
                allArticleMutableLiveData.postValue(it)
            }
        }
    }

    fun setHeaderTitle(title : String, subtitle : String = "") {
        headerTitleMutableLiveData.value = title to subtitle
    }

}