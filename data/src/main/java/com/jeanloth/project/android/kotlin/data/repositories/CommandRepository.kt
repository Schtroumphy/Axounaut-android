package com.jeanloth.project.android.kotlin.data.repositories

import com.jeanloth.project.android.kotlin.data.contracts.ArticleContract
import com.jeanloth.project.android.kotlin.data.contracts.CommandContract
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleDatasourceContract
import com.jeanloth.project.android.kotlin.local.contracts.LocalCommandDatasourceContract
import kotlinx.coroutines.flow.Flow

class CommandRepository(
    private val localCommandDatasourceRepository : LocalCommandDatasourceContract
) : CommandContract{

    override fun getAllCommands(): List<Command> {
        return localCommandDatasourceRepository.getAllCommands()
    }

    override fun observeCommands(): Flow<List<Command>> {
        return localCommandDatasourceRepository.observeAllCommands()
    }

    override fun observeCommandById(commandId: Long): Flow<Command?> {
        return localCommandDatasourceRepository.observeCommandById(commandId)
    }

    override fun saveCommand(command: Command): Long {
        return localCommandDatasourceRepository.saveCommand(command)
    }

    override fun deleteCommand(command: Command): Boolean {
        return localCommandDatasourceRepository.deleteCommand(command)
    }
}