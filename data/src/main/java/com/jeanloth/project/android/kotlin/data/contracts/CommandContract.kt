package com.jeanloth.project.android.kotlin.data.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import kotlinx.coroutines.flow.Flow

interface CommandContract {

    fun getAllCommands() : List<Command>

    fun observeCommands() : Flow<List<Command>>

    fun observeCommandById(commandId : Long) : Flow<Command?>

    fun saveCommand(command: Command) : Boolean

    fun deleteCommand(command: Command) : Boolean
}