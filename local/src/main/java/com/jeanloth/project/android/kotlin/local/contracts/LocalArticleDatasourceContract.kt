package com.jeanloth.project.android.kotlin.local.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.Article


interface LocalArticleDatasourceContract {

    fun getAllArticles() : List<Article>


    fun saveArticle(article: Article) : Boolean
}