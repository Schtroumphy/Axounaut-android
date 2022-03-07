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

    var currentCommand : Command? = null

    var paymentReceivedMutableLiveData : MutableLiveData<Double> = MutableLiveData()
    fun paymentReceivedLiveData() : LiveData<Double> = paymentReceivedMutableLiveData

    init {
        Log.d(TAG, "TEST UI observeCurrentCommand launched ")
        viewModelScope.launch {
            if(currentCommandId != 0L) {
                observeCommandByIdUseCase.invoke(currentCommandId).collect { command ->
                    Log.d(TAG, " Current AWs from command Id $currentCommandId observed : $command")
                    Log.d(TAG, " Current command $currentCommandId AWS codes : ${command?.articleWrappers?.map { it.statusCode }}")
                    currentCommand = command
                    if(currentCommand?.statusCode != CommandStatusType.DONE.code && currentCommand?.articleWrappers?.map { it.statusCode }?.all { it in listOf(CommandStatusType.DONE.code, CommandStatusType.CANCELED.code) } == true){
                        updateStatusCommand(CommandStatusType.DONE)
                        return@collect
                    } else if( currentCommand?.statusCode != CommandStatusType.TO_DO.code && currentCommand?.articleWrappers?.map { it.statusCode }?.any { it in listOf(CommandStatusType.TO_DO.code) } == true){
                        updateStatusCommand(CommandStatusType.TO_DO)
                        return@collect
                    } else if( currentCommand?.statusCode != CommandStatusType.IN_PROGRESS.code && currentCommand?.articleWrappers?.map { it.statusCode }?.any { it in listOf(CommandStatusType.IN_PROGRESS.code) } == true){
                        updateStatusCommand(CommandStatusType.IN_PROGRESS)
                        return@collect
                    }
                    currentCommandMutableLiveData.postValue(command)
                }
            }
        }
    }
    fun removeCommand(){
        currentCommand?.let {
            deleteCommandUseCase.invoke(it)
        }
    }

    fun saveArticleWrapper(articleWrapper : ArticleWrapper) {
        saveArticleWrapperUseCase.invoke(articleWrapper)
    }

    fun setPaymentReceived(price : Double){
        paymentReceivedMutableLiveData.postValue(price)
    }

    fun updateStatusCommand(status: CommandStatusType) {
        Log.d(TAG, "Make command $status")
        currentCommand?.let { saveCommandUseCase.updateCommandStatus(it, status) }
    }

    fun deleteArticleWrapperFromCurrentCommand(articleWrapper: ArticleWrapper) {
        deleteArticleWrapperUseCase.invoke(currentCommandMutableLiveData.value!!.articleWrappers.find { it == articleWrapper }!!)
    }

}