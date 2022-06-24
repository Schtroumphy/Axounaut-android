package com.jeanloth.project.android.kotlin.axounaut.viewModels

import androidx.lifecycle.*
import com.jeanloth.project.android.kotlin.axounaut.datastore.CommandPrefsManager
import com.jeanloth.project.android.kotlin.axounaut.extensions.toLocalDate
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveCommandsUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandDisplayMode
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import java.time.LocalDate

class CommandVM (
    private val observeCommandsUseCase: ObserveCommandsUseCase,
    private val commandPrefsManager: CommandPrefsManager
    ): ViewModel() {

    val TAG = "[Command VM]"

    var allCommandMutableLiveData : MutableLiveData<List<Command>> = MutableLiveData(emptyList())
    var displayModeMutableLiveData : MutableLiveData<CommandDisplayMode> = MutableLiveData(CommandDisplayMode.IN_PROGRESS)
    val commandToDisplayMediatorLiveData = MediatorLiveData<List<Command>>()

    init {

        viewModelScope.launch {
            observeCommandsUseCase.invoke().shareIn(this, SharingStarted.Lazily).collect {
                allCommandMutableLiveData.postValue(it)
            }
        }

        commandToDisplayMediatorLiveData.addSource(allCommandMutableLiveData) { commands: List<Command> ->
            filterCommandsByMode(commands = commands)
        }

        commandToDisplayMediatorLiveData.addSource(displayModeMutableLiveData) { mode : CommandDisplayMode ->
            filterCommandsByMode(mode = mode)

        }
    }

    private fun filterCommandsByMode(commands: List<Command>? = null, mode: CommandDisplayMode? = null ){
        val commands = commands ?: allCommandMutableLiveData.value ?: emptyList()
        val modeToFilter = mode ?: displayModeMutableLiveData.value ?: CommandDisplayMode.TO_COME

        val commandToDisplay = commands.filter { it.statusCode in modeToFilter.statusCode }.apply {
            when (modeToFilter) {
                CommandDisplayMode.TO_COME -> filter {
                    it.deliveryDate?.toLocalDate()!!.isAfter(LocalDate.now())
                }
                else -> this.sortedBy { it.deliveryDate?.toLocalDate() }
            }
        }
        commandToDisplayMediatorLiveData.setValue(commandToDisplay)
    }

    fun retrieveSaveMode(){
        // Set display mode by stored mode in datastore at initialization
        viewModelScope.launch {
            commandPrefsManager.getLastCommandFilter().collect {
                displayModeMutableLiveData.value = CommandDisplayMode.fromVal(it)
            }
        }
    }

    fun setDisplayMode(displayMode: CommandDisplayMode) {
        viewModelScope.launch {
            displayModeMutableLiveData.value = displayMode
            commandPrefsManager.saveLastCommandFilter(displayMode)
        }
    }
}