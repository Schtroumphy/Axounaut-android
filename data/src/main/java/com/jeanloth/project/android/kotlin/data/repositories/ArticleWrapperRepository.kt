package com.jeanloth.project.android.kotlin.data.repositories

import com.jeanloth.project.android.kotlin.data.contracts.ArticleContract
import com.jeanloth.project.android.kotlin.data.contracts.ArticleWrapperContract
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleDatasourceContract
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleWrapperDatasourceContract
import kotlinx.coroutines.flow.Flow

class ArticleWrapperRepository(
    val localArticleWrapperDatasourceContract: LocalArticleWrapperDatasourceContract
) : ArticleWrapperContract {


    override fun getAllArticleWrappers(): List<ArticleWrapper> {
        return localArticleWrapperDatasourceContract.getAllArticleWrappers()
    }

    override fun observeArticleWrappers(): Flow<List<ArticleWrapper>> {
        return localArticleWrapperDatasourceContract.observeAllArticleWrappers()
    }

    override fun observeArticleWrappersByCommandId(commandId: Long): Flow<List<ArticleWrapper>> {
        return localArticleWrapperDatasourceContract.observeArticleWrappersByCommandId(commandId)
    }

    override fun saveArticleWrapper(articleWrapper: ArticleWrapper): Boolean {
        return localArticleWrapperDatasourceContract.saveArticleWrapper(articleWrapper)
    }

    override fun deleteArticleWrapper(articleWrapper: ArticleWrapper): Boolean {
        return localArticleWrapperDatasourceContract.deleteArticleWrapper(articleWrapper)
    }
}