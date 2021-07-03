package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory
import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity

class ArticleEntityMapper : Mapper<Article, ArticleEntity> {

    override fun from(t: ArticleEntity): Article {
        return Article(
            id = t.id,
            name = t.name,
            category = ArticleCategory.SALTED,
            count = 0,
            unitPrice = 10.0
        )
    }

    override fun to(t: Article): ArticleEntity {
        return ArticleEntity(
            id = 0L,
            name = t.name,
            count = 0,
            unitPrice = 10.0
        )
    }
}