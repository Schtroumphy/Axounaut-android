package com.jeanloth.project.android.kotlin.domain.usescases.usecases

import com.jeanloth.project.android.kotlin.data.contracts.ArticleWrapperContract

class ObserveArticleWrappersByCommandIdUseCase(
    private val articleWrapperContract: ArticleWrapperContract
) {

    fun invoke(commandId : Long) = articleWrapperContract.observeArticleWrappersByCommandId(commandId)

}