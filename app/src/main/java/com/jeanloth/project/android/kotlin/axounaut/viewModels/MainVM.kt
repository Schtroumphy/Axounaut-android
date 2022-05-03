package com.jeanloth.project.android.kotlin.axounaut.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.RecipeWrapper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.core.KoinApplication.Companion.init

class MainVM(
    val observeCommandsUseCase: ObserveCommandsUseCase,
    val observeArticlesUseCase: ObserveArticlesUseCase,
): ViewModel() {

    private val headerTitleMutableLiveData = MutableLiveData<Pair<String, String>>()
    fun headerTitleLiveData(): LiveData<Pair<String, String>> = headerTitleMutableLiveData

    private var allCommandMutableLiveData : MutableLiveData<List<Command>> = MutableLiveData(emptyList())
    fun allCommandsLiveData() : LiveData<List<Command>> = allCommandMutableLiveData

    private var allCommandCountMutableLiveData : MutableLiveData<Int> = MutableLiveData(0)
    fun allCommandCountLiveData() : LiveData<Int> = allCommandCountMutableLiveData

    private var caMutableLiveData : MutableLiveData<Int> = MutableLiveData(0)
    fun caLiveData() : LiveData<Int> = caMutableLiveData

    private var unPayedCommandSumMutableLiveData : MutableLiveData<Int> = MutableLiveData(0)
    fun unPayedCommandSumLiveData() : LiveData<Int> = unPayedCommandSumMutableLiveData

    private var allArticleMutableLiveData : MutableLiveData<List<Article>> = MutableLiveData(emptyList())
    fun allArticleLiveData() : LiveData<List<Article>> = allArticleMutableLiveData

    // TODO
    // Observe all command saved count
    // Observe all command payed sum
    // Observe all command unpayed sum

    // Observe all command articles

    init {

        viewModelScope.launch {
            observeCommandsUseCase.invoke().collect {
                allCommandMutableLiveData.postValue(it)
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