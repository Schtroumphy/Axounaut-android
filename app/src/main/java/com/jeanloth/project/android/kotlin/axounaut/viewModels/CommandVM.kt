package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.GetAllCommandsUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveCommandUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class CommandVM (
    private val observeCommandsUseCase: ObserveCommandsUseCase,
    private val saveCommandUseCase: SaveCommandUseCase,
): ViewModel() {

    var commands : List<Command> = emptyList()

    var allCommandMutableLiveData : MutableLiveData<List<Command>> = MutableLiveData(emptyList())
    fun allCommandsLiveData() : LiveData<List<Command>> = allCommandMutableLiveData

    init {

        viewModelScope.launch {
            observeCommandsUseCase.invoke().collect {
                Log.d("[ArticleVM]", " Articles observed : $it")
                allCommandMutableLiveData.postValue(it)
            }
        }
    }

    /*fun getAllArticles(): List<Article> {
        articles = getAllArticlesUseCase.invoke()
        Log.d("[ArticleVM]", " Articles : $articles")
        return articles
    }*/

    fun saveCommand(command: Command) {
        saveCommandUseCase.invoke(command)
    }

}