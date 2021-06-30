package com.jeanloth.project.android.kotlin.data.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.Article

interface ArticleContract {

    fun getAllArticles() : List<Article>

    fun saveArticle(article: Article) : Boolean
}