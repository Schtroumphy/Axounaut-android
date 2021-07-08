package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapperStatusType.Companion.getArticleWrapperStatusFromCode
import com.jeanloth.project.android.kotlin.local.entities.ArticleWrapperEntity

class ArticleWrapperEntityMapper(val articleMapper : ArticleEntityMapper) : Mapper<ArticleWrapper, ArticleWrapperEntity> {

    override fun from(t: ArticleWrapperEntity): ArticleWrapper {

        return ArticleWrapper(
            articleWrapperId = t.articleWrapperId,
            commandId = t.command.targetId,
            article = articleMapper.from(t.article.target),
            count = t.count,
            totalArticleWrapperPrice = t.totalArticleWrapperPrice,
            statusCode = t.statusCode
        )
    }

    override fun to(t: ArticleWrapper): ArticleWrapperEntity {
        val articleWrapperEntity =  ArticleWrapperEntity(
            articleWrapperId = t.articleWrapperId,
            count = t.count,
            totalArticleWrapperPrice = t.totalArticleWrapperPrice,
            statusCode = t.statusCode
        )
        articleWrapperEntity.command.targetId = t.commandId
        articleWrapperEntity.article.target = articleMapper.to(t.article)
        return articleWrapperEntity
    }
}