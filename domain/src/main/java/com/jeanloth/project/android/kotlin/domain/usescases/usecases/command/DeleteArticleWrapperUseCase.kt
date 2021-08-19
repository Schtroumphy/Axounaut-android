package com.jeanloth.project.android.kotlin.domain.usescases.usecases.command

import com.jeanloth.project.android.kotlin.data.contracts.ArticleWrapperContract
import com.jeanloth.project.android.kotlin.data.contracts.CommandContract
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.Command

class DeleteArticleWrapperUseCase(
    private val articleWrapperContract : ArticleWrapperContract
){

    fun invoke(articleWrapper: ArticleWrapper) {
        return articleWrapperContract.deleteArticleWrapper(articleWrapper)
    }
}