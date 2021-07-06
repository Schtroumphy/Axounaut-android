package com.jeanloth.project.android.kotlin.domain.usescases.usecases.article

import com.jeanloth.project.android.kotlin.data.contracts.CommandContract
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import kotlinx.coroutines.flow.Flow

class ObserveCommandsUseCase(
    private val commandContract: CommandContract
) {

    fun invoke() : Flow<List<Command>> = commandContract.observeCommands()

}