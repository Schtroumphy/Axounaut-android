package com.jeanloth.project.android.kotlin.domain.usescases.usecases

import com.jeanloth.project.android.kotlin.data.contracts.ArticleContract
import com.jeanloth.project.android.kotlin.domain_models.entities.Article

class DeleteArticleUseCase(
    private val articleContract : ArticleContract
) {

    fun invoke(article : Article) = articleContract.deleteArticle(article)

}