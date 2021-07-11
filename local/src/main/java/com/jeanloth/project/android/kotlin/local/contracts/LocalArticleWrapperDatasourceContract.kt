package com.jeanloth.project.android.kotlin.local.contracts

import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import kotlinx.coroutines.flow.Flow

interface LocalArticleWrapperDatasourceContract {

    fun getAllArticleWrappers() : List<ArticleWrapper>

    fun observeAllArticleWrappers() : Flow<List<ArticleWrapper>>

    fun saveArticleWrapper(articleWrapper: ArticleWrapper) : Boolean

    fun deleteArticleWrapper(articleWrapper: ArticleWrapper) : Boolean

    fun observeArticleWrappersByCommandId(commandId: Long): Flow<List<ArticleWrapper>>
}