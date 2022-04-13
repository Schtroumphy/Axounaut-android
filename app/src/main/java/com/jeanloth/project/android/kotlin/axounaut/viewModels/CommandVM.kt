package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.jeanloth.project.android.kotlin.axounaut.extensions.notCanceled
import com.jeanloth.project.android.kotlin.axounaut.ui.commands.CommandListFragment
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteCommandUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveCommandUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception
import androidx.lifecycle.MediatorLiveData
import com.jeanloth.project.android.kotlin.axounaut.extensions.toLocalDate
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsByStatusUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import java.time.LocalDate

class CommandVM (
    private val observeCommandsUseCase: ObserveCommandsUseCase,
    private val observeCommandsByStatusUseCase: ObserveCommandsByStatusUseCase,
    private val observeCommandByIdUseCase: ObserveCommandByIdUseCase,
    private val saveCommandUseCase: SaveCommandUseCase,
    private val deleteCommandUseCase: DeleteCommandUseCase,
    private val deleteArticleWrapperUseCase: DeleteArticleWrapperUseCase,
    private val saveArticleWrapperUseCase: SaveArticleWrapperUseCase,
): ViewModel() {

    var commands : List<Command> = emptyList()
    var currentCommand : Command? = null
    var currentCommandId : Long = 0L

    val TAG= "[Command VM]"

    var allCommandMutableLiveData : MutableLiveData<List<Command>> = MutableLiveData(emptyList())
    fun allCommandsLiveData() : LiveData<List<Command>> = allCommandMutableLiveData

    var allNotDeliveredCommandMutableLiveData : MutableLiveData<List<RecipeWrapper>> = MutableLiveData(emptyList()) // ToDo and InProgress commands
    fun allNotDeliveredCommandLiveData() : LiveData<List<RecipeWrapper>> = allNotDeliveredCommandMutableLiveData

    var currentCommandMutableLiveData : MutableLiveData<Command?> = MutableLiveData()
    fun currentCommandLiveData() : LiveData<Command?> = currentCommandMutableLiveData

    var paymentReceivedMutableLiveData : MutableLiveData<Double> = MutableLiveData()
    fun paymentReceivedLiveData() : LiveData<Double> = paymentReceivedMutableLiveData

    var displayModeMutableLiveData : MutableLiveData<CommandListFragment.CommandDisplayMode> = MutableLiveData(CommandListFragment.CommandDisplayMode.IN_PROGRESS)

    val commandToDisplayMediatorLiveData = MediatorLiveData<List<Command>>()

    init {

        viewModelScope.launch {
            observeCommandsUseCase.invoke().collect {
                allCommandMutableLiveData.postValue(it)
            }
        }

        viewModelScope.launch {
            observeCommandsByStatusUseCase.invoke(listOf(CommandStatusType.TO_DO, CommandStatusType.IN_PROGRESS)).collect {
                val list : List<RecipeWrapper> = it.map { it.articleWrappers.map { it.article }.map { it.recipeIngredients }.flatten() }.flatten()
                allNotDeliveredCommandMutableLiveData.postValue(list) // TODO map by recipe wrappers
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
    }

    fun saveCommand(command: Command){
        saveCommandUseCase.invoke(command)
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