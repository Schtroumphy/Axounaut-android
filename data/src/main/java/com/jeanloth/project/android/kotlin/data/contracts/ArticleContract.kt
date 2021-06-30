package com.jeanloth.project.android.kotlin.domain.contracts

import com.jeanloth.project.android.kotlin.domain.entities.Article

interface ArticleContract {

    fun getAllArticles() : List<Article>
}