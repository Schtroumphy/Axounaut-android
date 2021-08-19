package com.jeanloth.project.android.kotlin.data.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import kotlinx.coroutines.flow.Flow

interface ArticleWrapperContract {

    fun getAllArticleWrappers() : List<ArticleWrapper>

    fun observeArticleWrappers() : Flow<List<ArticleWrapper>>

    fun observeArticleWrappersByCommandId(commandId: Long): Flow<List<ArticleWrapper>>

    fun saveArticleWrapper(articleWrapper: ArticleWrapper) : Boolean

    fun deleteArticleWrapper(articleWrapper: ArticleWrapper)
}