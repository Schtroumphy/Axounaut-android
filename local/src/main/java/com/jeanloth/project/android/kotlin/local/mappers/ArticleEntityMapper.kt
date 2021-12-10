package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.RecipeWrapper
import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity

class ArticleEntityMapper(
    private val recipeWrapperEntityMapper: RecipeWrapperEntityMapper
) : Mapper<Article, ArticleEntity> {

    override fun from(t: ArticleEntity): Article {
        val article =  Article(
            id = t.id,
            label = t.label,
            price = t.price,
            timeOrdered = t.timeOrdered,
            category = t.category
        )

        // Add article wrappers
        val recipeWrapperList = mutableListOf<RecipeWrapper>()
        t.recipeWrappers.map { recipeWrapperEntityMapper.from(it) }.forEach {
            recipeWrapperList.add(it)
        }
        article.recipeIngredients = recipeWrapperList
        return article
    }

    override fun to(t: Article): ArticleEntity {
        return ArticleEntity(
            id = t.id,
            label = t.label,
            price = t.price,
            timeOrdered = t.timeOrdered,
            category = t.category
        )
    }
}