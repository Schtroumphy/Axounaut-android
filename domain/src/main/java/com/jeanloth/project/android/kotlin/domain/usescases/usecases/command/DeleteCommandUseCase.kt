package com.jeanloth.project.android.kotlin.domain.usescases.usecases.command

import com.jeanloth.project.android.kotlin.data.contracts.CommandContract
import com.jeanloth.project.android.kotlin.domain_models.entities.Command

class DeleteCommandUseCase(
    private val commandContract : CommandContract
){

    fun invoke(command: Command) : Boolean {
        return commandContract.deleteCommand(command)
    }
}