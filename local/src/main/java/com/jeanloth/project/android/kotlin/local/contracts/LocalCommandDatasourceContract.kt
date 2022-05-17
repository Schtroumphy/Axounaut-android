package com.jeanloth.project.android.kotlin.local.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import kotlinx.coroutines.flow.Flow

interface LocalCommandDatasourceContract {

    fun getAllCommands() : List<Command>

    fun observeAllCommands() : Flow<List<Command>>

    fun observeCommandsByStatus(statuses : List<CommandStatusType>) : Flow<List<Command>>

    fun getCommandsByStatus(statuses : List<CommandStatusType>) : List<Command>

    fun observeCommandById(commandId : Long) : Flow<Command?>

    fun saveCommand(command: Command) : Long

    fun deleteCommand(command: Command) : Boolean

    fun getCommandById(commandId: Long): Command?
}