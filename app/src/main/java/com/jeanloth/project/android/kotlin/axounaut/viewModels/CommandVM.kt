package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.CommandListFragment
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteCommandUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveCommandUseCase
import kotlinx.coroutines.launch
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jeanloth.project.android.kotlin.axounaut.extensions.toLocalDate
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsByStatusUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.SaveArticleUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import kotlinx.coroutines.flow.*
import java.time.LocalDate

class CommandVM (
    private val observeCommandsUseCase: ObserveCommandsUseCase,
    private val observeCommandsByStatusUseCase: ObserveCommandsByStatusUseCase,
    private val saveCommandUseCase: SaveCommandUseCase,
    private val deleteCommandUseCase: DeleteCommandUseCase,
    private val deleteArticleWrapperUseCase: DeleteArticleWrapperUseCase,
    private val saveArticleWrapperUseCase: SaveArticleWrapperUseCase,
    private val saveArticleUseCase: SaveArticleUseCase
): ViewModel() {

    var commands : List<Command> = emptyList()
    var currentCommand : Command? = null

    val TAG= "[Command VM]"

    var allCommandMutableLiveData : MutableLiveData<List<Command>> = MutableLiveData(emptyList())
    var displayModeMutableLiveData : MutableLiveData<CommandListFragment.CommandDisplayMode> = MutableLiveData(CommandListFragment.CommandDisplayMode.IN_PROGRESS)
    var displayModeSF : MutableStateFlow<CommandListFragment.CommandDisplayMode> = MutableStateFlow(CommandListFragment.CommandDisplayMode.IN_PROGRESS)

    val commandToDisplayMediatorLiveData = MediatorLiveData<List<Command>>()
    var commandToDisplayMutableStateFlow : MutableStateFlow<List<Command>> = MutableStateFlow(
        emptyList())
    val commandToDisplayStateFlow : StateFlow<List<Command>> = commandToDisplayMutableStateFlow.asStateFlow()

    init {

        viewModelScope.launch {
            observeCommandsUseCase.invoke().shareIn(this, SharingStarted.Lazily).collect {
                allCommandMutableLiveData.postValue(it)
            }
        }

        viewModelScope.launch {
            observeCommandsUseCase.invoke().stateIn(this, SharingStarted.Lazily, emptyList()).combine(displayModeSF){ commands, mode ->

                val commandToDisplay: List<Command> = commands.filter { it.statusCode in mode.statusCode }.apply {
                    when (mode) {
                        CommandListFragment.CommandDisplayMode.TO_COME -> filter {
                            it.deliveryDate?.toLocalDate()!!.isAfter(LocalDate.now())
                        }
                        else -> this.sortedBy { it.deliveryDate?.toLocalDate() }
                    }
                }
                commandToDisplay
            }.collect {
                commandToDisplayMutableStateFlow.emit(it)
            }
        }

        commandToDisplayMediatorLiveData.addSource(allCommandMutableLiveData) { value: List<Command> ->
            commandToDisplayMediatorLiveData.setValue(value)
        }
        commandToDisplayMediatorLiveData.addSource(displayModeMutableLiveData) { value: CommandListFragment.CommandDisplayMode ->
            val commandToDisplay: List<Command>
            val commands = allCommandMutableLiveData.value ?: emptyList()

            commandToDisplay = commands.filter { it.statusCode in value.statusCode }.apply {
                when (value) {
                    CommandListFragment.CommandDisplayMode.TO_COME -> filter {
                        it.deliveryDate?.toLocalDate()!!.isAfter(LocalDate.now())
                    }
                    else -> this.sortedBy { it.deliveryDate?.toLocalDate() }
                }
            }
            commandToDisplayMediatorLiveData.setValue(commandToDisplay)
        }
    }

    fun setDisplayMode(displayMode: CommandListFragment.CommandDisplayMode) {
        displayModeMutableLiveData.value = displayMode
        viewModelScope.launch {
            displayModeSF.emit(displayMode)
        }
    }

    fun saveCommand(command: Command, isNewCommand : Boolean = false){
        saveCommandUseCase.invoke(command)

        if(isNewCommand){
            // Update time orderer for eachArticle
            command.articleWrappers.forEach {
                it.article.apply {
                    timeOrdered += 1
                }
                saveArticleUseCase.invoke(it.article)
            }
        }
    }

    fun removeCommand(){
        deleteCommandUseCase.invoke(currentCommand!!)
    }

    fun saveArticleWrapper(articleWrapper : ArticleWrapper) {
        saveArticleWrapperUseCase.invoke(articleWrapper)
    }

    fun updateStatusCommand(status: CommandStatusType) {
        Log.d(TAG, "Make command $status")
        saveCommandUseCase.updateCommandStatus(currentCommand!!, status)
    }

    fun deleteArticleWrapperFromCurrentCommand(articleWrapper: ArticleWrapper) {
        deleteArticleWrapperUseCase.invoke(currentCommand!!.articleWrappers.find { it == articleWrapper }!!)
    }
}