package com.jeanloth.project.android.kotlin.data.repositories

import com.jeanloth.project.android.kotlin.data.contracts.ArticleContract
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleDatasourceContract
import kotlinx.coroutines.flow.Flow

class ArticleRepository(
    val localArticleDatasourceContract : LocalArticleDatasourceContract
) : ArticleContract{

    override fun getAllArticles(): List<Article> {
        return localArticleDatasourceContract.getAllArticles()
    }

    override fun observeArticles(): Flow<List<Article>> {
        return localArticleDatasourceContract.observeAllArticles()
    }

    override fun saveArticle(article: Article): Boolean {
        return localArticleDatasourceContract.saveArticle(article)
    }

    override fun deleteArticle(article: Article): Boolean {
        return localArticleDatasourceContract.deleteArticle(article)
    }
}