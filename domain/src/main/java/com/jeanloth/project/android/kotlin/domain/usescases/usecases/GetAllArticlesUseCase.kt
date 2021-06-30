package com.jeanloth.project.android.kotlin.domain.usescases

import com.jeanloth.project.android.kotlin.data.contracts.ArticleContract


class GetAllArticlesUseCase(
    private val articleContract : ArticleContract
) {

    fun invoke() = articleContract.getAllArticles()
}