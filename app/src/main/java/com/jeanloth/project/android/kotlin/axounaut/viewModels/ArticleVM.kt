package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.GetAllArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.SaveArticleUseCase

class ArticleVM (
    private val getAllArticlesUseCase : GetAllArticlesUseCase,
    private val saveArticleUseCase: SaveArticleUseCase,
): ViewModel() {

    var articles : List<Article> = emptyList()

    init {

    }

    fun getAllArticles(): List<Article> {
        articles = getAllArticlesUseCase.invoke()
        Log.d("[ArticleVM]", " Articles : $articles")
        return articles
    }

    fun saveArticle() {
        // Save article
        val article = Article(
            name = "Test 1"
        )
        saveArticleUseCase.invoke(article)
    }


}