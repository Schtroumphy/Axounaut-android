package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory
import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity

class ArticleEntityMapper : Mapper<Article, ArticleEntity> {

    override fun from(t: ArticleEntity): Article {
        return Article(
            id = t.id,
            name = t.name,
            price = t.price,
            category = t.category
        )
    }

    override fun to(t: Article): ArticleEntity {
        return ArticleEntity(
            id = t.id,
            name = t.name,
            price = t.price,
            category = t.category
        )
    }
}