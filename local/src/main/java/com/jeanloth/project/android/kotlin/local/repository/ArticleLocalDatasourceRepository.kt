package com.jeanloth.project.android.kotlin.local.repository

import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.local.contracts.LocalArticleDatasourceContract
import com.jeanloth.project.android.kotlin.local.database.ArticleDAO
import com.jeanloth.project.android.kotlin.local.entities.ArticleEntity
import com.jeanloth.project.android.kotlin.local.mappers.ArticleEntityMapper

class ArticleLocalDatasourceRepository(
    private val dao : ArticleDAO,
    private val mapper : ArticleEntityMapper,
) : LocalArticleDatasourceContract{

    override fun getAllArticles(): List<Article> {
       return dao.all.map { mapper.from(it as ArticleEntity) }
    }

    override fun saveArticle(article: Article): Boolean {
        print("[ArticleLocalDSRepository] : Save article - Article : $article")
        print("[ArticleLocalDSRepository] : Save article - Article Entity: ${mapper.to(article)}")
        val result = dao.box.put(mapper.to(article))
        return result == 1L
    }


}