package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.DeleteCommandUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveArticleWrapperUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveCommandUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

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
        viewModelScope.launch {
            if(currentCommandId != 0L) {
                observeCommandByIdUseCase.invoke(currentCommandId).collect { command ->
                    currentCommand = command

                    if(command?.statusCode !in listOf(CommandStatusType.DELIVERED.code, CommandStatusType.PAYED.code, CommandStatusType.INCOMPLETE_PAYMENT.code)){
                        currentCommand?.let {
                            val status = getCommandStatusToUpdate(it)
                            if(it.statusCode != status.code) updateStatusCommand(status)
                        }
                    }
                    currentCommandMutableLiveData.postValue(command)
                }
            }
        }
    }

    fun getCommandStatusToUpdate(command : Command) : CommandStatusType{
        if(command.articleWrappers.map { it.statusCode }.all { it == ArticleWrapperStatusType.TO_DO.code }){
            return CommandStatusType.TO_DO
        } else if (command.articleWrappers.map { it.statusCode }.all { it  in listOf(ArticleWrapperStatusType.DONE.code,) }){
            return CommandStatusType.DONE
        } else {
            return CommandStatusType.IN_PROGRESS
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
        if(status == CommandStatusType.DELIVERED && currentCommand?.articleWrappers?.any { it.statusCode != ArticleWrapperStatusType.DONE.code } == true){
            currentCommand?.articleWrappers?.filter { it.statusCode != ArticleWrapperStatusType.DONE.code }?.forEach {
                it.statusCode = ArticleWrapperStatusType.CANCELED.code
            }
        }
        currentCommand?.let { saveCommandUseCase.updateCommandStatus(it, status) }
    }

    fun deleteArticleWrapperFromCurrentCommand(articleWrapper: ArticleWrapper) {
        deleteArticleWrapperUseCase.invoke(currentCommandMutableLiveData.value!!.articleWrappers.find { it == articleWrapper }!!)
    }

}