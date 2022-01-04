package com.jeanloth.project.android.kotlin.local.repository

import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.domain_models.entities.CommandStatusType
import com.jeanloth.project.android.kotlin.local.contracts.LocalCommandDatasourceContract
import com.jeanloth.project.android.kotlin.local.database.ArticleDAO
import com.jeanloth.project.android.kotlin.local.database.ArticleWrapperDAO
import com.jeanloth.project.android.kotlin.local.database.CommandDAO
import com.jeanloth.project.android.kotlin.local.entities.CommandEntity
import com.jeanloth.project.android.kotlin.local.entities.CommandEntity_
import com.jeanloth.project.android.kotlin.local.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.local.mappers.ArticleEntityMapper
import com.jeanloth.project.android.kotlin.local.mappers.ArticleWrapperEntityMapper
import com.jeanloth.project.android.kotlin.local.mappers.CommandEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map

class CommandLocalDatasourceRepository(
    private val dao: CommandDAO,
    private val articleDao: ArticleDAO,
    private val awDao: ArticleWrapperDAO,
    private val articleWrapperDAO: ArticleWrapperDAO,
    private val mapper: CommandEntityMapper,
    private val clientMapper: AppClientEntityMapper,
    private val articleMapper: ArticleEntityMapper,
    private val articleWrapperMapper: ArticleWrapperEntityMapper,
) : LocalCommandDatasourceContract {

    override fun getAllCommands(): List<Command> {
        return dao.all.map { mapper.from(it as CommandEntity) }
    }

    override fun observeAllCommands(): Flow<List<Command>> {
        val result = dao.observeAll { it.filter { true } }.map {
            it.map {
                mapper.from(it)
            }
        }
        return result
    }

    override fun observeCommandsByStatus(statuses: List<CommandStatusType>): Flow<List<Command>> {
        val codes = statuses.map { it.code }
        return dao.observeAll {
            it.filter{ command ->
                codes.contains(command.statusCode)
            }
        }.map {
            it.map {
                mapper.from(it)
            }
        }
    }

    override fun observeCommandById(commandId: Long): Flow<Command?> {
        try {
            val result = dao.observeFirst { it.equal(CommandEntity_.idCommand, commandId) }
            .map { it?.let { mapper.from(it) } }

            return result
        } catch (e: Exception) {
            print("ERROR observe command by id : $e")
            return emptyFlow()
        }
    }

    override fun getCommandById(commandId: Long): Command? {
        return mapper.from(dao.box.get(commandId))
    }

    override fun saveCommand(command: Command): Long {
        print("[CommandLocalDSRepository] : Save command - Command : $command")
        print("[CommandLocalDSRepository] : Save command - Command Entity: ${mapper.to(command)}")

        if(command.statusCode == CommandStatusType.DELIVERED.code){
            // update all article status not done
            command.articleWrappers.filter {
                it.statusCode != ArticleWrapperStatusType.DONE.code }
                .forEach { it.statusCode = ArticleWrapperStatusType.CANCELED.code }
        }

        val commandEntity = mapper.to(command)

        // If command already exists
        dao.box.query().equal(CommandEntity_.idCommand, command.idCommand).build().findUnique()?.let{

            commandEntity.idCommand = it.idCommand

            // Remove all articleWrappers
            it.articleWrappers.forEach {
                awDao.box.remove(it)
            }
        }

        // Associate command t one client by toOne relation
        commandEntity.client.target = clientMapper.to(command.client!!)

        // Save the command entity
        val commandId = dao.box.put(commandEntity)

        command.articleWrappers.forEach {
            it.commandId = commandId
            if(command.statusCode == CommandStatusType.PAYED.code || command.statusCode == CommandStatusType.INCOMPLETE_PAYMENT.code ){
                if(it.statusCode == ArticleWrapperStatusType.DONE.code) {
                    it.article.timeOrdered = it.article.timeOrdered + it.count
                    articleDao.box.put(articleMapper.to(it.article))
                }
            }
            articleWrapperDAO.box.put(articleWrapperMapper.to(it))
        }
        return commandId
    }

    override fun deleteCommand(command: Command): Boolean {
        command.articleWrappers.forEach {
            it.article.timeOrdered = it.article.timeOrdered - it.count  // Delete time ordered for each article of this command
            articleDao.box.put(articleMapper.to(it.article))
        }
        var result = dao.box.remove(mapper.to(command))
        print("[CommandLocalDSRepository] : delete Command result : $result")
        return true
    }


}