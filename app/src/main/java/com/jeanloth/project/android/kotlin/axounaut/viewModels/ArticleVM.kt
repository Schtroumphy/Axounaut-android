package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.DeleteArticleUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.GetAllArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ObserveArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.SaveArticleUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ArticleVM (
    private val getAllArticlesUseCase : GetAllArticlesUseCase,
    private val observeAllArticlesUseCase : ObserveArticlesUseCase,
    private val saveArticleUseCase: SaveArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
): ViewModel() {

    var articles : List<Article> = emptyList()

    var allArticleMutableLiveData : MutableLiveData<List<Article>> = MutableLiveData(emptyList())
    fun allArticlesLiveData() : LiveData<List<Article>> = allArticleMutableLiveData

    init {

        viewModelScope.launch {
            observeAllArticlesUseCase.invoke().collect {
                Log.d("[ArticleVM]", " Articles observed : $it")
                allArticleMutableLiveData.postValue(it)
            }
        }
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

    fun deleteArticle(article : Article) {
        deleteArticleUseCase.invoke(article)
    }


}