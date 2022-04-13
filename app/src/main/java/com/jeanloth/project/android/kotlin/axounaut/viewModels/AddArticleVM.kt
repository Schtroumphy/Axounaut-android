package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.axounaut.extensions.displayPreparingTime
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.DeleteArticleUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.GetAllArticlesUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.article.SaveArticleUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper.ObserveAllIngredientWrappersUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper.SaveIngredientWrapperUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory
import com.jeanloth.project.android.kotlin.domain_models.entities.ArticleCategory.Companion.getArticleCategoryFromCode
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper.Companion.toIngredientWrapper
import com.jeanloth.project.android.kotlin.domain_models.entities.RecipeWrapper.Companion.toRecipeWrapper
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddArticleVM (
    private val saveArticleUseCase: SaveArticleUseCase,
    private val observeAllIngredientWrappersUseCase: ObserveAllIngredientWrappersUseCase,
    private val saveIngredientWrapperUseCase: SaveIngredientWrapperUseCase,
    ): ViewModel() {

    private var TAG = "[Add Article VM]"
    private var stepCount : Int = 1

    // Article to edit
    private var articleToEdit: Article? = null

    // Step 1
    private var _nameMutableLiveData : MutableLiveData<String> = MutableLiveData("")
    val nameLiveData : LiveData<String> = _nameMutableLiveData
    private var _categoryMutableLiveData : MutableLiveData<ArticleCategory> = MutableLiveData(ArticleCategory.SALTED)
    val categoryLiveData : LiveData<ArticleCategory> = _categoryMutableLiveData
    private var _priceMutableLiveData : MutableLiveData<Int> = MutableLiveData(0)
    val priceLiveData : LiveData<Int> = _priceMutableLiveData

    // Step 2
    private val observeIngredientsMutableLD = MutableLiveData<List<IngredientWrapper>>(emptyList())
    val observeIngredientsLD : LiveData<List<IngredientWrapper>> = observeIngredientsMutableLD
    private var checkedItemsMLD = MutableLiveData<MutableList<IngredientWrapper>>(mutableListOf())
    val checkedItemsLD : LiveData<MutableList<IngredientWrapper>> = checkedItemsMLD
    val result: MediatorLiveData<MutableList<IngredientWrapper>> = MediatorLiveData()

    // Step 3
    private var _timePreparingMutableLiveData : MutableLiveData<Float> = MutableLiveData(0f)
    var timePreparingLiveData : LiveData<Float> = _timePreparingMutableLiveData

    // Can resume
    private var _canResumeMLD = MutableLiveData<Boolean>(false)
    var canResumeLD = _canResumeMLD

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
            if(stepCount == 3) canResume()
            result.value = observeIngredientsMutableLD.value?.toMutableList()
        }

        result.addSource(observeIngredientsMutableLD){
            it.forEach {
                it.isSelected = checkedItemsMLD.value?.contains(it) == true
            }
            result.value = it.toMutableList()
        }
    }

    fun setArticleToEdit(articleToEdit: Article?){
        this.articleToEdit = articleToEdit
        setArticleName(articleToEdit?.label ?: "")
        setArticleCategory(getArticleCategoryFromCode(articleToEdit?.category))
        setPrice(articleToEdit?.price ?: 0)
        _timePreparingMutableLiveData.value = articleToEdit?.preparingTime ?: 0f
    }

    fun updateCheckedListByArticleToEdit(){
        articleToEdit?.let {
            it.recipeIngredients.forEach {
                updateCheckedItemsList(it.toIngredientWrapper(true), true)
            }
        }
        Log.d(TAG, "Checked list ${checkedItemsMLD.value}")
    }

    private fun canResume(){
        val isOk = when(stepCount){
            1 -> _priceMutableLiveData.value?.let {
                _nameMutableLiveData.value?.isNotBlank() == true && it > 0
            }
            2 -> checkedItemsMLD.value?.isNotEmpty()
            3 -> _timePreparingMutableLiveData.value?.let {
                it > 0f && checkedItemsMLD.value?.all { it.quantity > 0 } == true
            }
            else -> true
        }

        Log.d(TAG, "Can resume step $stepCount ? $isOk")
        viewModelScope.launch {
            _canResumeMLD.postValue(isOk ?: false)
        }
    }

    fun saveIngredientWrapper(pw: IngredientWrapper) {
        saveIngredientWrapperUseCase.invoke(pw)
    }

    fun saveArticle() {
        val articleToAdd = Article(
            id = articleToEdit?.id ?: 0L,
            label = _nameMutableLiveData.value ?: "Error",
            price = _priceMutableLiveData.value?.toInt() ?: 0,
            preparingTime = _timePreparingMutableLiveData.value?: 0.5f,
            category = categoryLiveData.value?.code ?: ArticleCategory.SALTED.code,
            recipeIngredients = checkedItemsLD.value?.toRecipeWrapper(true) ?: mutableListOf()
        )
        Log.d(TAG, "Article to save : $articleToAdd")
        saveArticleUseCase.invoke(articleToAdd)
    }

    fun updateCheckedItemsList(item: IngredientWrapper, checked: Boolean) {
        if(checked){
            if(checkedItemsMLD.value?.contains(item) == false) checkedItemsMLD.value?.add(item)
        } else {
            if(checkedItemsMLD.value?.contains(item) == true) checkedItemsMLD.value?.remove(item)
        }
        checkedItemsMLD.postValue(checkedItemsMLD.value)
        canResume()
    }

    fun displayHour() : String = displayPreparingTime(_timePreparingMutableLiveData.value)

    fun checkedItemsHasChanged() {
        canResume()
    }

    /** Setter **/

    fun setStepCount(step : Int){
        stepCount = step
        Log.d(TAG, "Set step count to : $stepCount")
        canResume()
    }

    fun setArticleName(name : String){
        _nameMutableLiveData.value = name
        Log.d(TAG, "Set name to : ${_nameMutableLiveData.value}")
        canResume()
    }

    fun setArticleCategory(category : ArticleCategory){
        _categoryMutableLiveData.value = category
        Log.d(TAG, "Set category to : ${_categoryMutableLiveData.value}")
        canResume()
    }

    private fun setPrice(price : Int){
        _priceMutableLiveData.value = price
    }

    fun setPrice(adding : Boolean = false){
        _priceMutableLiveData.value =
            if(adding) {
                _priceMutableLiveData.value?.plus(5)
            }  else if(_priceMutableLiveData.value!! > 0) {
                _priceMutableLiveData.value?.minus(5)
            } else _priceMutableLiveData.value
        Log.d(TAG, "Set price to : ${_priceMutableLiveData.value}")
        canResume()
    }

    fun setTimePreparingMutableLiveData(increase : Boolean){
        val time = _timePreparingMutableLiveData.value ?: 0f
        _timePreparingMutableLiveData.value = if(increase) time + 0.5f else if(time > 0f) time - 0.5f else 0f
        Log.d(TAG, "Set preparing time to : ${_timePreparingMutableLiveData.value}")
        canResume()
    }

    fun clearData() {
        _nameMutableLiveData.value = ""
        _priceMutableLiveData.value = 0
        _categoryMutableLiveData.value = ArticleCategory.SALTED
        checkedItemsMLD.value?.clear()
        _timePreparingMutableLiveData.value = 0f
        observeIngredientsMutableLD.value?.forEach {
            it.isSelected = false
            it.quantity = 0F
        }
    }

    fun isEditMode(): Boolean = articleToEdit != null
}