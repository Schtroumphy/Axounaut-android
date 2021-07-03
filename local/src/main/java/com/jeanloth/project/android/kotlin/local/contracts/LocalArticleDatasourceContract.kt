package com.jeanloth.project.android.kotlin.local.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import kotlinx.coroutines.flow.Flow


interface LocalArticleDatasourceContract {

    fun getAllArticles() : List<Article>

    fun observeAllArticles() : Flow<List<Article>>

    fun saveArticle(article: Article) : Boolean
}