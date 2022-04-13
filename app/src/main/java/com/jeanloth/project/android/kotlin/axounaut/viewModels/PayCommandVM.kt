package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.*
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveCommandByIdUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.command.SaveCommandUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import androidx.lifecycle.MediatorLiveData
import com.jeanloth.project.android.kotlin.axounaut.R
import com.jeanloth.project.android.kotlin.domain_models.entities.*

class PayCommandVM(
    val commandId: Long? = null,
    private val observeCommandByIdUseCase: ObserveCommandByIdUseCase,
    private val saveCommandUseCase: SaveCommandUseCase
) : ViewModel() {

    var commands: List<Command> = emptyList()
    var currentCommand: Command? = null
    var totalPrice: Int = 0
    var reductionApplicated: Int = 0

    val TAG = "[Command VM]"

    enum class PaymentStatus {
        COMPLETE,
        UNCOMPLETE,
        SUPERIOR,
        UNKONWN
    }

    private var currentCommandMutableLiveData: MutableLiveData<Command?> = MutableLiveData()
    fun currentCommandLiveData(): LiveData<Command?> = currentCommandMutableLiveData

    private var totalPriceWithReductionMutableLiveData: MutableLiveData<Int> = MutableLiveData(0)
    fun totalPriceWithReductionLiveData(): LiveData<Int> = totalPriceWithReductionMutableLiveData

    private var statusAndPaymentReceivedMutableLiveData: MutableLiveData<Pair<PaymentStatus, Int>> =
        MutableLiveData(PaymentStatus.UNKONWN to 0)

    fun statusAndPaymentReceivedLiveData(): LiveData<Pair<PaymentStatus, Int>> = statusAndPaymentReceivedMutableLiveData

    private var paymentReceivedMutableLiveData: MutableLiveData<Int> = MutableLiveData(0)
    fun paymentReceivedLiveData(): LiveData<Int> = paymentReceivedMutableLiveData

    val reductionAndPaymentReceivedMediatorLiveData = MediatorLiveData<List<Command>>()

    init {
        viewModelScope.launch {
            commandId?.let {
                observeCommandByIdUseCase.invoke(it).collect { command ->
                    currentCommand = command
                    currentCommandMutableLiveData.postValue(command)
                }
            }
        }

    }

    fun saveCommand() {
        currentCommand?.apply {
            paymentAmount = paymentReceivedLiveData().value
            reduction = reductionApplicated
            statusCode = when(statusAndPaymentReceivedMutableLiveData.value?.first){
                PaymentStatus.COMPLETE -> CommandStatusType.PAYED.code
                PaymentStatus.UNCOMPLETE -> CommandStatusType.INCOMPLETE_PAYMENT.code
                PaymentStatus.SUPERIOR -> {
                    CommandStatusType.DELIVERED.code
                    Log.e(TAG, "Status received is SUPERIOR. So the command is still delivered.")
                }
                else -> {
                    CommandStatusType.DELIVERED.code
                    Log.e(TAG, "Status received is NULL or status is not monitored. So the command is still delivered.")
                }
            }
        }
        currentCommand?.let{ saveCommandUseCase.invoke(it)}
    }

    fun setReduction(reduction: Int) {
        reductionApplicated = reduction
        totalPriceWithReductionMutableLiveData.postValue(totalPrice - reduction)
    }

    fun setCurrentTotalPrice(price: Int) {
        totalPrice = price
        setReduction(0) // Init totalPriceWithReductionMutableLiveData
    }

    fun setPaymentComplete() {
        statusAndPaymentReceivedMutableLiveData.postValue(PaymentStatus.COMPLETE to totalPrice)
        paymentReceivedMutableLiveData.postValue(totalPrice)
    }

    fun setPaymentReceived(price: Int) {
        var status =
            if (price > totalPriceWithReductionLiveData().value ?: 0) {
                PaymentStatus.SUPERIOR
            } else if (price < totalPriceWithReductionLiveData().value ?: 0) {
                PaymentStatus.UNCOMPLETE
            } else {
                PaymentStatus.COMPLETE
            }
        statusAndPaymentReceivedMutableLiveData.postValue(status to totalPrice)
        paymentReceivedMutableLiveData.postValue(price)
    }

    fun updateStatusCommand(status: CommandStatusType) {
        Log.d(TAG, "Make command $status")
        saveCommandUseCase.updateCommandStatus(currentCommand!!, status)
    }
}