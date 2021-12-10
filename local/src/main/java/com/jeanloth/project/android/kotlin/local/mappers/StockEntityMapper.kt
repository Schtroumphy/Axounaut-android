package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.*
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory.Companion.getArticleCategoryFromLabel
import com.jeanloth.project.android.kotlin.local.database.CommandDAO
import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity
import com.jeanloth.project.android.kotlin.local.entities.CommandEntity
import java.time.LocalDate

class CommandEntityMapper(
    private val articleWrapperMapper: ArticleWrapperEntityMapper,
    val clientMapper : AppClientEntityMapper,
    ) :
    Mapper<Command, CommandEntity> {

    override fun from(t: CommandEntity): Command {
        val command = Command(
            idCommand = t.idCommand,
            deliveryDate = t.deliveryDate,
            statusCode = t.statusCode,
            articleWrappers = mutableListOf<ArticleWrapper>(),
            client = clientMapper.from(t.client.target),
            reduction = t.reduction,
            paymentAmount = t.paymentAmount,
            paymentTypeCode = t.paymentTypeCode
        )

        // Add article wrappers
        val articleWrapperList = mutableListOf<ArticleWrapper>()
        t.articleWrappers.map { articleWrapperMapper.from(it) }.forEach {
            articleWrapperList.add(it)
        }
        command.articleWrappers = articleWrapperList
        return command
    }

    override fun to(t: Command): CommandEntity {
        val commandEntity = CommandEntity(
            idCommand = t.idCommand,
            deliveryDate = t.deliveryDate,
            statusCode = t.statusCode,
            totalPrice = t.totalPrice,
            reduction = t.reduction,
            paymentAmount = t.paymentAmount,
            paymentTypeCode = t.paymentTypeCode
        )
        // Attach entity first
        //commandDao.box.attach(commandEntity)

        // Add article wrappers converted
        /*t.articleWrappers.map { articleWrapperMapper.to(it) }.forEach {
            it.command.targetId = t.idCommand
            commandEntity.articleWrappers.add(it)
        }*/
        // Associate the client
        commandEntity.client.target = clientMapper.to(t.client!!)

        return commandEntity
    }
}