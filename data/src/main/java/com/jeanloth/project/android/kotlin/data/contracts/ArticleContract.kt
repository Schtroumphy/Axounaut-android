package com.jeanloth.project.android.kotlin.data.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import kotlinx.coroutines.flow.Flow

interface ArticleContract {

    fun getAllArticles() : List<Article>

    fun observeArticles() : Flow<List<Article>>

    fun saveArticle(article: Article) : Boolean
}