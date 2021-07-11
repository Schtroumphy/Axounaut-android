package com.jeanloth.project.android.kotlin.local.repository

import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleWrapper
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleWrapperDatasourceContract
import com.jeanloth.project.android.kotlin.local.database.ArticleWrapperDAO
import com.jeanloth.project.android.kotlin.local.entities.ArticleWrapperEntity
import com.jeanloth.project.android.kotlin.local.entities.ArticleWrapperEntity_
import com.jeanloth.project.android.kotlin.local.mappers.ArticleWrapperEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleWrapperLocalDatasourceRepository(
    private val dao : ArticleWrapperDAO,
    private val mapper : ArticleWrapperEntityMapper,
) : LocalArticleWrapperDatasourceContract {

    override fun getAllArticleWrappers(): List<ArticleWrapper> {
        return dao.all.map { mapper.from(it as ArticleWrapperEntity) }
    }

    override fun observeAllArticleWrappers(): Flow<List<ArticleWrapper>> {
        val result =  dao.observeAll{ it.filter { true } }.map {
            it.map {
                mapper.from(it)
            }
        }
        return result
    }

    override fun saveArticleWrapper(articleWrapper: ArticleWrapper): Boolean {
        print("[ArticleWrapperLocalDSRepository] : Save AW  : $articleWrapper")
        print("[ArticleWrapperLocalDSRepository] : Save AW Entity: ${mapper.to(articleWrapper)}")

        val articleWrapperEntity = mapper.to(articleWrapper)

        // Save the articleWrapperEntity entity
        dao.box.put(articleWrapperEntity)
        return true
    }

    override fun deleteArticleWrapper(articleWrapper: ArticleWrapper): Boolean {
        var result = dao.box.remove(mapper.to(articleWrapper))
        print("[ArticleWrapperLocalDSRepository] : delete AticleWrapper result : $result")
        return true
    }

    override fun observeArticleWrappersByCommandId(commandId: Long): Flow<List<ArticleWrapper>> {
        var result = dao.observeAll{ it.equal(ArticleWrapperEntity_.commandId, commandId)}.map {
            it.map {
                mapper.from(it)
            }
        }
        return result
    }


}