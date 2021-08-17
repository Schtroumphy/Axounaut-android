package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.GetCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveArticleWrappersByCommandIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteCommandUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveCommandUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class CommandVM (
    private val observeCommandsUseCase: ObserveCommandsUseCase,
    private val observeCommandByIdUseCase: ObserveCommandByIdUseCase,
    private val getCommandByIdUseCase: GetCommandByIdUseCase,
    private val observeArticleWrappersByCommandIdUseCase: ObserveArticleWrappersByCommandIdUseCase,
    private val saveCommandUseCase: SaveCommandUseCase,
    private val deleteCommandUseCase: DeleteCommandUseCase,
    private val saveArticleWrapperUseCase: SaveArticleWrapperUseCase,
): ViewModel() {

    var commands : List<Command> = emptyList()
    var currentCommand : Command? = null
    var currentCommandId : Long = 0L

    var allCommandMutableLiveData : MutableLiveData<List<Command>> = MutableLiveData(emptyList())
    fun allCommandsLiveData() : LiveData<List<Command>> = allCommandMutableLiveData

    var currentCommandMutableLiveData : MutableLiveData<Command?> = MutableLiveData()
    fun currentCommandLiveData() : LiveData<Command?> = currentCommandMutableLiveData

    var currentCommandAwsMutableLiveData : MutableLiveData<List<ArticleWrapper>?> = MutableLiveData()
    fun currentAWLiveData() : LiveData<List<ArticleWrapper>?> = currentCommandAwsMutableLiveData

    var commandAwsMediatorLiveData = MediatorLiveData<Any>()
    var updatedCommandMutableLiveData = MutableLiveData<Command>()
    fun updatedCommand() : LiveData<Command>  = updatedCommandMutableLiveData

    init {

        commandAwsMediatorLiveData.addSource(currentCommandAwsMutableLiveData){
            emitUpdatedCommand(null, it)
        }

        commandAwsMediatorLiveData.addSource(currentCommandMutableLiveData){
            emitUpdatedCommand(it, null)
        }

        viewModelScope.launch {
            observeCommandsUseCase.invoke().collect {
                allCommandMutableLiveData.postValue(it)
            }
        }

        viewModelScope.launch {
            if(currentCommandId != 0L) {
                try {
                    observeCommandByIdUseCase.invoke(currentCommandId).collect {
                        Log.d("[CommandVM]", " Current AWs from command Id $currentCommandId observed : $it")
                        currentCommandMutableLiveData.postValue(it)

                        if(it?.articleWrappers?.all { it.statusCode == ArticleWrapperStatusType.DONE.code} == true ){
                            updateStatusCommand(CommandStatusType.DONE)
                        } else {
                            if(it?.statusCode == CommandStatusType.DONE.code){
                                updateStatusCommand(CommandStatusType.TO_DO)
                            } else if(it?.statusCode == CommandStatusType.TO_DO.code){
                                updateStatusCommand(CommandStatusType.IN_PROGRESS)
                            }
                        }
                    }
                } catch (e:Exception){
                    Log.d("[CommandVM]", " Exception AWs from command Id $currentCommandId observed : $e")
                }
                try {
                    observeArticleWrappersByCommandIdUseCase.invoke(currentCommandId).collect {
                        Log.d("[CommandVM]", " Current AWs from command Id $currentCommandId observed : $it")
                        currentCommandAwsMutableLiveData.postValue(it)
                    }
                } catch (e:Exception){
                    Log.d("[CommandVM]", " Exception AWs from command Id $currentCommandId observed : $e")
                }
            }
        }
    }

    private fun emitUpdatedCommand(command: Command?, aws: List<ArticleWrapper>?) {
        command?.let {
            Log.d("[CommandVM]", " Current command UPDATED $command")
            updatedCommandMutableLiveData.postValue(command!!)
        }
        aws?.let {

            // Get command by id and emit it
            val commandGetted = getCommandByIdUseCase.invoke(aws.first().commandId)
            Log.d("[CommandVM]", " Current command UPDATED $commandGetted")
            updatedCommandMutableLiveData.postValue(commandGetted!!)
        }
    }

    fun observeCurrentCommand(){
        viewModelScope.launch {
            try {
                observeCommandByIdUseCase.invoke(currentCommandId).collect {
                    Log.d("[CommandVM]", " Current command from Id $currentCommandId observed : $it")
                    currentCommandMutableLiveData.postValue(it)
                    currentCommand = it
                }
            } catch (e:Exception){
                Log.d("[CommandVM]", " Exception command from Id $currentCommandId observed : $e")
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

    fun removeCommand(){
        deleteCommandUseCase.invoke(currentCommand!!)
    }

    fun saveArticleWrapper(articleWrapper : ArticleWrapper) {
        saveArticleWrapperUseCase.invoke(articleWrapper)
    }

    fun updateStatusCommand(status: CommandStatusType) {
        if(status == CommandStatusType.DELIVERED){
            // update all article status not done
            currentCommand!!.articleWrappers.filter { it.statusCode != ArticleWrapperStatusType.DONE.code }.forEach { it.statusCode = ArticleWrapperStatusType.CANCELED.code }
        }
        currentCommand!!.statusCode= status.code
        Log.d("[CommandVM]", "Make command $status - $currentCommand")
        saveCommand(currentCommand!!)
    }

}