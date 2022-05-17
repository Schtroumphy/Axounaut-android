package com.jeanloth.project.android.kotlin.data.repositories

import com.jeanloth.project.android.kotlin.data.contracts.CommandContract
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import com.jeanloth.project.android.kotlin.local.contracts.LocalCommandDatasourceContract
import kotlinx.coroutines.flow.Flow

class CommandRepository(
    private val localCommandDatasourceRepository : LocalCommandDatasourceContract
) : CommandContract{

    override fun observeAllCommands(): Flow<List<Command>> {
        return localCommandDatasourceRepository.observeAllCommands()
    }

    override fun observeCommandsByStatus(statuses: List<CommandStatusType>): Flow<List<Command>> {
        return localCommandDatasourceRepository.observeCommandsByStatus(statuses)
    }

    override fun getCommandsByStatus(statuses: List<CommandStatusType>): List<Command> {
        return localCommandDatasourceRepository.getCommandsByStatus(statuses)
    }

    override fun observeCommandById(commandId: Long): Flow<Command?> {
        return localCommandDatasourceRepository.observeCommandById(commandId)
    }

    override fun getCommandById(commandId: Long): Command? {
        return localCommandDatasourceRepository.getCommandById(commandId)
    }

    override fun saveCommand(command: Command): Long {
        return localCommandDatasourceRepository.saveCommand(command)
    }

    override fun deleteCommand(command: Command): Boolean {
        return localCommandDatasourceRepository.deleteCommand(command)
    }
}