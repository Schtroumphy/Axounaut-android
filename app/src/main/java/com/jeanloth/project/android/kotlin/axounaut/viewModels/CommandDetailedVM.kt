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

class CommandDetailedVM (
    val currentCommandId: Long,
    private val observeCommandByIdUseCase: ObserveCommandByIdUseCase,
    private val saveCommandUseCase: SaveCommandUseCase,
    private val deleteCommandUseCase: DeleteCommandUseCase,
    private val deleteArticleWrapperUseCase: DeleteArticleWrapperUseCase,
    private val saveArticleWrapperUseCase: SaveArticleWrapperUseCase,
): ViewModel() {

    val TAG= "[Command Detailed VM]"

    var currentCommandMutableLiveData : MutableLiveData<Command?> = MutableLiveData()
    fun currentCommandLiveData() : LiveData<Command?> = currentCommandMutableLiveData

    var paymentReceivedMutableLiveData : MutableLiveData<Double> = MutableLiveData()
    fun paymentReceivedLiveData() : LiveData<Double> = paymentReceivedMutableLiveData

    init {
        Log.d(TAG, "TEST UI observeCurrentCommand launched ")
        viewModelScope.launch {
            if(currentCommandId != 0L) {
                observeCommandByIdUseCase.invoke(currentCommandId).collect {
                    Log.d(TAG, " Current AWs from command Id $currentCommandId observed : $it")
                    currentCommandMutableLiveData.postValue(it)
                }
            }
        }
    }

    fun saveCommand(command: Command){
        saveCommandUseCase.invoke(command)
    }

    fun removeCommand(){
        deleteCommandUseCase.invoke(currentCommandMutableLiveData.value!!)
    }

    fun saveArticleWrapper(articleWrapper : ArticleWrapper) {
        saveArticleWrapperUseCase.invoke(articleWrapper)
    }

    fun setPaymentReceived(price : Double){
        paymentReceivedMutableLiveData.postValue(price)
    }

    fun updateStatusCommand(status: CommandStatusType) {
        Log.d(TAG, "Make command $status")
        saveCommandUseCase.updateCommandStatus(currentCommandMutableLiveData.value!!, status)
    }

    fun deleteArticleWrapperFromCurrentCommand(articleWrapper: ArticleWrapper) {
        deleteArticleWrapperUseCase.invoke(currentCommandMutableLiveData.value!!.articleWrappers.find { it == articleWrapper }!!)
    }

    fun updateCommandStatus(statusCode: Int) {
        val command = currentCommandMutableLiveData.value!!
        when (statusCode) {
            CommandStatusType.TO_DO.code -> {
                Log.d(TAG, "TODO")
                if (command.articleWrappers.any { it.statusCode == ArticleWrapperStatusType.DONE.code || it.statusCode == ArticleWrapperStatusType.CANCELED.code }) {
                    updateStatusCommand(CommandStatusType.IN_PROGRESS)
                }
            }
            CommandStatusType.IN_PROGRESS.code -> {
                Log.d(TAG, "In progress")
                if (command.articleWrappers.notCanceled().all { it.statusCode == ArticleWrapperStatusType.DONE.code}) {
                    updateStatusCommand(CommandStatusType.DONE)
                } else if (command.articleWrappers.all { it.statusCode == ArticleWrapperStatusType.CANCELED.code }) {
                    // TODO display dialog to canceled or delete command
                }
            }
            CommandStatusType.DONE.code -> {
                Log.d(TAG, "Terminé")
                if (!command.articleWrappers.notCanceled().all { it.statusCode == ArticleWrapperStatusType.DONE.code}) {
                    updateStatusCommand(CommandStatusType.IN_PROGRESS)
                }
            }
            CommandStatusType.DELIVERED.code -> {
                Log.d(TAG, "Livré")
            }
            CommandStatusType.PAYED.code -> {
                Log.d(TAG, "Payé")
            }
            CommandStatusType.CANCELED.code -> {
                Log.d(TAG, "Cancel")
            }
        }

    }

}