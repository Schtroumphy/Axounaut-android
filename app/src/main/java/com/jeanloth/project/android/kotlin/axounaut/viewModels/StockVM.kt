package com.jeanloth.project.android.kotlin.axounaut.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jeanloth.project.android.kotlin.axounaut.datastore.StockManager
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper.ObserveAllIngredientWrappersUseCase
import com.jeanloth.project.android.kotlin.domain.usescases.usecases.ingredientWrapper.SaveIngredientWrapperUseCase
import com.jeanloth.project.android.kotlin.domain_models.entities.Article
import com.jeanloth.project.android.kotlin.domain_models.entities.IngredientWrapper
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.time.LocalDate

class StockVM (
    private val observeAllIngredientWrappersUseCase: ObserveAllIngredientWrappersUseCase,
    private val saveIngredientWrapperUseCase: SaveIngredientWrapperUseCase,
    private val stockManager: StockManager,
): ViewModel() {

    private val observePWMutableLiveData = MutableLiveData<List<IngredientWrapper>>(emptyList())
    fun observePWLiveData() : LiveData<List<IngredientWrapper>> = observePWMutableLiveData

    init {

        viewModelScope.launch {
            observeAllIngredientWrappersUseCase.invoke().collect {
                Log.d("[StockVM]", " Ingredient wrappers observed : $it")
                observePWMutableLiveData.postValue(it)
            }
        }
    }

    fun savePWs(pws : List<IngredientWrapper>) {
        pws.forEach{ saveIngredientWrapper(it)}
    }

    fun saveIngredientWrapper(pw: IngredientWrapper) {
        saveIngredientWrapperUseCase.invoke(pw)
    }

    fun updateStockLastUpdateDate(){
        viewModelScope.launch {
            stockManager.saveStockLastUpdateDate(LocalDate.now().toString())
        }
    }

    fun deleteArticle(article : Article) {
        //deleteArticleUseCase.invoke(article)
    }


}