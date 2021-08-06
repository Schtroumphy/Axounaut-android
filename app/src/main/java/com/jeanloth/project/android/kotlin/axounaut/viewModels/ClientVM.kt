package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.DeleteClientsUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.ObserveClientUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.appClient.SaveClientUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.AppClient
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ClientVM (
    private val observeClientUseCase: ObserveClientUseCase,
    private val saveClientUseCase: SaveClientUseCase,
    private val deleteClientsUseCase: DeleteClientsUseCase,
): ViewModel() {

    var clients : List<AppClient> = emptyList()

    var allClientsMutableLiveData : MutableLiveData<List<AppClient>> = MutableLiveData(emptyList())
    fun allClientsLiveData() : LiveData<List<AppClient>> = allClientsMutableLiveData

    init {

        viewModelScope.launch {
            observeClientUseCase.invoke().collect {
                Log.d("[AppClientVM]", " Clients observed : $it")
                allClientsMutableLiveData.postValue(it)
            }
        }
    }

    fun saveClient(client: AppClient) {
        Log.d("[AppClientVM]", " Client to save : $client")
        saveClientUseCase.invoke(client)
    }

    fun deleteClients(clients : List<AppClient>) {
        deleteClientsUseCase.invoke(clients)
    }


}