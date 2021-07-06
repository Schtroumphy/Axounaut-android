package com.jeanloth.project.android.kotlin.local.repository

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.Command
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleDatasourceContract
import com.jeanloth.project.android.kotlin.local.contracts.LocalCommandDatasourceContract
import com.jeanloth.project.android.kotlin.local.database.ArticleDAO
import com.jeanloth.project.android.kotlin.local.database.ArticleWrapperDAO
import com.jeanloth.project.android.kotlin.local.database.CommandDAO
import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity
import com.jeanloth.project.android.kotlin.local.entities.CommandEntity
import com.jeanloth.project.android.kotlin.local.mappers.AppClientEntityMapper
import com.jeanloth.project.android.kotlin.local.mappers.ArticleEntityMapper
import com.jeanloth.project.android.kotlin.local.mappers.ArticleWrapperEntityMapper
import com.jeanloth.project.android.kotlin.local.mappers.CommandEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CommandLocalDatasourceRepository(
    private val dao : CommandDAO,
    private val articleWrapperDao : ArticleWrapperDAO,
    private val articleDao : ArticleDAO,
    private val mapper : CommandEntityMapper,
    private val articleWrapperMapper : ArticleWrapperEntityMapper,
    private val articleMapper : ArticleEntityMapper,
    private val clientMapper : AppClientEntityMapper,
) : LocalCommandDatasourceContract{

    override fun getAllCommands(): List<Command> {
        return dao.all.map { mapper.from(it as CommandEntity) }
    }

    override fun observeAllCommands(): Flow<List<Command>> {
        val result =  dao.observeAll{ it.filter { true } }.map {
            it.map {
                mapper.from(it)
            }
        }
        return result
    }

    override fun saveCommand(command: Command): Boolean {
        print("[CommandLocalDSRepository] : Save command - Command : $command")
        print("[CommandLocalDSRepository] : Save command - Command Entity: ${mapper.to(command)}")

        val commandEntity = mapper.to(command)

        //add each article wrapper to command entity
        command.articleWrappers.forEach {
            val articleWrapperEntity = articleWrapperMapper.to(it)
            articleWrapperDao.box.put(articleWrapperEntity)

            commandEntity.articleWrappers.add(articleWrapperEntity)
        }
        // Associate command t one client by toOne relation
        commandEntity.client.target = clientMapper.to(command.client!!)

        // Save tha command entity
        dao.box.put(commandEntity)
        return true
    }

    override fun deleteCommand(command: Command): Boolean {
        var result = dao.box.remove(mapper.to(command))
        print("[CommandLocalDSRepository] : delete Command result : $result")
        return true
    }


}