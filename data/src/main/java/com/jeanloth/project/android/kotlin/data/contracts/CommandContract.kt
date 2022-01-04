package com.jeanloth.project.android.kotlin.data.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import kotlinx.coroutines.flow.Flow

interface CommandContract {

    fun getAllCommands() : List<Command>

    fun observeCommands() : Flow<List<Command>>

    fun observeCommandsByStatus(statuses : List<CommandStatusType>) : Flow<List<Command>>

    fun observeCommandById(commandId : Long) : Flow<Command?>

    fun getCommandById(commandId : Long) : Command?

    fun saveCommand(command: Command) : Long

    fun deleteCommand(command: Command) : Boolean
}