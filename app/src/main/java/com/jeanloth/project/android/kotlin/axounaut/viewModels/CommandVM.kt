package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.jeanloth.project.android.kotlin.axounaut.extensions.notCanceled
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.GetCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveArticleWrappersByCommandIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.SaveArticleUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteCommandUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveCommandUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import kotlinx.android.synthetic.main.fragment_command_detail.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.Exception

class CommandVM (
    private val observeCommandsUseCase: ObserveCommandsUseCase,
    private val observeCommandByIdUseCase: ObserveCommandByIdUseCase,
    private val saveCommandUseCase: SaveCommandUseCase,
    private val deleteCommandUseCase: DeleteCommandUseCase,
    private val deleteArticleWrapperUseCase: DeleteArticleWrapperUseCase,
    private val saveArticleWrapperUseCase: SaveArticleWrapperUseCase,
): ViewModel() {

    var commands : List<Command> = emptyList()
    var currentCommand : Command? = null
    var currentCommandId : Long = 0L

    var allCommandMutableLiveData : MutableLiveData<List<Command>> = MutableLiveData(emptyList())
    fun allCommandsLiveData() : LiveData<List<Command>> = allCommandMutableLiveData

    var currentCommandMutableLiveData : MutableLiveData<Command?> = MutableLiveData()
    fun currentCommandLiveData() : LiveData<Command?> = currentCommandMutableLiveData

    var currentTotalPriceMutableLiveData : MutableLiveData<Double> = MutableLiveData()
    fun currentTotalPriceLiveData() : LiveData<Double> = currentTotalPriceMutableLiveData

    var reductionMutableLiveData : MutableLiveData<Double> = MutableLiveData()

    init {

        viewModelScope.launch {
            observeCommandsUseCase.invoke().collect {
                allCommandMutableLiveData.postValue(it)
            }
        }

        observeCurrentCommand()
    }


    fun observeCurrentCommand(){
        viewModelScope.launch {
            if(currentCommandId != 0L) {
                try {
                    observeCommandByIdUseCase.invoke(currentCommandId).collect {
                        Log.d("[CommandVM]", " Current AWs from command Id $currentCommandId observed : $it")
                        currentCommandMutableLiveData.postValue(it)
                        currentCommand = it
                        setCurrentTotalPrice(it!!.totalPrice!!)

                        val realTotalPrice = it.articleWrappers.filter { it.statusCode != ArticleWrapperStatusType.CANCELED.code }.map { it.count * it.article.price }.sum()
                        if(it.articleWrappers.any { it.statusCode == ArticleWrapperStatusType.CANCELED.code } && it.totalPrice != realTotalPrice){
                            it.totalPrice= realTotalPrice
                            saveCommand(it)
                        }
                    }
                } catch (e:Exception){
                    Log.d("[CommandVM]", " Exception AWs from command Id $currentCommandId observed : $e")
                }
            }
        }
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

    fun setCurrentTotalPrice(price : Double){
        currentTotalPriceMutableLiveData.postValue(price)
    }

    fun updateStatusCommand(status: CommandStatusType) {
        Log.d("[CommandVM]", "Make command $status")
        saveCommandUseCase.updateCommandStatus(currentCommand!!, status)
    }

    fun deleteArticleWrapperFromCurrentCommand(articleWrapper: ArticleWrapper) {
        deleteArticleWrapperUseCase.invoke(currentCommand!!.articleWrappers.find { it == articleWrapper }!!)
    }

    fun updateStatusByStatus(statusCode: Int) {
        val command = currentCommand!!
        when (statusCode) {
            CommandStatusType.TO_DO.code -> {
                Log.d("[Command details]", "TODO")
                if (command.articleWrappers.any { it.statusCode == ArticleWrapperStatusType.DONE.code || it.statusCode == ArticleWrapperStatusType.CANCELED.code }) {
                    updateStatusCommand(CommandStatusType.IN_PROGRESS)
                }
            }
            CommandStatusType.IN_PROGRESS.code -> {
                Log.d("[Command details]", "In progress")
                if (command.articleWrappers.notCanceled().all { it.statusCode == ArticleWrapperStatusType.DONE.code}) {
                    updateStatusCommand(CommandStatusType.DONE)
                } else if (command.articleWrappers.all { it.statusCode == ArticleWrapperStatusType.CANCELED.code }) {
                    // TODO display dialog to canceled or delete command
                }
            }
            CommandStatusType.DONE.code -> {
                Log.d("[Command details]", "Terminé")
                if (!command.articleWrappers.notCanceled().all { it.statusCode == ArticleWrapperStatusType.DONE.code}) {
                    updateStatusCommand(CommandStatusType.IN_PROGRESS)
                }
            }
            CommandStatusType.DELIVERED.code -> {
                Log.d("[Command details]", "Livré")
            }
            CommandStatusType.PAYED.code -> {
                Log.d("[Command details]", "Payé")
            }
            CommandStatusType.CANCELED.code -> {
                Log.d("[Command details]", "Cancel")
            }
        }

    }

}