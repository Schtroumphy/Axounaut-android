package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveArticleWrappersByCommandIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteCommandUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveCommandUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class CommandVM (
    private val currentCommandId : Long = 0L,
    private val observeCommandsUseCase: ObserveCommandsUseCase,
    private val observeArticleWrappersByCommandIdUseCase: ObserveArticleWrappersByCommandIdUseCase,
    private val saveCommandUseCase: SaveCommandUseCase,
    private val deleteCommandUseCase: DeleteCommandUseCase,
    private val saveArticleWrapperUseCase: SaveArticleWrapperUseCase,
): ViewModel() {

    var commands : List<Command> = emptyList()

    var allCommandMutableLiveData : MutableLiveData<List<Command>> = MutableLiveData(emptyList())
    fun allCommandsLiveData() : LiveData<List<Command>> = allCommandMutableLiveData

    var currentCommandMutableLiveData : MutableLiveData<Command?> = MutableLiveData()
    fun currentCommandLiveData() : LiveData<Command?> = currentCommandMutableLiveData

    var currentAWMutableLiveData : MutableLiveData<List<ArticleWrapper>?> = MutableLiveData()
    fun currentAWLiveData() : LiveData<List<ArticleWrapper>?> = currentAWMutableLiveData

    init {

        viewModelScope.launch {
            observeCommandsUseCase.invoke().collect {
                allCommandMutableLiveData.postValue(it)
            }
        }

        viewModelScope.launch {
            if(currentCommandId != 0L) {
                try {
                    observeArticleWrappersByCommandIdUseCase.invoke(currentCommandId).collect {
                        Log.d("[CommandVM]", " Current AWs from command Id $currentCommandId observed : $it")
                        currentAWMutableLiveData.postValue(it)
                    }
                } catch (e:Exception){
                    Log.d("[CommandVM]", " Exception AWs from command Id $currentCommandId observed : $e")
                }
            }
        }
    }

    fun saveCommand(command: Command){
        val commandID = saveCommandUseCase.invoke(command)

        command.articleWrappers.forEach {
            it.commandId = commandID
            saveArticleWrapper(it)
        }
    }

    fun removeCommand(command: Command){
        deleteCommandUseCase.invoke(command)
    }

    fun saveArticleWrapper(articleWrapper : ArticleWrapper) {
        saveArticleWrapperUseCase.invoke(articleWrapper)
    }

}