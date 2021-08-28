package com.jeanloth.project.android.kotlin.domain.usescases.usecases.command

import com.jeanloth.project.android.kotlin.data.contracts.CommandContract
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType

class SaveCommandUseCase(
    private val commandContract : CommandContract
){

    fun invoke(command: Command) : Long{
        val commandId = commandContract.saveCommand(command)
        return commandId
    }

    fun updateCommandStatus(command: Command, status: CommandStatusType){
        command.statusCode = status.code
        invoke(command)
    }
}