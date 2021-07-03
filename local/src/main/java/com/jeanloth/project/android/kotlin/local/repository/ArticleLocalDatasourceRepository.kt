package com.jeanloth.project.android.kotlin.local.repository

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleDatasourceContract
import com.jeanloth.project.android.kotlin.local.database.ArticleDAO
import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity
import com.jeanloth.project.android.kotlin.local.mappers.ArticleEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ArticleLocalDatasourceRepository(
    private val dao : ArticleDAO,
    private val mapper : ArticleEntityMapper,
) : LocalArticleDatasourceContract{

    override fun getAllArticles(): List<Article> {
       return dao.all.map { mapper.from(it as ArticleEntity) }
    }

    // put method returns id of the entity added
    override fun saveArticle(article: Article): Boolean {
        print("[ArticleLocalDSRepository] : Save article - Article : $article")
        print("[ArticleLocalDSRepository] : Save article - Article Entity: ${mapper.to(article)}")
        dao.box.put(mapper.to(article))
        return true
    }

    override fun observeAllArticles(): Flow<List<Article>> {
        val result =  dao.observeAll{
            it.filter { true }
        }.map {
            it.map {
                mapper.from(it)
            }
        }
        return result
    }


}