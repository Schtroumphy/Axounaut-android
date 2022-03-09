package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.DeleteArticleUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.GetAllArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.ObserveArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.SaveArticleUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper.ObserveAllIngredientWrappersUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper.SaveIngredientWrapperUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddArticleVM (
    private val getAllArticlesUseCase : GetAllArticlesUseCase,
    private val saveArticleUseCase: SaveArticleUseCase,
    private val deleteArticleUseCase: DeleteArticleUseCase,
    private val observeAllIngredientWrappersUseCase: ObserveAllIngredientWrappersUseCase,
    private val saveIngredientWrapperUseCase: SaveIngredientWrapperUseCase,
    ): ViewModel() {

    private var TAG = "[Add Article VM]"
    var articles : List<Article> = emptyList()

    private var nameMutableLiveData : MutableLiveData<String> = MutableLiveData("")
    var categoryMutableLiveData : MutableLiveData<ArticleCategory> = MutableLiveData(ArticleCategory.SALTED)
    var priceMutableLiveData : MutableLiveData<Int> = MutableLiveData(0)
    fun priceLiveData() : LiveData<Int> = priceMutableLiveData

    private val observeIngredientsMutableLD = MutableLiveData<List<IngredientWrapper>>(emptyList())
    private var checkedItemsMLD = MutableLiveData<MutableList<IngredientWrapper>>(mutableListOf())
    val result: MediatorLiveData<MutableList<IngredientWrapper>> = MediatorLiveData()

    init {

        viewModelScope.launch {
            observeAllIngredientWrappersUseCase.invoke().collect {
                observeIngredientsMutableLD.postValue(it)
            }
        }

        result.addSource(checkedItemsMLD) {
            observeIngredientsMutableLD.value?.forEach {
                it.isSelected = checkedItemsMLD.value?.contains(it) == true
            }
            observeIngredientsMutableLD.postValue(observeIngredientsMutableLD.value)
        }

        result.addSource(observeIngredientsMutableLD){
            result.value = it.toMutableList()
        }
    }

    fun setArticleName(name : String){
        nameMutableLiveData.value = name
        Log.d(TAG, "Set name to : ${nameMutableLiveData.value}")
    }

    fun setArticleCategory(category : ArticleCategory){
        categoryMutableLiveData.value = category
        Log.d(TAG, "Set category to : ${categoryMutableLiveData.value}")
    }

    fun setPrice(adding : Boolean = false){
        priceMutableLiveData.value =
            if(adding) {
                priceMutableLiveData.value?.plus(5)
            }  else if(priceMutableLiveData.value!! > 0) {
                priceMutableLiveData.value?.minus(5)
            } else priceMutableLiveData.value
        Log.d(TAG, "Set price to : ${priceMutableLiveData.value}")
    }

    fun saveIngredientWrapper(pw: IngredientWrapper) {
        saveIngredientWrapperUseCase.invoke(pw)
    }

    fun getAllArticles(): List<Article> {
        articles = getAllArticlesUseCase.invoke()
        Log.d(TAG, " Articles : $articles")
        return articles
    }

    fun saveArticle(articleToAdd: Article) {
        saveArticleUseCase.invoke(articleToAdd)
    }

    fun deleteArticle(article : Article) {
        deleteArticleUseCase.invoke(article)
    }

    fun updateCheckedItemsList(item: IngredientWrapper, checked: Boolean) {
        if(checked){
            if(checkedItemsMLD.value?.contains(item) == false) checkedItemsMLD.value?.add(item)
        } else {
            if(checkedItemsMLD.value?.contains(item) == true) checkedItemsMLD.value?.remove(item)
        }
        checkedItemsMLD.postValue(checkedItemsMLD.value)
    }

}