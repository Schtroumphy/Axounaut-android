package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val observeCommandByIdUseCase: ObserveCommandByIdUseCase,
    private val saveCommandUseCase: SaveCommandUseCase,
    private val deleteCommandUseCase: DeleteCommandUseCase,
    private val saveArticleWrapperUseCase: SaveArticleWrapperUseCase,
): ViewModel() {

    var commands : List<Command> = emptyList()

    var allCommandMutableLiveData : MutableLiveData<List<Command>> = MutableLiveData(emptyList())
    fun allCommandsLiveData() : LiveData<List<Command>> = allCommandMutableLiveData

    var currentCommandMutableLiveData : MutableLiveData<Command?> = MutableLiveData()
    fun currentCommandLiveData() : LiveData<Command?> = currentCommandMutableLiveData

    init {

        viewModelScope.launch {
            observeCommandsUseCase.invoke().collect {
                //Log.d("[CommandVM]", "All Commands observed : $it")
                allCommandMutableLiveData.postValue(it)
            }
        }

        viewModelScope.launch {
            if(currentCommandId != 0L) {
                try {
                    observeCommandByIdUseCase.invoke(currentCommandId).collect {
                        Log.d("[CommandVM]", " Current command observed : $it")
                        currentCommandMutableLiveData.postValue(it)
                    }
                } catch (e:Exception){
                    Log.d("[CommandVM]", " Exception command observed by id : $e")
                }
            }
        }
    }

    fun saveCommand(command: Command){
        saveCommandUseCase.invoke(command)
    }

    fun removeCommand(command: Command){
        deleteCommandUseCase.invoke(command)
    }

    fun saveArticleWrapper(articleWrapper : ArticleWrapper) {
        saveArticleWrapperUseCase.invoke(articleWrapper)
    }

}