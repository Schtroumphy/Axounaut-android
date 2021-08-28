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
        if(command.statusCode != CommandStatusType.DELIVERED.code && command.articleWrappers.all { it.statusCode == ArticleWrapperStatusType.DONE.code}){
            command.statusCode = CommandStatusType.DONE.code
            commandContract.saveCommand(command)
        } else {
            if(command.statusCode == CommandStatusType.DONE.code){
                command.statusCode = CommandStatusType.IN_PROGRESS.code
                commandContract.saveCommand(command)
            }
        }
        return commandId
    }

    fun updateCommandStatus(command: Command, status: CommandStatusType){
        command.statusCode = status.code
        invoke(command)
    }
}