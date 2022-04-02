package com.jeanloth.project.android.kotlin.local.mappers

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.RecipeWrapper
import com.jeanloth.project.android.kotlin.local.database.ArticleDAO
import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity

class ArticleEntityMapper(
    private val recipeWrapperEntityMapper: RecipeWrapperEntityMapper,
    private val articleDao: ArticleDAO
) : Mapper<Article, ArticleEntity> {

    override fun from(t: ArticleEntity): Article {
        return Article(
            id = t.id,
            label = t.label,
            price = t.price,
            timeOrdered = t.timeOrdered,
            category = t.category,
            recipeIngredients = t.recipeWrappers.map { recipeWrapperEntityMapper.from(it)}
        )
    }

    override fun to(t: Article): ArticleEntity {
        val articleEntity = ArticleEntity(
            id = t.id,
            label = t.label,
            price = t.price,
            timeOrdered = t.timeOrdered,
            category = t.category,
        )
        articleDao.box.attach(articleEntity)
        // Add recipeWrappers converted
        t.recipeIngredients.map { recipeWrapperEntityMapper.to(it) }.forEach {
            articleEntity.recipeWrappers.add(it)
        }
        return articleEntity
    }
}